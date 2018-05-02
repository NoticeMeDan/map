package com.noticemedan.map.viewmodel;

import com.noticemedan.map.model.utilities.Coordinate;
import com.noticemedan.map.view.MainViewController;
import lombok.Getter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import static java.lang.Math.pow;

public class MouseController extends MouseAdapter {
    private CanvasView canvas;
    private MainViewController mainViewController;
    @Getter private Point2D lastMousePosition;
    @Getter private Point2D lastMousePositionCanvasCoords;


    public MouseController(CanvasView canvas, MainViewController mainViewController) {
        this.canvas = canvas;
        this.mainViewController = mainViewController;
        canvas.addMouseListener(this);
        canvas.addMouseWheelListener(this);
        canvas.addMouseMotionListener(this);
        canvas.toggleAntiAliasing();
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
		lastMousePositionCanvasCoords = Coordinate.viewportPoint2canvasPoint(lastMousePosition, canvas.getTransform());
    }

    @Override
	public void mouseMoved(MouseEvent e) {
    	Point2D currentHoverPoint = Coordinate.viewportPoint2canvasPoint(e.getPoint(), canvas.getTransform());
		Coordinate currentHoverCoordinate = new Coordinate(currentHoverPoint.getX(), currentHoverPoint.getY());
    	canvas.logNearestNeighbor(currentHoverCoordinate);
		mainViewController.updateScalaBar();
	}

    @Override
    public void mouseReleased(MouseEvent e) {
		canvas.toggleAntiAliasing();
	}

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double factor = pow(1.1, -e.getWheelRotation());
        canvas.zoom(factor, -e.getX(), -e.getY());
	}
}
