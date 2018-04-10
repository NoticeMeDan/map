package com.noticemedan.map.view;

import com.noticemedan.map.data.OSMManager;
import com.noticemedan.map.model.osm.OSMType;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;

public class CanvasView extends JComponent implements Observer{

    private final OSMManager model;
    private boolean useAntiAliasing = false;
    private AffineTransform transform = new AffineTransform();
    private double fps = 0.0;

    public CanvasView(OSMManager m) {
        this.model = m;
        model.addObserver(this);
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
        g.setPaint(new Color(237, 237, 237));
        for (Shape coastline: model.get(OSMType.COASTLINE)) {
            g.fill(coastline);
        }
        g.setPaint(new Color(60, 149, 255));
        for (Shape water: model.get(OSMType.WATER)) {
            if (water.intersects(viewRect)) {
                g.fill(water);
            }
        }
        g.setPaint(Color.black);
        for (Shape line: model.get(OSMType.UNKNOWN)) {
            if (line.intersects(viewRect)) {
                g.draw(line);
            }
        }
        g.setStroke(new BasicStroke(0.00001f));
        g.setPaint(new Color(230, 139, 213));
        for (Shape road : model.get(OSMType.ROAD)) {
            if (road.intersects(viewRect)) {
                g.draw(road);
            }
        }
        g.setStroke(new BasicStroke(0.00005f));
        g.setPaint(new Color(255, 114, 109));
        for (Shape highway: model.get(OSMType.HIGHWAY)) {
            if (highway.intersects(viewRect)) {
                g.draw(highway);
            }
        }
        g.setPaint(new Color(172, 169, 151));
        for (Shape building: model.get(OSMType.BUILDING)) {
            if (building.intersects(viewRect)) {
                g.fill(building);
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
