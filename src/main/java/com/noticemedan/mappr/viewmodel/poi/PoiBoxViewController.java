package com.noticemedan.mappr.viewmodel.poi;

import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.user.FavoritePoi;
import com.noticemedan.mappr.model.util.Coordinate;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.Setter;

import javax.inject.Inject;

public class PoiBoxViewController {
	@FXML Button closePoiBoxButton;
	@FXML Button savePoiButton;
	@FXML Pane poiBoxContainer;
	@FXML Label poiCoordinatesLabel;
	@FXML TextField namePoiTextfield;

	private Coordinate coordinate;
	private DomainFacade domain;

	@Inject
	public PoiBoxViewController(DomainFacade domainFacade) {
		this.domain = domainFacade;
	}

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
		FavoritePoi newFavoritePoi = new FavoritePoi(this.coordinate, namePoiTextfield.getText());
		this.domain.addPoi(newFavoritePoi);
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
		shortCoordinateX = shortCoordinateX.substring(0,10);

		String shortCoordinateY = String.valueOf(coordinate.getY());
		shortCoordinateY = shortCoordinateY.substring(0,10);

		poiCoordinatesLabel.setText(shortCoordinateY + ", " + shortCoordinateX);
		poiBoxContainer.setVisible(true);
		poiBoxContainer.setManaged(true);
	}
}
