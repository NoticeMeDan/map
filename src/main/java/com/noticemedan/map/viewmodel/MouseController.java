package com.noticemedan.map.viewmodel;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

public class MouseController {
    private PaneView paneView = PaneView.getInstance();
    Pane rootPane = paneView.getRootPane();

    MousePoint mousePressed, mouseDragged, mouseRelease;
    MousePoint newLocalationCoordinates;

    public void addMouseListeners() {
        newLocalationCoordinates = new MousePoint(0,0);
        mouseRelease = new MousePoint(0,0);
        rootPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event ->  {
            mousePressed = new MousePoint(event.getSceneX(), event.getSceneY());
        });

        rootPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (mouseDragged == null) mouseDragged = new MousePoint(event.getSceneX(), event.getSceneY());
            else {
                mouseDragged.setX(event.getSceneX() - mousePressed.getX());
                mouseDragged.setY(event.getSceneY() - mousePressed.getY());
            }
            double newPaneLocationX = mouseDragged.getX() + mouseRelease.getX();
            double newPaneLocationY = mouseDragged.getY() + mouseRelease.getY();

            newLocalationCoordinates.setX(newPaneLocationX);
            newLocalationCoordinates.setY(newPaneLocationY);

            rootPane.relocate(newPaneLocationX, newPaneLocationY);
        });

        rootPane.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            mouseRelease.setX(newLocalationCoordinates.getX());
            mouseRelease.setY(newLocalationCoordinates.getY());
        });
    }

    private class MousePoint {
        @Getter @Setter double x, y;
        MousePoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
