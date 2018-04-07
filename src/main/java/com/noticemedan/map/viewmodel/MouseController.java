package com.noticemedan.map.viewmodel;

import com.noticemedan.map.model.KDTree.Rect;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MouseController {

    private final double factor = 1.1;

    //THIS NEEDS TO BE REDONE TO WORK WITH KD-TREE AND zoomLevel in MapCanvas
	/**
	 * TODO Make zoomMethod: that pivots around mouseposition.
	 * TODO Make zoomMethod: that take use of the zoomLevel variable in MapCanvas.
	 * TODO Make zoomMethod: that scales properly without messing with the KD-TREE search area.
	 * */
    public void addZoomAbility(CustomPane customPane){
        customPane.getCanvasContent().setOnScroll(event -> {
            event.consume();

            if (event.getDeltaY() == 0) return;

            double scaleFactor = (event.getDeltaY() > 0) ? factor : 1 / factor;

            customPane.zoomToCenter(scaleFactor);
        });
    }

	//STILL BUGGY - Don't always draw on last position, and a bit laggy.
	public void dragAndDraw(CustomPane customPane){
		customPane.setOnDragDetected(event -> {
			event.consume();

			//multiply with 0.5/1.5 to increase the rangesearch search area
            double minX = Math.abs(customPane.getViewportBounds().getMinX()) * 0.5;
            double minY = Math.abs(customPane.getViewportBounds().getMinY()) * 0.5;

            double maxX = minX + customPane.getViewportBounds().getWidth() * 1.5;
            double maxY = minY + customPane.getViewportBounds().getHeight() * 1.5;

			Rect viewPort = new Rect(minX,minY,maxX,maxY);
			log.info("3  " + viewPort);

			customPane.getMapCanvas().setViewArea(viewPort);
			customPane.getMapCanvas().redrawCanvas();
		});
	}
}
