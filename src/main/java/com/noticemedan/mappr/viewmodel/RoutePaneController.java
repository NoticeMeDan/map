package com.noticemedan.mappr.viewmodel;

import com.google.inject.Inject;
import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.model.directions.NavigationInstruction;
import com.noticemedan.mappr.model.map.Address;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.pathfinding.ShortestPath;
import com.noticemedan.mappr.model.pathfinding.TravelType;
import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.view.util.InfoBox;
import io.vavr.collection.List;
import io.vavr.collection.Vector;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.Setter;

import java.awt.*;

public class RoutePaneController {
	@FXML Pane routePane;
	@FXML Pane routeInfoPane;
	@FXML Button routePaneCloseButton;
	@FXML ToggleButton routeCarToggleButton;
	@FXML ToggleButton routeWalkToggleButton;
	@FXML ToggleButton routeBicycleToggleButton;
	@FXML TextField searchStartPointAddressField;
	@FXML TextField searchEndPointAddressField;
	@FXML ListView routeStartSearchResultsListView;
	@FXML ListView routeEndSearchResultsListView;
	@FXML ListView navigationInstructionsListView;
	@FXML Label estimatedTimeLabel;
	@FXML Label estimatedDistanceLabel;
	private DomainFacade domain;
	private Address startAddress;
	private Address endAddress;
	private TravelType chosenTravelType = TravelType.ALL;

	@Setter
	MainViewController mainViewController;
	ObservableList<NavigationInstruction> navigationInstructions;

	@Inject
	public RoutePaneController(DomainFacade domainFacade) {
		this.domain = domainFacade;
	}

	public void initialize() {
		closeRoutePane();
		eventListeners();
	}

	private void eventListeners() {
		routePaneCloseButton.setOnAction(event -> {
			mainViewController.pushCanvas();
			closeRoutePane();
			MainViewController.getCanvas().hidePath();
		});

		ChangeListener<NavigationInstruction> navigationInstructionListener =
				(ObservableValue<? extends NavigationInstruction> observable, NavigationInstruction oldValue, NavigationInstruction newValue) -> zoomToAddress();
		navigationInstructionsListView.getSelectionModel().selectedItemProperty().addListener(navigationInstructionListener);

		// On Key Press
		searchStartPointAddressField.setOnKeyTyped(event -> handleAddressSearch(searchStartPointAddressField, routeStartSearchResultsListView));
		searchEndPointAddressField.setOnKeyTyped(event -> handleAddressSearch(searchEndPointAddressField, routeEndSearchResultsListView));

		// On focus
		searchStartPointAddressField.focusedProperty()
				.addListener((obs, oldVal, newVal) -> toggleListView("search-start"));

		searchEndPointAddressField.focusedProperty()
				.addListener((obs, oldVal, newVal) -> toggleListView("search-end"));

		// Choose item
		routeStartSearchResultsListView.getSelectionModel()
				.selectedItemProperty()
				.addListener((obs, oldVal, newVal) -> {
					searchStartPointAddressField.setText(newVal.toString());
					this.startAddress = this.domain.getAddress(newVal.toString());
				});

		// Choose travel type
		routeWalkToggleButton.setOnMouseClicked(e -> setTravelType(TravelType.WALK));
		routeBicycleToggleButton.setOnMouseClicked(e -> setTravelType(TravelType.BIKE));
		routeCarToggleButton.setOnMouseClicked(e -> setTravelType(TravelType.CAR));

		routeEndSearchResultsListView.getSelectionModel()
				.selectedItemProperty()
				.addListener((obs, oldVal, newVal) -> {
					searchEndPointAddressField.setText(newVal.toString());
					this.endAddress = this.domain.getAddress(newVal.toString());
				});
	}

	private void zoomToAddress() {
		NavigationInstruction currentSelectedInstruction = (NavigationInstruction) navigationInstructionsListView.getSelectionModel().getSelectedItem();
		if (currentSelectedInstruction != null) {
			mainViewController.getCanvas().zoomToCoordinate(
					new Coordinate(currentSelectedInstruction.getCoordinate().getX(),
							Coordinate.canvasLatToLat(currentSelectedInstruction.getCoordinate().getY())), 30);
			mainViewController.getCanvas().setPointerPosition(currentSelectedInstruction.getCoordinate());
			mainViewController.getCanvas().repaint();
		}
	}

	private void setTravelType(TravelType type) {
		if (this.chosenTravelType == type) this.chosenTravelType = TravelType.ALL;
		else this.chosenTravelType = type;
		search();
	}

