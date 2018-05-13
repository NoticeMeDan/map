package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.model.util.OsmElementProperty;
import com.noticemedan.mappr.view.util.FilePicker;
import io.vavr.control.Option;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.nio.file.Path;

@Slf4j
public class MenuBarController {
	@FXML MenuBar menuBar;
	@FXML MenuItem showFPSMenuItem;
	@FXML MenuItem showReversedBordersMenuItem;
	@FXML MenuItem showDijkstraNetworkMenuItem;
	@FXML MenuItem showShortestPathMenuItem;
	@FXML MenuItem showColorBlindModeMenuItem;
	@FXML MenuItem showStandardColorMenuItem;
	@FXML MenuItem showMapMenuItem;
	@FXML MenuItem loadFromOsmMenuItem;

	@Setter
	MainViewController mainViewController;

	private boolean showFPS = false;
	private boolean showReversedBorders = false;
	private boolean showDijkstra = false;
	private boolean showMapPane = false;

	private DomainFacade domain;

	@Inject
	public MenuBarController(DomainFacade domainFacade) { this.domain = domainFacade; }

	public void initialize() {
		eventListeners();
	}

	private void eventListeners() {
		showFPSMenuItem.setOnAction(event -> toggleFPS());
		showReversedBordersMenuItem.setOnAction(event -> toggleReversedBorders());
		showDijkstraNetworkMenuItem.setOnAction(event -> toggleDijkstra());
		showShortestPathMenuItem.setOnAction(event -> toggleShortestPath());
		showColorBlindModeMenuItem.setOnAction(event -> colorProfile("showColorBlindModeMenuItem"));
		showStandardColorMenuItem.setOnAction(event -> colorProfile("standard"));
		showMapMenuItem.setOnAction(event -> toggleMapPane());
		loadFromOsmMenuItem.setOnAction(event -> loadFromOsm());
	}

	private void colorProfile(String colorProfile) {
		switch (colorProfile) {
			case "showColorBlindModeMenuItem":
				OsmElementProperty.colorBlind();
				break;

			default:
				OsmElementProperty.standardColor();
				break;
		}
		MainViewController.getCanvas().repaint();
	}

	private void toggleReversedBorders() {
		this.showReversedBorders = !this.showReversedBorders;
		String labelStart = (this.showReversedBorders) ? "Fjern" : "Vis";
		MainViewController.getCanvas().toggleReversedBorders();
		MainViewController.getCanvas().repaint();
		showReversedBordersMenuItem.setText(labelStart + " range-search");
	}

	private void toggleFPS() {
		this.showFPS = !this.showFPS;
		String labelStart = (this.showFPS) ? "Fjern" : "Vis";
		MainViewController.getCanvas().toggleFPS();
		MainViewController.getCanvas().repaint();
		showFPSMenuItem.setText(labelStart + " FPS");
	}

	private void toggleDijkstra() {
		this.showDijkstra = !this.showDijkstra;
		String labelStart = (this.showDijkstra) ? "Fjern" : "Vis";
		MainViewController.getCanvas().toggleDijkstraNetwork();
		MainViewController.getCanvas().repaint();
		showDijkstraNetworkMenuItem.setText(labelStart + " dijkstra-netværk");
	}

	private void toggleShortestPath() {
		MainViewController.getCanvas().toggleRandomShortestPath();
		MainViewController.getCanvas().repaint();
	}

	private void toggleMapPane() {
		this.showMapPane = !this.showMapPane;
		mainViewController.getMapPaneController().openMapPane();
		mainViewController.pushCanvas();
	}

	private void loadFromOsm() {
		Stage stage = (Stage) this.menuBar.getScene().getWindow();
		FilePicker picker = new FilePicker(new FileChooser
				.ExtensionFilter("OSM Filer (*.osm or *.zip)", "*.osm", "*.zip"));

		Option<Path> path = picker.getPath(stage);
		if (!path.isEmpty()) {
			this.domain.loadMapFromOsm(path.get());
			this.mainViewController.centerViewport();
			MainViewController.getCanvas().repaint();
		}
	}
}
