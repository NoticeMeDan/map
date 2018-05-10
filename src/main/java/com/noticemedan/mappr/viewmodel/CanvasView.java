package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.model.Entities;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.map.Type;
import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.model.util.OsmElementProperty;
import com.noticemedan.mappr.model.util.Rect;
import com.noticemedan.mappr.model.util.Stopwatch;
import io.vavr.collection.Vector;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.net.URISyntaxException;
import java.nio.file.Paths;

// TODO: Split up - right now it does the job of both the V and VM layer
@Slf4j
public class CanvasView extends JComponent {
    @Setter @Getter
	private boolean antiAliasing = false;
	@Getter
    private AffineTransform transform = new AffineTransform();
    private double fps = 0.0;
    private Rectangle2D viewRect;
    private Graphics2D g;
    private Rect viewArea;
    @Setter
	private double zoomLevel;
	private boolean isShapeOpen;
	private boolean showReversedBorders = false;
	private boolean showFPS = false;
	private Point2D pointerPosition;
	private BufferedImage pointer;

    //Performance test fields
	public double timeDraw;
	public double timeRangeSearch;
	@Setter @Getter
	private boolean logRangeSearchSize = false;
	@Setter @Getter
	private boolean logZoomLevel = false;
	@Setter @Getter
	private boolean logPerformanceTimeDrawVSRangeSearch = false;
	@Setter @Getter
	private boolean logNearestNeighbor = false;

	private DomainFacade domain;

	//ShortestPath
	private boolean showNetwork;
	private boolean showRandomSP;
	private Vector<Shape> randomSP;

	public CanvasView(DomainFacade domainFacade) {
		try {
			this.domain = domainFacade;
			this.viewArea = viewPortCoords(new Point2D.Double(0,0), new Point2D.Double(1100, 650));
			this.pointer = domain.getImageFromFS(Paths.get(CanvasView.class.getResource("/graphics/pointer.png").toURI())).get();
      		OsmElementProperty.standardColor();
		} catch (URISyntaxException e) {
			log.error("An error occurred", e);
		}
		repaint();
	}

    @Override
    public void paint(Graphics _g) {
		this.g = (Graphics2D) _g;

		Stopwatch stopwatchDraw = new Stopwatch();
		long t1 = System.nanoTime();
		this.viewArea = viewPortCoords(
				new Point2D.Double(this.getX(), this.getY()),
				new Point2D.Double(this.getX() + this.getWidth(), this.getY() + this.getHeight())
		);

		this.viewRect = new Rectangle2D.Double(0, 0, getWidth(), getHeight());

		//Paint background
        this.g.setPaint(new Color(179, 227, 245));
        this.g.fill(this.viewRect);

		transformViewRect();
        drawCoastlines();
        drawAllElements();
        drawPoi();

		if (this.showNetwork) drawNetwork();
		if (this.showRandomSP) drawShortestPath(randomSP);
		if (pointerPosition != null) drawPoi();

		performanceTest();

		//TODO MOVE FPS COUNTER TO FXML
		if (showFPS) {
			long t2 = System.nanoTime();
			fps = (fps + 1e9 / (t2 - t1)) / 2;
			g.setTransform(new AffineTransform());
			g.setColor(Color.WHITE);
			g.fillRect(getWidth() - 85, 5, 80, 20);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			g.setColor(Color.BLACK);
			g.drawRect(getWidth() - 85, 5, 80, 20);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawString(String.format("FPS: %.1f", fps), getWidth() - 75, 20);
		}
		timeDraw = stopwatchDraw.elapsedTime();
    }

    private Shape pointShape(Point2D point, Color color) {
		this.g.setPaint(color);
		double size = 0.005;
		return new Ellipse2D.Double(point.getX() - (size/2), point.getY() - (size/2), size,size);
	}

	private void drawShortestPath(Vector<Shape> shape) {
		if (shape.isEmpty()) return;
		Path2D path = new GeneralPath();
		boolean first = true;
		for(Shape s : shape) {
			Line2D.Double line = (Line2D.Double) s;
			if(first) {
				this.g.fill(pointShape(new Point2D.Double(line.x1,line.y1), Color.MAGENTA));
				path.moveTo(line.x1, line.y1);
				first = false;
			}
			else path.lineTo(line.x1,line.y1);
		}
		this.g.fill(pointShape(path.getCurrentPoint(),Color.decode("#ff5733")));
		this.g.setPaint(Color.decode("#2F9862"));
		this.g.setStroke(getMediumLevelStroke());
		if (this.zoomLevel < 5)this.g.setStroke(new BasicStroke(Float.MIN_VALUE));
		this.g.draw(path);
	}

