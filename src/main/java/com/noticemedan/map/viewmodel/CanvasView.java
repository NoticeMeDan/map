package com.noticemedan.map.viewmodel;

import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.kdtree.Forest;
import com.noticemedan.map.model.osm.OsmType;
import com.noticemedan.map.model.utilities.Icon;
import com.noticemedan.map.model.utilities.Rect;
import com.noticemedan.map.model.utilities.Stopwatch;
import io.vavr.control.Try;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
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
		paintByType(result,OsmType.UNKNOWN,getLowLevelStroke());
		paintByType(result,OsmType.TRUNK, getLowLevelStroke());
		paintByType(result,OsmType.SAND, getLowLevelStroke());
		paintByType(result,OsmType.FOOTWAY, new BasicStroke(0.00002f));
		paintByType(result,OsmType.ROAD, new BasicStroke(0.00004f));
		paintByType(result,OsmType.TERTIARY, getHighLevelStroke());
		paintByType(result,OsmType.SECONDARY, getHighLevelStroke());
		paintByType(result,OsmType.PRIMARY, getMediumLevelStroke());
		paintByType(result,OsmType.HIGHWAY,new BasicStroke(0.0001f));
		paintByType(result,OsmType.MOTORWAY, getLowLevelStroke());
	}

	private void paintClosedElements (List<OsmElement> result, BasicStroke stroke) {
		this.isShapeOpen = false;
		paintByType(result,OsmType.PARK,stroke);
		paintByType(result,OsmType.GRASSLAND,stroke);
		paintByType(result,OsmType.FOREST,stroke);
		paintByType(result,OsmType.GARDEN,stroke);
		paintByType(result,OsmType.HEATH,stroke);
		paintByType(result,OsmType.TREE_ROW,stroke);
		paintByType(result,OsmType.PLAYGROUND,stroke);
		paintByType(result,OsmType.WATER,stroke);
		paintByType(result,OsmType.BUILDING,stroke);
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

    private void paintByType(List<OsmElement> elements, OsmType type, BasicStroke stroke) {
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

	private void drawPoi() {
		if (poiPos == null) return;
		BufferedImage pointer = new Icon().getPointer();
		AffineTransform at = new AffineTransform();
		double size = this.viewRect.getWidth() * 0.0001;
		double width = pointer.getWidth() * size;
		double height = pointer.getHeight() * size;
		at.translate(poiPos.getX() - width/2,poiPos.getY()-height);
		at.scale(size,size);
		this.g.drawImage(pointer,at,null);
	}

	public void setPoiPos(Point2D p) {
		this.poiPos = toModelCoords(p);
	}

	public Point2D toModelCoords(Point2D p) {
		return Try.of( () -> transform.inverseTransform(p, null))
				.getOrNull();
	}
}
