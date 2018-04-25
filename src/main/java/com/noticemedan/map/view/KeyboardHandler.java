package com.noticemedan.map.view;

import com.noticemedan.map.viewmodel.CanvasView;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

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
