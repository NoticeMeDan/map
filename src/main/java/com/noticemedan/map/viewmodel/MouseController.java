package com.noticemedan.map.viewmodel;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

public class MouseController {
    private static MouseController instance;
    private CoordinatePoint mousePressed, draggedDistance, oldLocation, newLocalation;
    private PaneView paneView = PaneView.getInstance();
    private Pane rootPane = paneView.getRootPane();

    private MouseController() {}

    public static MouseController getInstance() {
        if (instance == null) instance = new MouseController();
        return instance;
    }

    public void addPanFunctionality() {
        oldLocation = new CoordinatePoint(0,0);
        mousePressed = new CoordinatePoint(0,0);
        draggedDistance = new CoordinatePoint(0,0);
        newLocalation = new CoordinatePoint(0,0);

        setMousePressedEvent();
        setMouseDragEvent();
        setMouseReleasedEvent();
    }

    public void addZoomFunctionality() {
        // ZoomingFunctionality
    }


    private void setMousePressedEvent() {
        rootPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event ->  {
            mousePressed.setX(event.getSceneX());
            mousePressed.setY(event.getSceneY());
            draggedDistance.setX(event.getSceneX());
            draggedDistance.setY(event.getSceneY());
        });
    }

    private void setMouseDragEvent() {
        rootPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            draggedDistance.setX(event.getSceneX() - mousePressed.getX());
            draggedDistance.setY(event.getSceneY() - mousePressed.getY());
            newLocalation.setX(draggedDistance.getX() + oldLocation.getX());
            newLocalation.setY(draggedDistance.getY() + oldLocation.getY());
            rootPane.relocate(newLocalation.getX(), newLocalation.getY());
        });
    }

    private void setMouseReleasedEvent() {
        rootPane.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            oldLocation.setX(newLocalation.getX());
            oldLocation.setY(newLocalation.getY());
        });
    }

    private class CoordinatePoint {
        @Getter @Setter double x, y;
        CoordinatePoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
