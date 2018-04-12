package com.noticemedan.map.view;

import com.noticemedan.map.data.OSMManager;
import com.noticemedan.map.model.Entities;
import com.noticemedan.map.viewmodel.MouseController;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.awt.*;

public class MainViewController {
	//Content Pane
	@FXML Button routeButton;
	@FXML Pane searchFieldImitator;
	@FXML
	Pane osmPaneContainer;

	//Search pane
	@FXML Pane searchPane;
	@FXML TextField searchAddressField;
	@FXML Button searchPaneCloseButton;
	@FXML ListView addressSearchResultsListView;

	//Route Pane
	@FXML Pane routePane;
	@FXML Button routePaneCloseButton;
	@FXML ToggleButton routeCarToggleButton;
	@FXML ToggleButton routeWalkToggleButton;
	@FXML ToggleButton routeBicycleToggleButton;
	@FXML TextField searchStartPointAddressField;
	@FXML TextField searchEndPointAddressField;
	@FXML ListView routeSearchResultsListView;

	public void initialize() {
		insertOSMPane();
		hideComponentsAtStartUp();
		eventListeners();
	}

	private void insertOSMPane() {
		Dimension screenSize = new Dimension(1100, 650);

		SwingNode swingNode = new SwingNode();
		OSMManager m = new OSMManager();
		CanvasView cv = new CanvasView(m);
		cv.setSize(screenSize);
		System.out.println(Entities.writeOut());
		cv.pan(-Entities.getMinLon(), -Entities.getMaxLat());
		cv.zoom(screenSize.getWidth() / (Entities.getMaxLon() - Entities.getMinLon()), 0, 0);
		new MouseController(cv, m);
		SwingUtilities.invokeLater(() -> {
			swingNode.setContent(cv);
		});

		osmPaneContainer.getChildren().addAll(swingNode);
	}


	private void hideComponentsAtStartUp() {
		routePane.setVisible(false);
		routePane.setManaged(false);
		searchPane.setVisible(false);
		searchPane.setManaged(false);
	}

	private void eventListeners() {
		searchFieldImitator.setOnMouseClicked(event -> openSearchPane());

		searchPaneCloseButton.setOnAction(event -> closeSearchPane());

		routeButton.setOnAction(event -> openRoutePane());
		routePaneCloseButton.setOnAction(event -> closeRoutePane());

		searchStartPointAddressField.setOnMouseClicked(event -> routeSearchResultsListView.getItems().clear());
		searchEndPointAddressField.setOnMouseClicked(event -> routeSearchResultsListView.getItems().clear());
	}

	private void openSearchPane() {
		searchPane.setVisible(true);
		searchPane.setManaged(true);
		searchAddressField.requestFocus();
	}

	private void closeSearchPane() {
		addressSearchResultsListView.getItems().clear();
		searchAddressField.clear();
		searchPane.setVisible(false);
		searchPane.setManaged(false);
	}

	private void openRoutePane() {
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
