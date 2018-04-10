package com.noticemedan.map.view;

import com.noticemedan.map.viewmodel.MouseController;
import com.noticemedan.map.viewmodel.OSMPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

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
		OSMPane osmPane = new OSMPane();
		MouseController controller = new MouseController();
		controller.addZoomAbility(osmPane);
		controller.dragAndDraw(osmPane);
		osmPaneContainer.getChildren().addAll(osmPane);
	}

	private void hideComponentsAtStartUp() {
		routePane.setVisible(false);
		routePane.setManaged(false);
		searchPane.setVisible(false);
		searchPane.setManaged(false);
	}

	private void eventListeners() {
		searchFieldImitator.setOnMouseClicked(event -> openSearchPane());

		searchAddressField.setOnAction(event -> addressSearchResultsListView.getItems().setAll(setUpDummyData()));
		searchPaneCloseButton.setOnAction(event -> closeSearchPane());

		routeButton.setOnAction(event -> openRoutePane());
		routePaneCloseButton.setOnAction(event -> closeRoutePane());

		searchStartPointAddressField.setOnAction(event -> routeSearchResultsListView.getItems().setAll(setUpDummyData()));
		searchEndPointAddressField.setOnAction(event -> routeSearchResultsListView.getItems().setAll(setUpDummyData()));

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

	//TODO Delete when proper search function is implemented.
	private ObservableList<DummyAddress> setUpDummyData() {
		ObservableList<DummyAddress> dummyAddressSearchHits = FXCollections.observableArrayList();
		dummyAddressSearchHits.add(new DummyAddress("Frederiksberg Allé","20","2000","Frederiksberg C"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikskongehave","10","1600","Vejle"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikshavnsvej","168","1780","Hillerød"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikssøndervej","178","2100","Hellerup"));
		dummyAddressSearchHits.add(new DummyAddress("Frederiksberg Allé","20","2000","Frederiksberg C"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikskongehave","10","1600","Vejle"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikshavnsvej","168","1780","Hillerød"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikssøndervej","178","2100","Hellerup"));
		dummyAddressSearchHits.add(new DummyAddress("Frederiksberg Allé","20","2000","Frederiksberg C"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikskongehave","10","1600","Vejle"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikshavnsvej","168","1780","Hillerød"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikssøndervej","178","2100","Hellerup"));
		dummyAddressSearchHits.add(new DummyAddress("Frederiksberg Allé","20","2000","Frederiksberg C"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikskongehave","10","1600","Vejle"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikshavnsvej","168","1780","Hillerød"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikssøndervej","178","2100","Hellerup"));
		dummyAddressSearchHits.add(new DummyAddress("Frederiksberg Allé","20","2000","Frederiksberg C"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikskongehave","10","1600","Vejle"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikshavnsvej","168","1780","Hillerød"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikssøndervej","178","2100","Hellerup"));
		dummyAddressSearchHits.add(new DummyAddress("Frederiksberg Allé","20","2000","Frederiksberg C"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikskongehave","10","1600","Vejle"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikshavnsvej","168","1780","Hillerød"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikssøndervej","178","2100","Hellerup"));
		dummyAddressSearchHits.add(new DummyAddress("Frederiksberg Allé","20","2000","Frederiksberg C"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikskongehave","10","1600","Vejle"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikshavnsvej","168","1780","Hillerød"));
		dummyAddressSearchHits.add(new DummyAddress("Frederikssøndervej","178","2100","Hellerup"));
		return dummyAddressSearchHits;
	}
}
