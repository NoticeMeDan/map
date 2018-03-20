package com.noticemedan.map.viewmodel;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;

import java.awt.*;
import java.awt.geom.Point2D;

public class MouseController {
    private static MouseController instance;
    private static final double factor = 1.1;
    private Point2D.Double mousePressed, draggedDistance, oldLocation, newLocation;
    private PaneView paneView = PaneView.getInstance();
    private Pane rootPane = paneView.getRootPane();

    private MouseController() {}

    public static MouseController getInstance() {
        if (instance == null) instance = new MouseController();
        return instance;
    }

    public void addPanFunctionality() {
        oldLocation = new Point2D.Double(0,0);
        mousePressed = new Point2D.Double(0,0);
        draggedDistance = new Point2D.Double(0,0);
        newLocation = new Point2D.Double(0,0);

        setMousePressedEvent();
        setMouseDragEvent();
        setMouseReleasedEvent();
    }

    public void addZoomFunctionality() {
        rootPane.addEventFilter(ScrollEvent.SCROLL, event -> {
                Point2D.Double mousePosition = new Point2D.Double(event.getSceneX(), event.getSceneY());
                zoom(event.getDeltaY(), mousePosition);
            }
        );
    }

    private void zoom(double scrollDirection, Point2D.Double mousePosition) {
        Scale scale = (scrollDirection > 0) ? new Scale(factor, factor) : new Scale(1 / factor, 1 / factor);
        scale.setPivotX(mousePosition.getX());
        scale.setPivotY(mousePosition.getY());
        rootPane.getTransforms().addAll(scale);
    }

    private void setMousePressedEvent() {
        rootPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event ->  {
            mousePressed.setLocation(event.getSceneX(), event.getSceneY());
            draggedDistance.setLocation(event.getSceneX(), event.getSceneY());
        });
    }

    private void setMouseDragEvent() {
        rootPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            draggedDistance.setLocation(event.getSceneX() - mousePressed.getX(), event.getSceneY() - mousePressed.getY());
            newLocation.setLocation(draggedDistance.getX() + oldLocation.getX(), draggedDistance.getY() + oldLocation.getY());
            rootPane.relocate(newLocation.getX(), newLocation.getY());
        });
    }

    private void setMouseReleasedEvent() {
        rootPane.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            oldLocation.setLocation(newLocation.getX(), newLocation.getY());
        });
    }

}
