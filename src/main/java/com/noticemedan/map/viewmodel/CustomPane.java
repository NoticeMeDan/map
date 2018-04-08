package com.noticemedan.map.viewmodel;

import com.noticemedan.map.model.KDTree.Rect;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import lombok.Getter;

import java.awt.*;

public class CustomPane extends ScrollPane {
    private Group paneViewContent;
    @Getter private Group canvasContent;
    @Getter private MapCanvas mapCanvas;

    public CustomPane(Dimension dim) {
        super();
        initializeFields();
        applyContent();
        adjustPaneProperties();
        setWidth(dim.getWidth());
        setHeight(dim.getHeight());
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
    	this.setStyle("-fx-background: #2349B5");
		this.setPrefWidth(1100);
		this.setPrefHeight(650);
        //this.setHbarPolicy(ScrollBarPolicy.NEVER);
        //this.setVbarPolicy(ScrollBarPolicy.NEVER);
        this.setPannable(true);
    }

    public Rect getExtendedViewPortBounds() {
		double minX = Math.abs(this.getViewportBounds().getMinX());
		double minY = Math.abs(this.getViewportBounds().getMinY());
		double maxX = minX + this.getViewportBounds().getWidth();
		double maxY = minY + this.getViewportBounds().getHeight();

		return new Rect(minX * 0.5 ,minY * 0.5,maxX * 1.5,maxY * 1.5);
	}
}
