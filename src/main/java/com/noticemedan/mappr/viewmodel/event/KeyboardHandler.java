package com.noticemedan.mappr.viewmodel.event;

import com.noticemedan.mappr.viewmodel.CanvasView;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class KeyboardHandler implements EventHandler<KeyEvent> {
	CanvasView canvas;

	public KeyboardHandler(CanvasView canvas) {
		this.canvas = canvas;
	}

	@Override
	public void handle(KeyEvent event) {
		switch (event.getText()) {
			case "x":
				canvas.toggleAntiAliasing();
				break;
			case "w":
				canvas.pan(0, 10);
				break;
			case "a":
				canvas.pan(10, 0);
				break;
			case "s":
				canvas.pan(0, -10);
				break;
			case "d":
				canvas.pan(-10, 0);
				break;
			case "p":
				if (canvas.isLogPerformanceTimeDrawVSRangeSearch()) canvas.setLogPerformanceTimeDrawVSRangeSearch(false);
				else canvas.setLogPerformanceTimeDrawVSRangeSearch(true);
				break;
			case "r":
				if (canvas.isLogRangeSearchSize()) canvas.setLogRangeSearchSize(false);
				else canvas.setLogRangeSearchSize(true);
				break;
			case "z":
				if (canvas.isLogZoomLevel()) canvas.setLogZoomLevel(false);
				else canvas.setLogZoomLevel(true);
				break;
			case "+":
				canvas.zoomToCenter(1.1);
				break;
			case "-":
				canvas.zoomToCenter(1/1.1);
				break;
			default:
				break;
		}
	}
}
