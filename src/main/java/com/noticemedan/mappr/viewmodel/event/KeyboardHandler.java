package com.noticemedan.mappr.viewmodel.event;

import com.noticemedan.mappr.viewmodel.MainViewController;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import lombok.Setter;

public class KeyboardHandler implements EventHandler<KeyEvent> {
	@Setter
	MainViewController mainViewController;

	public KeyboardHandler(MainViewController mainViewController) {
		this.mainViewController = mainViewController;
	}

	@Override
	public void handle(KeyEvent event) {
		switch (event.getText()) {
			case "x":
				mainViewController.getCanvas().toggleAntiAliasing();
				break;
			case "w":
				mainViewController.getCanvas().pan(0, 10);
				break;
			case "a":
				mainViewController.getCanvas().pan(10, 0);
				break;
			case "s":
				mainViewController.getCanvas().pan(0, -10);
				break;
			case "d":
				mainViewController.getCanvas().pan(-10, 0);
				break;
			case "p":
				if (mainViewController.getCanvas().isLogPerformanceTimeDrawVSRangeSearch()) mainViewController.getCanvas().setLogPerformanceTimeDrawVSRangeSearch(false);
				else mainViewController.getCanvas().setLogPerformanceTimeDrawVSRangeSearch(true);
				break;
			case "r":
				if (mainViewController.getCanvas().isLogRangeSearchSize()) mainViewController.getCanvas().setLogRangeSearchSize(false);
				else mainViewController.getCanvas().setLogRangeSearchSize(true);
				break;
			case "z":
				if (mainViewController.getCanvas().isLogZoomLevel()) mainViewController.getCanvas().setLogZoomLevel(false);
				else mainViewController.getCanvas().setLogZoomLevel(true);
				break;
			case "n":
				if (mainViewController.getCanvas().isLogNearestNeighbor()) mainViewController.getCanvas().setLogNearestNeighbor(false);
				else mainViewController.getCanvas().setLogNearestNeighbor(true);
				break;
			case "+":
				mainViewController.getCanvas().zoomToCenter(1.1);
				break;
			case "-":
				mainViewController.getCanvas().zoomToCenter(1/1.1);
				break;
			default:
				break;
		}
	}
}
