package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.model.util.OsmElementProperty;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class MenuBarController {
	@FXML MenuItem menuShowFPS;
	@FXML MenuItem menuShowReversedBorders;
	@FXML MenuItem menuShowDijkstraNetwork;
	@FXML MenuItem menuShowShortestPath;
	@FXML MenuItem colourBlind;
	@FXML MenuItem standardColour;

	private boolean showFPS = false;
	private boolean showReversedBorders = false;
	private boolean showDijkstra = false;
	private boolean showShortestPath = false;

	public void initialize() {
		eventListeners();
	}

	private void eventListeners() {
		menuShowFPS.setOnAction(event -> toggleFPS());
		menuShowReversedBorders.setOnAction(event -> toggleReversedBorders());
		menuShowDijkstraNetwork.setOnAction(event -> toggleDijkstra());
		menuShowShortestPath.setOnAction(event -> toggleShortestPath());
		colourBlind.setOnAction(event -> colourProfile("colourBlind"));
		standardColour.setOnAction(event -> colourProfile("standard"));
	}

	private void colourProfile(String colourProfile) {
		switch (colourProfile) {
			case "colourBlind":
				OsmElementProperty.colorBlind();
				break;

			default:
				OsmElementProperty.standardColour();
				break;
		}
		MainViewController.getCanvasView().repaint();
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

	private void toggleDijkstra() {
		this.showDijkstra = !this.showDijkstra;
		String labelStart = (this.showDijkstra) ? "Fjern" : "Vis";
		MainViewController.getCanvasView().toggleDijkstraNetwork();
		MainViewController.getCanvasView().repaint();
		menuShowDijkstraNetwork.setText(labelStart + " dijkstra-netværk");
	}

	private void toggleShortestPath() {
		this.showShortestPath = !this.showShortestPath;
		String labelStart = (this.showShortestPath) ? "Fjern" : "Vis";
		MainViewController.getCanvasView().toggleRandomShortestPath();
		MainViewController.getCanvasView().repaint();
		menuShowShortestPath.setText(labelStart + " shortest path");
	}
}
