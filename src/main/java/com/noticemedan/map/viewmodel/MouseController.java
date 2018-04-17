package com.noticemedan.map.viewmodel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import static java.lang.Math.pow;

public class MouseController extends MouseAdapter {
    private CanvasView canvas;
    private Point2D lastMousePosition;

    public MouseController(CanvasView c) {
        canvas = c;
        canvas.addMouseListener(this);
        canvas.addMouseWheelListener(this);
        canvas.addMouseMotionListener(this);
        c.toggleAntiAliasing();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point2D currentMousePosition = e.getPoint();
        double dx = currentMousePosition.getX() - lastMousePosition.getX();
        double dy = currentMousePosition.getY() - lastMousePosition.getY();
        canvas.pan(dx, dy);
        lastMousePosition = currentMousePosition;
    }

    @Override
    public void mousePressed(MouseEvent e) {
		canvas.toggleAntiAliasing();
       	lastMousePosition = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
		canvas.toggleAntiAliasing();
	}

    public void mouseMoved(MouseEvent e) {
        Point2D modelCoords = canvas.toModelCoords(e.getPoint());
        /*System.out.println("Screen: [" + e.getX() + ", " + e.getY() + "], " +
            "Model: [" + modelCoords.getX() + ", " + modelCoords.getY() + "]");*/
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double factor = pow(1.1, -e.getWheelRotation());
        canvas.zoom(factor, -e.getX(), -e.getY());

    }
}
