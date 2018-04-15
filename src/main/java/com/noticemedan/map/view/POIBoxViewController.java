package com.noticemedan.map.view;

import com.noticemedan.map.model.utilities.Coordinate;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class POIBoxViewController {

	@FXML Button closePOIBoxButton;
	@FXML Button savePOIButton;
	@FXML Pane poiBoxContainer;
	@FXML Label poiCoordinatesLabel;
	@FXML TextField namePOITextfield;

	Coordinate currentCoordinate;

	public void initialize() {
		eventListeners();
	}

	private void eventListeners() {
		closePOIBoxButton.setOnAction(event -> closePOIBox());
		savePOIButton.setOnAction(event -> savePOI());
	}

	private void savePOI () {
		closePOIBox();
	}

	public void closePOIBox() {
		poiBoxContainer.setVisible(false);
		poiBoxContainer.setManaged(false);
		namePOITextfield.clear();
	}

	public void openPOIBox(Coordinate coordinate) {
		this.currentCoordinate = coordinate;
		poiCoordinatesLabel.setText(coordinate.getX() + ", " + coordinate.getY());
		poiBoxContainer.setVisible(true);
		poiBoxContainer.setManaged(true);
	}
}
