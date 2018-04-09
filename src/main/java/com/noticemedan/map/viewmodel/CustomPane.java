package com.noticemedan.map.viewmodel;

import com.noticemedan.map.model.KDTree.Rect;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import lombok.Getter;

public class CustomPane extends ScrollPane {
    private Group paneViewContent;
    @Getter private Group canvasContent;
    @Getter private MapCanvas mapCanvas;

	public CustomPane() {
        super();
        initializeFields();
        applyContent();
        adjustPaneProperties();
    }

    private void initializeFields() {
        this.mapCanvas = new MapCanvas();
        this.paneViewContent = new Group();
        this.canvasContent = new Group();
    }

    private void applyContent() {
        paneViewContent.getChildren().add(canvasContent);
        canvasContent.getChildren().add(mapCanvas.getCanvas());
        this.setContent(paneViewContent);
    }

    private void adjustPaneProperties() {
		this.setStyle("-fx-background: #2082b5");
		this.setPrefWidth(1100);
		this.setPrefHeight(650);
        //this.setHbarPolicy(ScrollBarPolicy.NEVER);
        //this.setVbarPolicy(ScrollBarPolicy.NEVER);
        this.setPannable(true);
    }

    public Rect getExtendedViewPortBounds(double zoomLevel) {
		double minX = Math.abs(this.getViewportBounds().getMinX());
		double minY = Math.abs(this.getViewportBounds().getMinY());
		double maxX = minX + this.getViewportBounds().getWidth();
		double maxY = minY + this.getViewportBounds().getHeight();
		double extendMaximum = 1.5 * (1 / zoomLevel);
		double extendMinimum = 0.5 * (1 / zoomLevel);
		return new Rect(minX * 0.5 * extendMinimum, minY * extendMinimum, maxX * extendMaximum, maxY * extendMaximum);
	}
}
