package com.noticemedan.map.viewmodel;

import com.noticemedan.map.model.Utilities.Rect;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MouseController {

    private double zoomLevel = 1.0;
	boolean trigger;

	/*THIS NEEDS TO BE REDONE TO WORK WITH KD-TREE AND zoomLevel in OSMCanvas
	TODO Make zoomMethod: that pivots around mouseposition.
	TODO Make zoomMethod: that take use of the zoomLevel variable in OSMCanvas.
	TODO Make zoomMethod: that scales properly without messing with the KD-TREE search area.*/
	public void addZoomAbility(OSMPane osmPane) {
		osmPane.getCanvasContent().setOnScroll(event -> {
			event.consume();

			if (event.getDeltaY() == 0) return;

			double oldZoomLevel = osmPane.getOsmCanvas().getZoomLevel();
			this.zoomLevel = (event.getDeltaY() > 0) ? oldZoomLevel * 1.1 : oldZoomLevel * (1 / 1.1);

			osmPane.getOsmCanvas().setZoomLevel(this.zoomLevel);
			osmPane.getOsmCanvas().redrawCanvas();
		});
	}

	// TODO FIX INITIAL LAG
	public void dragAndDraw(OSMPane osmPane) {
		osmPane.setOnDragDetected(event -> {
			event.consume();
			drawOnRelease(osmPane);
		});
	}

	private void drawOnRelease(OSMPane osmPane) {
		osmPane.setOnMousePressed(event -> {
			event.consume();
			Rect viewPort = osmPane.getExtendedViewPortBounds(this.zoomLevel);
			osmPane.getOsmCanvas().setViewArea(viewPort);
			osmPane.getOsmCanvas().redrawCanvas();
		});
	}
}
