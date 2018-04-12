package com.noticemedan.map.viewmodel;

import com.noticemedan.map.model.OSMMaterialElement;
import com.noticemedan.map.model.kdtree.Forest;
import com.noticemedan.map.model.kdtree.ForestCreator;
import com.noticemedan.map.model.osm.OSMType;
import com.noticemedan.map.model.utilities.Rect;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

public class CanvasView extends JComponent implements Observer{
    private boolean useAntiAliasing = false;
    private AffineTransform transform = new AffineTransform();
    private double fps = 0.0;

    private Forest forest;
    private Rect viewArea;

    public CanvasView() {
		ForestCreator forestCreator = new ForestCreator();
		this.forest = forestCreator.getForest();
		forestCreator.addObserver(this);
		//Half of bornholm (y-axis)
		//this.viewArea = new Rect(-100,-55.1, 100, -54);

		//Whole of bornholm
		this.viewArea = new Rect(-100,-100, 100, 100);
	}

	private EnumMap<OSMType, List<OSMMaterialElement>> initializeMap() {
		EnumMap<OSMType, List<OSMMaterialElement>> map = new EnumMap<>(OSMType.class);
		for (OSMType type: OSMType.values()) {
			map.put(type, new ArrayList<>());
		}
		return map;
	}

    @Override
    public void paint(Graphics _g) {
        long t1 = System.nanoTime();
        Graphics2D g = (Graphics2D) _g;
        g.setStroke(new BasicStroke(Float.MIN_VALUE));
        Rectangle2D viewRect = new Rectangle2D.Double(0, 0, getWidth(), getHeight());

        g.setPaint(new Color(60, 149, 255));
        g.fill(viewRect);
        g.transform(transform);
        try {
            viewRect = transform.createInverse().createTransformedShape(viewRect).getBounds2D();
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }

        if (useAntiAliasing) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

        for (OSMMaterialElement element : forest.rangeSearch(new Rect(-100,-100, 100,100))) {
			g.setPaint(element.getColor());
        	if(element.getOsmType() == OSMType.COASTLINE) {
				g.fill(element.getShape());
			}
        }

		System.out.println("Range size: " + forest.rangeSearch(viewArea).size());

		EnumMap<OSMType, List<OSMMaterialElement>> osmElements = initializeMap();

		forest.rangeSearch(viewArea).forEach(e -> osmElements.get(e.getOsmType()).add(e));

		for (OSMMaterialElement element : osmElements.get(OSMType.UNKNOWN)) {
			g.setPaint(element.getColor());
			if (element.getShape().intersects(viewRect)) {
				g.draw(element.getShape());
			}
		}
		for (OSMMaterialElement element : osmElements.get(OSMType.WATER)) {
			g.setPaint(element.getColor());
			if (element.getShape().intersects(viewRect)) {
				g.fill(element.getShape());
			}
		}
		for (OSMMaterialElement element : osmElements.get(OSMType.GRASSLAND)) {
			g.setPaint(element.getColor());
			if (element.getShape().intersects(viewRect)) {
				g.fill(element.getShape());
			}
		}
		for (OSMMaterialElement element : osmElements.get(OSMType.TREE_ROW)) {
			g.setPaint(element.getColor());
			if (element.getShape().intersects(viewRect)) {
				g.fill(element.getShape());
			}
		}
		for (OSMMaterialElement element : osmElements.get(OSMType.HEATH)) {
			g.setPaint(element.getColor());
			if (element.getShape().intersects(viewRect)) {
				g.fill(element.getShape());
			}
		}
		g.setStroke(new BasicStroke(0.00001f));
		for (OSMMaterialElement element : osmElements.get(OSMType.ROAD)) {
			g.setPaint(element.getColor());
			if (element.getShape().intersects(viewRect)) {
				g.draw(element.getShape());
			}
		}
		g.setStroke(new BasicStroke(0.0001f));
		for (OSMMaterialElement element : osmElements.get(OSMType.HIGHWAY)) {
			g.setPaint(element.getColor());
			if (element.getShape().intersects(viewRect)) {
				g.draw(element.getShape());
			}
		}
		for (OSMMaterialElement element : osmElements.get(OSMType.BUILDING)) {
			g.setPaint(element.getColor());
			if (element.getShape().intersects(viewRect)) {
				g.fill(element.getShape());
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
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }

    public void toggleAntiAliasing() {
        useAntiAliasing = !useAntiAliasing;
        repaint();
    }

    public void pan(double dx, double dy) {
        transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
        repaint();
    }

    public void zoomToCenter(double factor) {
        zoom(factor, -getWidth() / 2.0, -getHeight() / 2.0);
    }

    public void zoom(double factor, double x, double y) {
        pan(x, y);
        transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
        pan(-x, -y);
        repaint();
    }

    public Point2D toModelCoords(Point2D p) {
        try {
            return transform.inverseTransform(p, null);
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        return null;
    }
}
