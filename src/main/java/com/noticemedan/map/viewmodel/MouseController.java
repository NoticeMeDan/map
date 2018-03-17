package com.noticemedan.map.viewmodel;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import lombok.Getter;
import lombok.Setter;

public class MouseController {
    private static MouseController instance;
    private CoordinatePoint mousePressed, draggedDistance, oldLocation, newLocation;
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
        newLocation = new CoordinatePoint(0,0);

        setMousePressedEvent();
        setMouseDragEvent();
        setMouseReleasedEvent();
    }

    public void addZoomFunctionality() {
        rootPane.addEventFilter(ScrollEvent.SCROLL, event -> {
                CoordinatePoint mousePosition = new CoordinatePoint(event.getSceneX(),event.getSceneY());
                if(event.getDeltaY()>0){
                    zoomIn(mousePosition);
                } else zoomOut(mousePosition);
            }
        );
    }

    private void zoomIn(CoordinatePoint mousePosition) {
        Scale scaleUp = new Scale(1.1,1.1);
        scaleUp.setPivotX(mousePosition.getX());
        scaleUp.setPivotY(mousePosition.getY());
        rootPane.getTransforms().addAll(scaleUp);
    }

    private void zoomOut(CoordinatePoint mousePosition) {
        Scale scaleDown = new Scale(0.9,0.9);
        scaleDown.setPivotX(mousePosition.getX());
        scaleDown.setPivotY(mousePosition.getY());
        rootPane.getTransforms().addAll(scaleDown);
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
            newLocation.setX(draggedDistance.getX() + oldLocation.getX());
            newLocation.setY(draggedDistance.getY() + oldLocation.getY());
            rootPane.relocate(newLocation.getX(), newLocation.getY());
        });
    }

    private void setMouseReleasedEvent() {
        rootPane.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            oldLocation.setX(newLocation.getX());
            oldLocation.setY(newLocation.getY());
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
