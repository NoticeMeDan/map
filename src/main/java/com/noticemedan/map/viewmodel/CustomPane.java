package com.noticemedan.map.viewmodel;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import lombok.Getter;

public class CustomPane extends ScrollPane {
    private Group paneViewContent;
    @Getter private Group canvasContent;
    private MapCanvas mapCanvas;

    public CustomPane() {
        super();
        initializeFields();
        applyContent();
        adjustPaneProperties();
    }

    private void initializeFields(){
        this.mapCanvas = new MapCanvas();
        this.paneViewContent = new Group();
        this.canvasContent = new Group();
    }

    private void applyContent() {
        paneViewContent.getChildren().add(canvasContent);
        canvasContent.getChildren().add(mapCanvas.getCanvas());
        this.setContent(paneViewContent);
    }

    private void adjustPaneProperties(){
        this.setPrefHeight(560);
        this.setPrefWidth(700);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.NEVER);
        this.setPannable(true);
    }

    public void repositionScroller(double scaleFactor, Point2D scrollOffset) {
        adjustHorizontalPosition(scaleFactor,scrollOffset.getX());
        adjustVerticalPosition(scaleFactor,scrollOffset.getY());
    }

    private void adjustHorizontalPosition(double scaleFactor, double offSet){
        double deltaWidth = this.paneViewContent.getLayoutBounds().getWidth() - this.getViewportBounds().getWidth();
        if (deltaWidth > 0) {
            double horizontalCenter = this.getViewportBounds().getWidth() / 2 ;
            double newScrollXOffset = (scaleFactor - 1) *  horizontalCenter + scaleFactor * offSet;
            this.setHvalue(this.getHmin() + newScrollXOffset * (this.getHmax() - this.getHmin()) / deltaWidth);
        } else {
            this.setHvalue(this.getHmin());
        }
    }

    private void adjustVerticalPosition(double scaleFactor, double offSet){
        double deltaHeight = this.paneViewContent.getLayoutBounds().getHeight() - this.getViewportBounds().getHeight();
        if (deltaHeight > 0) {
            double halfHeight = this.getViewportBounds().getHeight() / 2 ;
            double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * offSet;
            this.setVvalue(this.getVmin() + newScrollYOffset * (this.getVmax() - this.getVmin()) / deltaHeight);
        } else {
            this.setVvalue(this.getVmin());
        }
    }

    public Point2D calculateScrollOffset() {
        double XOffset = calculateHorizontalOffset();
        double YOffset = calculateVerticalOffset();
        return new Point2D(XOffset, YOffset);
    }

    private double calculateHorizontalOffset(){
        double contentWidth = this.paneViewContent.getLayoutBounds().getWidth();
        double viewportWidth = this.getViewportBounds().getWidth();
        double deltaWidth = contentWidth - viewportWidth;
        double horizontalScrollProportion = (this.getHvalue() - this.getHmin()) / (this.getHmax() - this.getHmin());

        return horizontalScrollProportion * Math.max(0, deltaWidth);
    }

    private double calculateVerticalOffset(){
        double contentHeight = this.paneViewContent.getLayoutBounds().getHeight();
        double viewportHeight = this.getViewportBounds().getHeight();
        double deltaHeight = contentHeight - viewportHeight;
        double verticalScrollProportion = (this.getVvalue() - this.getVmin()) / (this.getVmax() - this.getVmin());

        return verticalScrollProportion * Math.max(0, deltaHeight);
    }

    public void zoomToCenter(double scaleFactor){
        // amount of scrolling in each direction in paneViewContent coordinate units
        Point2D scrollOffset = this.calculateScrollOffset();
        Canvas canvas = mapCanvas.getCanvas();

        canvas.setScaleX(canvas.getScaleX() * scaleFactor);
        canvas.setScaleY(canvas.getScaleY() * scaleFactor);

        // move viewport so that old center remains in the center after the scaling
        this.repositionScroller(scaleFactor, scrollOffset);
    }
}
