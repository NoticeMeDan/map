package com.noticemedan.map.view;

import com.noticemedan.map.model.user.FavoritePoi;
import com.noticemedan.map.model.utilities.Coordinate;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class PoiBoxViewController {
	@FXML Button closePoiBoxButton;
	@FXML Button savePoiButton;
	@FXML Pane poiBoxContainer;
	@FXML Label poiCoordinatesLabel;
	@FXML TextField namePoiTextfield;
	@Setter
	ObservableList<FavoritePoi> favoritePois;

	private Coordinate coordinate;

	public void initialize() {
		closePoiBox();
		savePoiButton.setDisable(true);
		eventListeners();
	}

	private void eventListeners() {
		closePoiBoxButton.setOnAction(event -> closePoiBox());
		namePoiTextfield.setOnKeyPressed(event -> enableSavePoiButton());
		savePoiButton.setOnAction(event -> savePoi());
	}

	private void enableSavePoiButton() {
		if (namePoiTextfield.getText() == null || namePoiTextfield.getText().equals("")) savePoiButton.setDisable(true);
		else savePoiButton.setDisable(false);
	}

	private void savePoi() {
		favoritePois.add(new FavoritePoi(this.coordinate, namePoiTextfield.getText()));
		closePoiBox();
	}

	public void closePoiBox() {
		poiBoxContainer.setVisible(false);
		poiBoxContainer.setManaged(false);
		namePoiTextfield.clear();
		savePoiButton.setDisable(true);
	}

	public void openPoiBox(Coordinate coordinate) {
		this.coordinate = coordinate;

		String shortCoordinateX = String.valueOf(coordinate.getX());
		shortCoordinateX = shortCoordinateX.substring(0,9);

		String shortCoordinateY = String.valueOf(coordinate.getY());
		shortCoordinateY = shortCoordinateY.substring(0,10); //TODO 9 @emil when not canvas coords.

		poiCoordinatesLabel.setText(shortCoordinateX + ", " + shortCoordinateY);
		poiBoxContainer.setVisible(true);
		poiBoxContainer.setManaged(true);
	}
}
