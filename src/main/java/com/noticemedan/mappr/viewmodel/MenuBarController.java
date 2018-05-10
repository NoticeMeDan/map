package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.model.util.OsmElementProperty;
import com.noticemedan.mappr.view.util.FilePicker;
import com.noticemedan.mappr.view.util.InfoBox;
import io.vavr.control.Option;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.nio.file.Path;

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
		MainViewController.getCanvas().repaint();
	}

	private void toggleReversedBorders() {
		this.showReversedBorders = !this.showReversedBorders;
		String labelStart = (this.showReversedBorders) ? "Fjern" : "Vis";
		MainViewController.getCanvas().toggleReversedBorders();
		MainViewController.getCanvas().repaint();
		menuShowReversedBorders.setText(labelStart + " range-search");
	}

	private void toggleFPS() {
		this.showFPS = !this.showFPS;
		String labelStart = (this.showFPS) ? "Fjern" : "Vis";
		MainViewController.getCanvas().toggleFPS();
		MainViewController.getCanvas().repaint();
		menuShowFPS.setText(labelStart + " FPS");
	}

	private void toggleDijkstra() {
		this.showDijkstra = !this.showDijkstra;
		String labelStart = (this.showDijkstra) ? "Fjern" : "Vis";
		MainViewController.getCanvas().toggleDijkstraNetwork();
		MainViewController.getCanvas().repaint();
		menuShowDijkstraNetwork.setText(labelStart + " dijkstra-netværk");
	}

	private void toggleShortestPath() {
		this.showShortestPath = !this.showShortestPath;
		String labelStart = (this.showShortestPath) ? "Fjern" : "Vis";
		MainViewController.getCanvas().toggleRandomShortestPath();
		MainViewController.getCanvas().repaint();
		menuShowShortestPath.setText(labelStart + " shortest path");
	}

	private void createMapFromOsm(ActionEvent event) {
		Stage stage = (Stage) this.menuBar.getScene().getWindow();
		FilePicker picker = new FilePicker(new FileChooser
				.ExtensionFilter("OSM Files (*.osm or *.osm.zip)", "*.osm", "*.osm.zip"));

		Option<Path> path = picker.getPath(stage);
		if (!path.isEmpty()) {
			new InfoBox("Vi danner kortet i baggrunden - du vil få besked når det er færdigt.").show();
			InfoBox onComplete = new InfoBox("Kortet er nu oprettet, og du har muligheden for at tilgå det fra menuen.");
			InfoBox onFailed = new InfoBox("Der opsted en fejl under oprettelsen af kortet. Tilkald venligst dine nærmeste chimpanser.");
			domain.buildMapFromOsmPath(path.get(), x -> onComplete.show(), x -> onFailed.show());
		}
	}
}
