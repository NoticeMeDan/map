package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.dao.MapDao;
import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.model.util.OsmElementProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class MenuBarController {
	@FXML MenuBar menuBar;
	@FXML MenuItem menuShowFPS;
	@FXML MenuItem menuShowReversedBorders;
	@FXML MenuItem menuShowDijkstraNetwork;
	@FXML MenuItem menuShowShortestPath;
	@FXML MenuItem menuCreateMapFromOsm;
	@FXML MenuItem colorBlind;
	@FXML MenuItem standardColor;

	private boolean showFPS = false;
	private boolean showReversedBorders = false;
	private boolean showDijkstra = false;
	private boolean showShortestPath = false;

	private DomainFacade domain;

	@Inject
	public MenuBarController(DomainFacade domainFacade) {
		this.domain = domainFacade;
	}

	public void initialize() {
		eventListeners();
	}

	private void eventListeners() {
		menuShowFPS.setOnAction(event -> toggleFPS());
		menuShowReversedBorders.setOnAction(event -> toggleReversedBorders());
		menuShowDijkstraNetwork.setOnAction(event -> toggleDijkstra());
		menuShowShortestPath.setOnAction(event -> toggleShortestPath());
		menuCreateMapFromOsm.setOnAction(this::createMapFromOsm);
		colorBlind.setOnAction(event -> colorProfile("colorBlind"));
		standardColor.setOnAction(event -> colorProfile("standard"));
	}

	private void colorProfile(String colorProfile) {
		switch (colorProfile) {
			case "colorBlind":
				OsmElementProperty.colorBlind();
				break;

			default:
				OsmElementProperty.standardColor();
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

	private void createMapFromOsm(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		Stage stage = (Stage) this.menuBar.getScene().getWindow();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("OSM Files (*.osm or *.osm.zip)", "*.osm", "*.osm.zip");
		fileChooser.getExtensionFilters().addAll(extFilter);

		Path path = Paths.get(fileChooser.showOpenDialog(stage).toURI());

		if (Files.exists(path)) log.info("File found: " + path.toString());

		log.info("Running future");
	}
}
