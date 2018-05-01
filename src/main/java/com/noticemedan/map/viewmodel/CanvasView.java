package com.noticemedan.map.viewmodel;

import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.kdtree.Forest;
import com.noticemedan.map.model.osm.OSMType;
import com.noticemedan.map.model.pathfinding.PathEdge;
import com.noticemedan.map.model.utilities.Rect;
import com.noticemedan.map.model.utilities.Stopwatch;
import io.vavr.control.Try;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.List;

@Slf4j
public class CanvasView extends JComponent {
    @Setter @Getter
	private boolean antiAliasing = false;
    private AffineTransform transform = new AffineTransform();
	private Forest forest;
    private double fps = 0.0;
    private Rectangle2D viewRect;
    private Graphics2D g;
    private Rect viewArea;
    @Setter
	private double zoomLevel;
	private boolean isShapeOpen;
	private Shape poi;
	private Point2D poiPos;
	private boolean showReversedBorders = false;
	private boolean showFPS = false;

    //Performance test fields
	//TODO @emil delete when finished performance tuning
	public double timeDraw;
	public double timeRangeSearch;
	@Setter @Getter
	private boolean logRangeSearchSize = false;
	@Setter @Getter
	private boolean logZoomLevel = false;
	@Setter @Getter
	private boolean logPerformanceTimeDrawVSRangeSearch = false;

	public CanvasView() {
		this.forest = new Forest();
		this.viewArea = viewPortCoords(new Point2D.Double(0,0), new Point2D.Double(1100, 650));
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

	private void transformViewRect() {
		g.transform(this.transform);
		try {
			this.viewRect = this.transform.createInverse().createTransformedShape(this.viewRect).getBounds2D();
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
	}

	private void drawCoastlines() {
		this.forest.getCoastlines().forEach(c -> {
			this.g.setPaint(c.getColor());
			this.g.fill(c.getShape());
		});
	}

    private void drawAllElements() {
		Stopwatch stopwatchRangeSearch = new Stopwatch();
		List<OsmElement> result = forest.rangeSearch(viewArea, zoomLevel);
		timeRangeSearch = stopwatchRangeSearch.elapsedTime();

		paintClosedElements(result, new BasicStroke(Float.MIN_VALUE));

		//All open elements
		this.isShapeOpen = true;
		paintByType(result,OSMType.UNKNOWN,getLowLevelStroke());
		paintByType(result,OSMType.TRUNK, getLowLevelStroke());
		paintByType(result,OSMType.SAND, getLowLevelStroke());
		paintByType(result,OSMType.FOOTWAY, new BasicStroke(0.00002f));
		paintByType(result,OSMType.ROAD, new BasicStroke(0.00004f));
		paintByType(result,OSMType.TERTIARY, getHighLevelStroke());
		paintByType(result,OSMType.SECONDARY, getHighLevelStroke());
		paintByType(result,OSMType.PRIMARY, getMediumLevelStroke());
		paintByType(result,OSMType.HIGHWAY,new BasicStroke(0.0001f));
		paintByType(result,OSMType.MOTORWAY, getLowLevelStroke());
	}

	private void paintClosedElements (List<OsmElement> result, BasicStroke stroke) {
		this.isShapeOpen = false;
		paintByType(result,OSMType.PARK,stroke);
		paintByType(result,OSMType.GRASSLAND,stroke);
		paintByType(result,OSMType.FOREST,stroke);
		paintByType(result,OSMType.GARDEN,stroke);
		paintByType(result,OSMType.HEATH,stroke);
		paintByType(result,OSMType.TREE_ROW,stroke);
		paintByType(result,OSMType.PLAYGROUND,stroke);
		paintByType(result,OSMType.WATER,stroke);
		paintByType(result,OSMType.BUILDING,stroke);
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

    private void paintByType(List<OsmElement> elements, OSMType type, BasicStroke stroke) {
		elements.stream()
				.filter(e -> e.getOsmType() == type)
				.forEach(e -> paintOsmElement(stroke, e));
	}

	private void paintOsmElement(BasicStroke stroke, OsmElement osmElement) {
		this.g.setStroke(stroke);
		this.g.setPaint(osmElement.getColor());
		if (osmElement.getShape().intersects(this.viewRect)) {
			if (isShapeOpen) {
				this.g.draw(osmElement.getShape());
			} else {
				this.g.fill(osmElement.getShape());
			}
		}
	}

	private void performanceTest() {
		if (logRangeSearchSize) log.info("Range search size: " + forest.rangeSearch(viewArea).size());
		if (logZoomLevel) log.info("ZoomLevel: " + zoomLevel);
		if (logPerformanceTimeDrawVSRangeSearch) log.info("TimeDraw: " + timeDraw + " --- TimeRangeSearch: " + timeRangeSearch + " --- Relative " + (timeDraw-timeRangeSearch)/timeDraw*100 );
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

	// TODO @Simon fix border relative to screen and not lat lon
	public Rect viewPortCoords(Point2D p1, Point2D p2) {
		int borderFactor = (showReversedBorders) ? -1 : 1;
		double x1 = toModelCoords(p1).getX() - 0.02 * borderFactor;
		double y1 = toModelCoords(p1).getY() - 0.02 * borderFactor;
		double x2 = toModelCoords(p2).getX() + 0.02 * borderFactor;
		double y2 = toModelCoords(p2).getY() + 0.02 * borderFactor;

		return new Rect(x1, y1, x2, y2);
	}

	public void toggleFPS() {
		this.showFPS = !this.showFPS;
	}

	public void toggleReversedBorders() {
		this.showReversedBorders = !this.showReversedBorders;
	}

	public void drawShortestPath (io.vavr.collection.List<PathEdge> sp) {
		sp.forEach(e -> this.g.draw(e.toShape()));
	}

	private void drawPoi() {
		if (poiPos == null) return;
		this.poi = createPoiShape();
		this.g.setStroke(new BasicStroke(Float.MIN_VALUE));
		//Background color
		this.g.setPaint(Color.decode("#D0021B"));
		this.g.fill(this.poi);
		//Outline color
		this.g.setPaint(Color.WHITE);
		this.g.draw(this.poi);
	}

	private Shape createPoiShape() {
		double size = this.viewRect.getWidth() * 0.05;
		double xPos = this.poiPos.getX() - size/2;
		double yPos = this.poiPos.getY() - size * 1.2;

		Shape oval = new Ellipse2D.Double(xPos, yPos, size, size);
		Shape inner = new Ellipse2D.Double(xPos + size/2.7, yPos + size/2.7, size/4, size/4);

		Path2D pointer = new Path2D.Double();
		pointer.moveTo(xPos, yPos + size/1.5);
		pointer.lineTo(poiPos.getX(), poiPos.getY());
		pointer.lineTo(xPos + size, yPos + size/1.5);
		pointer.closePath();

		Area area = new Area(oval);
		area.add(new Area(pointer));
		area.subtract(new Area(inner));
		return area;
	}

	public void setPoiPos(Point2D p) {
		this.poiPos = toModelCoords(p);
	}

	public Point2D toModelCoords(Point2D p) {
		return Try.of( () -> transform.inverseTransform(p, null))
				.getOrNull();
	}
}
