package com.noticemedan.map.viewmodel;

import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.kdtree.Forest;
import com.noticemedan.map.model.osm.OsmType;
import com.noticemedan.map.model.utilities.Rect;
import com.noticemedan.map.model.utilities.Stopwatch;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

@Slf4j
public class CanvasView extends JComponent {
    @Setter @Getter
	private boolean antiAliasing = false;
    private AffineTransform transform = new AffineTransform();
	Forest forest;
    private double fps = 0.0;
    private Rectangle2D viewRect;
    private Graphics2D g;
    private Rect viewArea;
    @Setter
	private double zoomLevel;

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
		Stopwatch stopwatchDraw = new Stopwatch();
    	long t1 = System.nanoTime();
		this.viewArea = viewPortCoords(new Point2D.Double(getX(), getY()), new Point2D.Double(getX() + getWidth(), getY() + getHeight()));
        this.g = (Graphics2D) _g;
		this.viewRect = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
		BasicStroke stroke = new BasicStroke(Float.MIN_VALUE);

        g.setPaint(new Color(179, 227, 245));
        g.fill(this.viewRect);

        g.transform(this.transform);
        try {
            this.viewRect = this.transform.createInverse().createTransformedShape(viewRect).getBounds2D();
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }

		/*if (antiAliasing) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }*/

		for (OsmElement element : forest.getCoastlines()) {
			g.setPaint(element.getColor());
        	if(element.getOsmType() == OsmType.COASTLINE) {
				g.fill(element.getShape());
			}
        }

		Stopwatch stopwatchRangeSearch = new Stopwatch();
		List<OsmElement> result = forest.rangeSearch(viewArea, zoomLevel);
		timeRangeSearch = stopwatchRangeSearch.elapsedTime();

		for ( OsmElement osmElement : result) {
			switch (osmElement.getOsmType()) {
				case HIGHWAY:
					this.paintOsmElement(new BasicStroke(0.0001f), osmElement, "draw");
					break;
				case UNKNOWN:
				case TRUNK:
				case SAND:
				case MOTORWAY:
					if (zoomLevel > 0) stroke = new BasicStroke(0.005f);
					if (zoomLevel > 1) stroke = new BasicStroke(0.0025f);
					if (zoomLevel > 2) stroke = new BasicStroke(0.0007f);
					if (zoomLevel > 5) stroke = new BasicStroke(0.0003f);
					if (zoomLevel > 18) stroke = new BasicStroke(0.0001f);
					if (zoomLevel > 130) stroke = new BasicStroke(0.00005f);
					this.paintOsmElement(stroke, osmElement, "draw");
					break;
				case PRIMARY:
					if (zoomLevel > 0) stroke = new BasicStroke(0.0025f);
					if (zoomLevel > 2) stroke = new BasicStroke(0.0007f);
					if (zoomLevel > 5) stroke = new BasicStroke(0.0003f);
					if (zoomLevel > 18) stroke = new BasicStroke(0.0001f);
					if (zoomLevel > 130) stroke = new BasicStroke(0.00005f);
					this.paintOsmElement(stroke, osmElement, "draw");
					break;
				case SECONDARY:
					stroke = new BasicStroke(0.0007f);
					if (zoomLevel > 5) stroke = new BasicStroke(0.0003f);
					if (zoomLevel > 18) stroke = new BasicStroke(0.0001f);
					if (zoomLevel > 130) stroke = new BasicStroke(0.00005f);
					this.paintOsmElement(stroke, osmElement, "draw");
					break;
				case TERTIARY:
					stroke = new BasicStroke(0.0007f);
					if (zoomLevel > 5) stroke = new BasicStroke(0.0003f);
					if (zoomLevel > 18) stroke = new BasicStroke(0.0001f);
					if (zoomLevel > 130) stroke = new BasicStroke(0.00005f);
					this.paintOsmElement(stroke, osmElement, "draw");
					break;
				case ROAD:
					this.paintOsmElement(new BasicStroke(0.00004f), osmElement, "draw");
					break;
				case FOOTWAY:
					this.paintOsmElement(new BasicStroke(0.00002f), osmElement, "draw");
					break;
				case PLAYGROUND:
				case PARK:
				case FOREST:
				case GARDEN:
				case GRASSLAND:
				case BUILDING:
				case HEATH:
				case TREE_ROW:
				case WATER:
					this.paintOsmElement(new BasicStroke(Float.MIN_VALUE), osmElement, "fill");
					break;
				default:
					break;
			}
		}

		long t2 = System.nanoTime();
        fps = (fps + 1e9/ (t2 - t1)) / 2;
        g.setTransform(new AffineTransform());
        g.setColor(Color.WHITE);
        g.fillRect(getWidth() - 85, 5, 80, 20);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setColor(Color.BLACK);
        g.drawRect(getWidth() - 85, 5, 80, 20);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawString(String.format("FPS: %.1f", fps), getWidth() - 75, 20);
        timeDraw = stopwatchDraw.elapsedTime();

		performanceTest();
    }

	private void performanceTest() {
		if (logRangeSearchSize) log.info("Range search size: " + forest.rangeSearch(viewArea).size());
		if (logZoomLevel) log.info("ZoomLevel: " + zoomLevel);
		if (logPerformanceTimeDrawVSRangeSearch) log.info("TimeDraw: " + timeDraw + " --- TimeRangeSearch: " + timeRangeSearch + " --- Relative " + (timeDraw-timeRangeSearch)/timeDraw*100 );
    }

	private void paintOsmElement(BasicStroke stroke, OsmElement osmElement, String drawMethod) {
		this.g.setStroke(stroke);
    	this.g.setPaint(osmElement.getColor());
		if (osmElement.getShape().intersects(this.viewRect)) {
			if (drawMethod.equals("draw")) {
				this.g.draw(osmElement.getShape());
			} else {
				this.g.fill(osmElement.getShape());
			}
		}
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

    public Point2D toModelCoords(Point2D p) {
        try {
			return transform.inverseTransform(p, null);
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        return null;
    }

	public Rect viewPortCoords(Point2D p1, Point2D p2) {
		double x1 = toModelCoords(p1).getX() - 0.02;
		double y1 = toModelCoords(p1).getY() - 0.02;
		double x2 = toModelCoords(p2).getX() + 0.02;
		double y2 = toModelCoords(p2).getY() + 0.02;

		return new Rect(x1, y1, x2, y2);
	}
}
