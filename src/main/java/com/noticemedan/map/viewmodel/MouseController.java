package com.noticemedan.map.viewmodel;

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
}
