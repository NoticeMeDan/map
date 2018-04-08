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
	//OLD ZOOM
    public void addZoomAbility(CustomPane customPane){
        customPane.getCanvasContent().setOnScroll(event -> {
            event.consume();

            if (event.getDeltaY() == 0) return;

            double zoomLevel = customPane.getMapCanvas().getZoomLevel();
            zoomLevel = (event.getDeltaY() > 0) ? zoomLevel * 1.1 : zoomLevel * (1 / 1.1);

            customPane.getMapCanvas().setZoomLevel(zoomLevel);
            customPane.getMapCanvas().redrawCanvas();
        });
    }

	//STILL BUGGY - Don't always draw on last position, and a bit laggy.
	public void dragAndDraw(CustomPane customPane) {
	customPane.setOnDragDetected(event -> {
		event.consume();
		Rect viewPort = customPane.getExtendedViewPortBounds();
		log.info("3  " + viewPort);
		customPane.getMapCanvas().setViewArea(viewPort);
		customPane.getMapCanvas().redrawCanvas();
	});
	}
}

