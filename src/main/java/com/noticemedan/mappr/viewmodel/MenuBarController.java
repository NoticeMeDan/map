package com.noticemedan.mappr.viewmodel;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class MenuBarController {
	@FXML MenuItem menuShowFPS;
	@FXML MenuItem menuShowReversedBorders;

	private boolean showFPS = false;
	private boolean showReversedBorders = false;

	public void initialize() {
		eventListeners();
	}

	private void eventListeners() {
		menuShowFPS.setOnAction(event -> toggleFPS());
		menuShowReversedBorders.setOnAction(event -> toggleReversedBorders());
	}

	private void toggleReversedBorders() {
		this.showReversedBorders = !this.showReversedBorders;
		String labelStart = (this.showReversedBorders) ? "Fjern" : "Vis";
		MainViewController.getCanvasView().toggleReversedBorders();
		MainViewController.getCanvasView().repaint();
		menuShowReversedBorders.setText(labelStart + " range-search");
	}

	private void toggleFPS() {
		this.showFPS = !this.showFPS;
		String labelStart = (this.showFPS) ? "Fjern" : "Vis";
		MainViewController.getCanvasView().toggleFPS();
		MainViewController.getCanvasView().repaint();
		menuShowFPS.setText(labelStart + " FPS");
	}


}