	private void transformViewRect() {
		g.transform(this.transform);
		try {
			this.viewRect = this.transform.createInverse().createTransformedShape(this.viewRect).getBounds2D();
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
	}

	private void drawCoastlines() {
		this.domain.getCoastLines().forEach(c -> {
			this.g.setPaint(OsmElementProperty.deriveColorFromType(c.getType()));
			this.g.fill(c.getShape());
		});
	}

    private void drawAllElements() {
		Stopwatch stopwatchRangeSearch = new Stopwatch();
		Vector<Element> result = this.domain.doRangeSearch(viewArea, zoomLevel);
		timeRangeSearch = stopwatchRangeSearch.elapsedTime();

		paintClosedElements(result, new BasicStroke(Float.MIN_VALUE));

		//All open elements
		this.isShapeOpen = true;
		paintByType(result,Type.UNKNOWN,getLowLevelStroke());
		paintByType(result,Type.TRUNK, getLowLevelStroke());
		paintByType(result,Type.SAND, getLowLevelStroke());
		paintByType(result,Type.FOOTWAY, new BasicStroke(0.00002f));
		paintByType(result,Type.ROAD, new BasicStroke(0.00004f));
		paintByType(result,Type.FOOTWAY, getLowLevelStroke());
		paintByType(result,Type.TRACK, getLowLevelStroke());
		paintByType(result,Type.SERVICE, getLowLevelStroke());
		paintByType(result,Type.RACEWAY, getLowLevelStroke());
		paintByType(result,Type.CYCLEWAY, getLowLevelStroke());
		paintByType(result,Type.PATH, getLowLevelStroke());
		paintByType(result,Type.UNCLASSIFIED, getLowLevelStroke());
		paintByType(result,Type.TERTIARY, getHighLevelStroke());
		paintByType(result,Type.SECONDARY, getHighLevelStroke());
		paintByType(result,Type.PRIMARY, getMediumLevelStroke());
		paintByType(result,Type.HIGHWAY,new BasicStroke(0.0001f));
		paintByType(result,Type.MOTORWAY, getLowLevelStroke());

	}

	private void paintClosedElements (Vector<Element> result, BasicStroke stroke) {
		this.isShapeOpen = false;
		paintByType(result,Type.PARK,stroke);
		paintByType(result,Type.GRASSLAND,stroke);
		paintByType(result,Type.FOREST,stroke);
		paintByType(result,Type.GARDEN,stroke);
		paintByType(result,Type.HEATH,stroke);
		paintByType(result,Type.TREE_ROW,stroke);
		paintByType(result,Type.PLAYGROUND,stroke);
		paintByType(result,Type.WATER,stroke);
		paintByType(result,Type.BUILDING,stroke);
	}

	private BasicStroke getLowLevelStroke() {
		if (this.zoomLevel > 2) return getMediumLevelStroke();
		else if (this.zoomLevel > 1) return new BasicStroke(0.0025f);
		else if (this.zoomLevel > 0) return new BasicStroke(0.005f);
		else return new BasicStroke(Float.MIN_VALUE);
	}

	private BasicStroke getMediumLevelStroke() {
		if (this.zoomLevel > 5) return getHighLevelStroke();
		else if (this.zoomLevel > 2) return new BasicStroke(0.0007f);
		else if (this.zoomLevel > 0) return new BasicStroke(0.0025f);
		else return new BasicStroke(Float.MIN_VALUE);
	}

	private BasicStroke getHighLevelStroke() {
		if (this.zoomLevel > 130) return new BasicStroke(0.00005f);
		else if (this.zoomLevel > 18) return new BasicStroke(0.0001f);
		else if (this.zoomLevel > 5) return new BasicStroke(0.0003f);
		else return new BasicStroke(0.0007f);
	}

    private void paintByType(Vector<Element> elements, Type type, BasicStroke stroke) {
		elements.filter(e -> e.getType() == type)
				.forEach(e -> paintOsmElement(stroke, e));
	}

	private void paintOsmElement(BasicStroke stroke, Element element) {
		this.g.setStroke(stroke);
		this.g.setPaint(OsmElementProperty.deriveColorFromType(element.getType()));
		if (element.getShape().intersects(this.viewRect)) {
			if (isShapeOpen) {
				this.g.draw(element.getShape());
			} else {
				this.g.fill(element.getShape());
			}
		}
	}

	private void drawNetwork() {
		// Paint all edges
		this.domain.deriveAllDijkstraEdges().forEach(e -> {
			this.g.setPaint(Color.GREEN);
			if (e.getSpeedLimit() < 80) this.g.setPaint(Color.CYAN);
			if (e.getSpeedLimit() <= 20) this.g.setPaint(Color.RED);
			this.g.setStroke(new BasicStroke(0.00002f));
			this.g.draw(e.toShape());
		});
		// Paint all nodes
		this.domain.deriveAllDijkstraNodes().forEach(p -> {
			this.g.setPaint(Color.BLUE);
			this.g.fill(p.toShape());
		});
	}

	private void performanceTest() {
		if (logRangeSearchSize) log.info("Range search size: " + this.domain.doRangeSearch(viewArea, zoomLevel).size());
		if (logZoomLevel) log.info("ZoomLevel: " + zoomLevel);
		if (logPerformanceTimeDrawVSRangeSearch) log.info("TimeDraw: " + timeDraw + " --- TimeRangeSearch: " + timeRangeSearch + " --- Relative " + (timeDraw-timeRangeSearch)/timeDraw*100 );
    }

    public void logNearestNeighbor(Coordinate queryPoint) {
		if (logNearestNeighbor) log.info("Nearest Neighbor: " + this.domain.doNearestNeighborSearch(queryPoint, zoomLevel));
	}

    public void toggleAntiAliasing() {
        antiAliasing = !antiAliasing;
        repaint();
    }

    public void pan(double dx, double dy) {
        transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
        repaint();
    }

    public void zoom(double factor, double x, double y) {
        pan(x, y);
        transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
        pan(-x, -y);
        repaint();
		zoomLevel = zoomLevel*factor;
	}

	public void zoomToCenter(double factor) {
		zoom(factor, -getWidth() / 2.0, -getHeight() / 2.0);
	}

	public Rect viewPortCoords(Point2D p1, Point2D p2) {
		int borderW = 100;
		Point2D p1Altered = new Point2D.Double(p1.getX() + borderW, p1.getY() + borderW);
		Point2D p2Altered = new Point2D.Double(p2.getX() - borderW, p2.getY() - borderW);

		double x1 = (showReversedBorders) ? Coordinate.viewportPointToCanvasPoint(p1Altered, transform).getX() : Coordinate.viewportPointToCanvasPoint(p1, transform).getX() - 0.02;
		double y1 = (showReversedBorders) ? Coordinate.viewportPointToCanvasPoint(p1Altered, transform).getY() : Coordinate.viewportPointToCanvasPoint(p1, transform).getY() - 0.02;
		double x2 = (showReversedBorders) ? Coordinate.viewportPointToCanvasPoint(p2Altered, transform).getX() : Coordinate.viewportPointToCanvasPoint(p2, transform).getX() + 0.02;
		double y2 = (showReversedBorders) ? Coordinate.viewportPointToCanvasPoint(p2Altered, transform).getY() : Coordinate.viewportPointToCanvasPoint(p2, transform).getY() + 0.02;

		return new Rect(x1, y1, x2, y2);
	}

	/**
	 * Go to a coordinate at a given zoom level in canvas.
	 * @param coordinate		In WGS-84 format
	 * @param zoomLevel			Zoom level in canvas
	 */
	public void zoomToCoordinate(Coordinate coordinate, int zoomLevel) {
		centerCoordinateInCanvas(coordinate);
		zoomToZoomLevel(zoomLevel);
	}

	/**
	 * @param coordinate 		In WGS-84 format
	 */
	private void centerCoordinateInCanvas(Coordinate coordinate) {
		//Go to coordinate at map's original zoomlevel. (The coordinate will be in the upper right corner)
		transform = new AffineTransform();
		pan(-coordinate.getX(), -Coordinate.latToCanvasLat(coordinate.getY()));
		zoom(getWidth() / (Entities.getMaxLon() - Entities.getMinLon()), 0, 0);

		//Define upper left and lower right viewport corners screen in canvas lat/lon
		Point2D upperLeftViewPortCorner = Coordinate.viewportPointToCanvasPoint(new Point2D.Double(0, 0), transform);
		Point2D lowerRightViewPortCorner = Coordinate.viewportPointToCanvasPoint(new Point2D.Double(getWidth(), getHeight()), transform);

		//Calculate new upper right corner coordinates lambda and phi such that the coordinate will be centered
		double lambda = upperLeftViewPortCorner.getX()-((lowerRightViewPortCorner.getX()-upperLeftViewPortCorner.getX())/2);
		double phi = upperLeftViewPortCorner.getY()-((lowerRightViewPortCorner.getY()-upperLeftViewPortCorner.getY())/2);

		//Go to coordinate at map's original zoomlevel. (The coordinate will now be centered)
		transform = new AffineTransform();
		pan(-lambda, -phi);
		zoom(getWidth() / (Entities.getMaxLon() - Entities.getMinLon()), 0, 0);

		//Reset canvas zoom level because we have reset to the map's original zoomlevel
		this.zoomLevel = (1 / (Entities.getMaxLon() - Entities.getMinLon()));
	}

	//Zoom canvas to a given zoom level
	private void zoomToZoomLevel(int zoomLevel) {
		//Assumes zoomLevel has been reset.
		while(this.zoomLevel < zoomLevel) zoomToCenter(1.1);
		repaint();
	}

	/**
	 * Zoom as far in on canvas while still maintaining view of two coordinates.
	 * @param coordinateA		In WGS-84 format
	 * @param coordinateB		In WGS-84 format
	 */
	public void zoomToRoute(Coordinate coordinateA, Coordinate coordinateB) {
		//Transform coordinates into canvas coordinates
		coordinateA = new Coordinate(coordinateA.getX(), Coordinate.latToCanvasLat(coordinateA.getY()));
		coordinateB = new Coordinate(coordinateB.getX(), Coordinate.latToCanvasLat(coordinateB.getY()));

		Coordinate averageCoordinate = new Coordinate(
				(coordinateA.getX() + coordinateB.getX())/2,
				(coordinateA.getY() + coordinateB.getY())/2);

		centerCoordinateInCanvas(averageCoordinate);

		Point2D upperLeftViewPortCorner = Coordinate.viewportPointToCanvasPoint(new Point2D.Double(0,0), transform);
		Point2D lowerRightViewPortCorner = Coordinate.viewportPointToCanvasPoint(new Point2D.Double(getWidth(), getHeight()), transform);

		Rect currentViewRect = new Rect(
				upperLeftViewPortCorner.getX(),
				upperLeftViewPortCorner.getY(),
				lowerRightViewPortCorner.getX(),
				lowerRightViewPortCorner.getY()
		);

		Rect routeRect = new Rect(
				coordinateA.getX(),
				Coordinate.latToCanvasLat(coordinateA.getY()),
				coordinateB.getX(),
				Coordinate.latToCanvasLat(coordinateB.getY())
		);

		// Zoom continuously in on the route Rectangle. If the route rectange intersects view rectangle stop zooming.
		while (Rect.rectInRect(routeRect, currentViewRect)) {
			zoomToCenter(1.1);
			upperLeftViewPortCorner = Coordinate.viewportPointToCanvasPoint(new Point2D.Double(0,0), transform);
			lowerRightViewPortCorner = Coordinate.viewportPointToCanvasPoint(new Point2D.Double(getWidth(), getHeight()), transform);

			currentViewRect = new Rect(
					upperLeftViewPortCorner.getX(),
					upperLeftViewPortCorner.getY(),
					lowerRightViewPortCorner.getX(),
					lowerRightViewPortCorner.getY()
			);
		}
		zoomToCenter(1/1.3);
		repaint();
	}

	public void toggleFPS() {
		this.showFPS = !this.showFPS;
	}

	public void toggleReversedBorders() {
		this.showReversedBorders = !this.showReversedBorders;
	}

	private void drawPoi() {
		if (this.pointerPosition == null) return;
		double size = this.viewRect.getWidth() * 0.0001;
		double width = pointer.getWidth() * size;
		double height = pointer.getHeight() * size;

		AffineTransform at = new AffineTransform();
		at.translate(this.pointerPosition.getX() - width/2,this.pointerPosition.getY()-height);
		at.scale(size,size);

		this.g.drawImage(this.pointer,at,null);
	}

	public void setPointerPosition(Point2D p) {
		this.pointerPosition = Coordinate.viewportPointToCanvasPoint(p, transform);
	}

	public void toggleDijkstraNetwork() {
		this.showNetwork = !this.showNetwork;
	}

	public void toggleRandomShortestPath() {
		this.showRandomSP = !this.showRandomSP;

		if (this.showRandomSP) this.randomSP = domain.deriveRandomShortestPathShapes();
	}
}