	private void handleAddressSearch(TextField field, ListView resultList) {
		String search = field.getText();
		if (search.isEmpty()) resultList.getItems().clear();
		else {
			List<String> results = this.domain.doAddressSearch(search).take(20);
			resultList.setItems(
					FXCollections.observableArrayList(results.toJavaList())
			);
		}
	}

	public void search() {
		if (startAddress == null || endAddress == null) return;
		Element startElement = this.domain.doNearestNeighborUsingRangeSearch(this.startAddress.getCoordinate(), this.chosenTravelType, Double.POSITIVE_INFINITY);
		Element endElement = this.domain.doNearestNeighborUsingRangeSearch(this.endAddress.getCoordinate(), this.chosenTravelType, Double.POSITIVE_INFINITY);
		Coordinate startCoordinate = startElement.getAvgPoint();
		Coordinate endCoordinate = endElement.getAvgPoint();

		ShortestPath shortestPath = this.domain.deriveShortestPath(startCoordinate, endCoordinate, this.chosenTravelType);

		if (shortestPath.getShortestPathShapes().length() == 0) {
			showInfoMessage("Der kunne desværre ikke findes en rute.\nPrøv en anden transport form.");
		}

		mainViewController.getCanvas().zoomToRoute(
				new Coordinate(
						endCoordinate.getX(),
						Coordinate.canvasLatToLat(endCoordinate.getY())),
				new Coordinate(
						startCoordinate.getX(),
						Coordinate.canvasLatToLat(startCoordinate.getY()))
		);

		Vector<Shape> shortestPathShapes = shortestPath.getShortestPathShapes();
		MainViewController.getCanvas().showPath(shortestPathShapes);
		MainViewController.getCanvas().repaint();
		toggleListView("route");
		setNavigationInstructions(shortestPath.getTravelInstructions());
		estimatedTimeLabel.setText(shortestPath.getTimeToTravel());
		estimatedDistanceLabel.setText(shortestPath.getDistanceToTravel());
	}

	public void openRoutePane() {
		routePane.setVisible(true);
		routePane.setManaged(true);
		searchStartPointAddressField.requestFocus();
	}

	private void toggleListView(String type) {
		if (type.equals("route")) {
			toggleRoute(true);
			toggleAddressStartSearchResults(false);
			toggleAddressEndSearchResults(false);
		}
		if (type.equals("search-start")) {
			toggleRoute(false);
			toggleAddressStartSearchResults(true);
			toggleAddressEndSearchResults(false);
		}
		if (type.equals("search-end")) {
			toggleRoute(false);
			toggleAddressStartSearchResults(false);
			toggleAddressEndSearchResults(true);
		}
	}

	private void closeRoutePane() {
		routePane.setVisible(false);
		routePane.setManaged(false);
		searchStartPointAddressField.clear();
		searchEndPointAddressField.clear();
		routeStartSearchResultsListView.getItems().clear();
		navigationInstructionsListView.getItems().clear();
		routeWalkToggleButton.setSelected(false);
		routeBicycleToggleButton.setSelected(false);
		routeCarToggleButton.setSelected(false);
	}

	private void toggleRoute(boolean toggle) {
		navigationInstructionsListView.setVisible(toggle);
		navigationInstructionsListView.setManaged(toggle);
		routeInfoPane.setVisible(toggle);
		routeInfoPane.setManaged(toggle);
	}

	private void setNavigationInstructions(Vector<NavigationInstruction> instructions) {
		navigationInstructionsListView.getItems().clear();
		if (navigationInstructions != null) navigationInstructions.clear();
		else navigationInstructions = FXCollections.observableArrayList();
		instructions.forEach(i -> navigationInstructions.add(i));
		navigationInstructionsListView.setItems(navigationInstructions);
		navigationInstructionsListView.setCellFactory(listView -> new NavigationInstructionCell());
	}

	private void toggleAddressStartSearchResults(boolean toggle) {
		routeStartSearchResultsListView.setVisible(toggle);
		routeStartSearchResultsListView.setManaged(toggle);
	}

	private void toggleAddressEndSearchResults(boolean toggle) {
		routeEndSearchResultsListView.setVisible(toggle);
		routeEndSearchResultsListView.setManaged(toggle);
	}

	private void showInfoMessage(String message) {
		InfoBox infoBox = new InfoBox(message);
		infoBox.show();
	}
}
