package com.noticemedan.map.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

public class RoutePaneController {
	@FXML Pane routePane;
	@FXML Button routePaneCloseButton;
	@FXML ToggleButton routeCarToggleButton;
	@FXML ToggleButton routeWalkToggleButton;
	@FXML ToggleButton routeBicycleToggleButton;
	@FXML TextField searchStartPointAddressField;
	@FXML TextField searchEndPointAddressField;
	@FXML ListView routeSearchResultsListView;

	public void initialize() {
		closeRoutePane();
		eventListeners();
	}

	private void eventListeners() {
		routePaneCloseButton.setOnAction(event -> closeRoutePane());
	}

	public void openRoutePane() {
		routePane.setVisible(true);
		routePane.setManaged(true);
		searchStartPointAddressField.requestFocus();
	}

	private void closeRoutePane() {
		routePane.setVisible(false);
		routePane.setManaged(false);
		searchStartPointAddressField.clear();
		searchEndPointAddressField.clear();
		routeSearchResultsListView.getItems().clear();
	}
}
