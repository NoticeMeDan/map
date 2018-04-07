package com.noticemedan.map.viewmodel;

import com.noticemedan.map.model.KDTree.Rect;

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

    public void panning(CustomPane customPane){
    	customPane.setOnDragDetected(event -> {
			/*System.out.println("Scrollbar: "+ customPane.getHvalue() + " | " + customPane.getVvalue());
			System.out.println("Layout: "+ customPane.getLayoutX() + " | " + customPane.getLayoutY());
			System.out.println("Viewport Bounds: " + customPane.getViewportBounds());*/
		});
	}

	//TESTING
	public void panDraw(CustomPane customPane){
		customPane.setOnDragDetected(event -> customPane.setOnMouseReleased(event2 -> {
            double minX = Math.abs(customPane.getViewportBounds().getMinX());
            double minY = Math.abs(customPane.getViewportBounds().getMinY());

            double maxX = minX + customPane.getViewportBounds().getWidth();
            double maxY = minY + customPane.getViewportBounds().getHeight();
            Rect viewPort = new Rect(minX,minY,maxX*2,maxY*2);

			System.out.println(customPane.getViewportBounds().getWidth() + " , " + customPane.getViewportBounds().getHeight() + " | " + customPane.getViewportBounds().getMinX() + " , " + customPane.getViewportBounds().getMinY());
            System.out.println("3  " + viewPort);
            customPane.getMapCanvas().setViewArea(viewPort);
            customPane.getMapCanvas().redrawCanvas();
			}));
	}
}
