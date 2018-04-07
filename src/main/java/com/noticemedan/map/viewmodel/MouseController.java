package com.noticemedan.map.viewmodel;

import com.noticemedan.map.model.KDTree.Rect;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MouseController {

    private final double factor = 1.1;

    public void addZoomAbility(CustomPane customPane){
        customPane.getCanvasContent().setOnScroll(event -> {
            event.consume();

            if (event.getDeltaY() == 0) return;

            double scaleFactor = (event.getDeltaY() > 0) ? factor : 1 / factor;

            customPane.zoomToCenter(scaleFactor);
        });
    }

	//TESTING
	public void dragAndDraw(CustomPane customPane){
		customPane.setOnDragDetected(event -> {
			event.consume();

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
