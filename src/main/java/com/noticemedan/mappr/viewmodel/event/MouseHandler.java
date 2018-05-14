package com.noticemedan.mappr.viewmodel.event;

import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.view.CanvasView;
import com.noticemedan.mappr.viewmodel.MainViewController;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import static java.lang.Math.pow;

@Slf4j
public class MouseHandler extends MouseAdapter {
    private CanvasView canvas;
    @Getter private Point2D lastMousePosition;
    @Getter private Point2D lastMousePositionCanvasCoords;
    private MainViewController mainViewController;


    public MouseHandler(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    	canvas = mainViewController.getCanvas();
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
		lastMousePositionCanvasCoords = Coordinate.viewportPointToCanvasPoint(lastMousePosition, canvas.getTransform());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
		canvas.toggleAntiAliasing();
	}

	@Override
    public void mouseMoved(MouseEvent e) {
		Point2D currentHoverPoint = Coordinate.viewportPointToCanvasPoint(e.getPoint(), canvas.getTransform());
		Coordinate currentHoverCoordinate = new Coordinate(currentHoverPoint.getX(), currentHoverPoint.getY());
		canvas.logNearestNeighbor(currentHoverCoordinate);
		mainViewController.updateScalaBar();
		mainViewController.updateCurrentHoveredRoad(currentHoverCoordinate);
		//log.info("" + Coordinate.viewportPointToCanvasPoint(e.getPoint(), canvas.getTransform()));
	}

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double factor = pow(1.1, -e.getWheelRotation());
        canvas.zoom(factor, -e.getX(), -e.getY());
    }

    @Override
	public void mouseClicked(MouseEvent e) {
    	Point2D mousePosition = (e.getButton() == 1) ? e.getPoint() : null;
		this.canvas.setPointerPosition(mousePosition);
		this.canvas.repaint();
	}
}
