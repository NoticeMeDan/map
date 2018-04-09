package com.noticemedan.map.viewmodel;

import com.noticemedan.map.model.KDTree.Rect;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MouseController {

    private double zoomLevel = 1.0;
	boolean trigger;

	/*THIS NEEDS TO BE REDONE TO WORK WITH KD-TREE AND zoomLevel in MapCanvas
	TODO Make zoomMethod: that pivots around mouseposition.
	TODO Make zoomMethod: that take use of the zoomLevel variable in MapCanvas.
	TODO Make zoomMethod: that scales properly without messing with the KD-TREE search area.*/
	public void addZoomAbility(CustomPane customPane) {
		customPane.getCanvasContent().setOnScroll(event -> {
			event.consume();

			if (event.getDeltaY() == 0) return;

			double oldZoomLevel = customPane.getMapCanvas().getZoomLevel();
			this.zoomLevel = (event.getDeltaY() > 0) ? oldZoomLevel * 1.1 : oldZoomLevel * (1 / 1.1);

			customPane.getMapCanvas().setZoomLevel(this.zoomLevel);
			customPane.getMapCanvas().redrawCanvas();
		});
	}

	// TODO FIX INITIAL LAG
	public void dragAndDraw(CustomPane customPane) {
		customPane.setOnDragDetected(event -> {
			event.consume();
			drawOnRelease(customPane);
		});
	}

	private void drawOnRelease(CustomPane customPane) {
		customPane.setOnMousePressed(event -> {
			event.consume();
			Rect viewPort = customPane.getExtendedViewPortBounds(this.zoomLevel);
			customPane.getMapCanvas().setViewArea(viewPort);
			customPane.getMapCanvas().redrawCanvas();
		});
	}
}
