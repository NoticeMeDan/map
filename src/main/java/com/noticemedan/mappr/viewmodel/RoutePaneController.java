package com.noticemedan.mappr.viewmodel;

import com.google.inject.Inject;
import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.model.NavigationAction;
import com.noticemedan.mappr.model.directions.Guide;
import com.noticemedan.mappr.model.directions.NavigationInstruction;
import com.noticemedan.mappr.model.map.Address;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.pathfinding.ShortestPath;
import com.noticemedan.mappr.model.pathfinding.TravelType;
import com.noticemedan.mappr.model.util.Coordinate;
import io.vavr.collection.List;
import io.vavr.collection.Vector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;

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
	private TravelType chosenTravelType = TravelType.CAR;

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
			this.mainViewController.getCanvas().hidePath();
		});

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
		Element startElement = this.domain.doNearestNeighborNewRangeSearch(this.startAddress.getCoordinate(), this.chosenTravelType);
		Element endElement = this.domain.doNearestNeighborNewRangeSearch(this.endAddress.getCoordinate(), this.chosenTravelType);
		Coordinate startCoordinate = startElement.getAvgPoint();
		Coordinate endCoordinate = endElement.getAvgPoint();

		ShortestPath shortestPath = this.domain.deriveShortestPathShapes(startCoordinate, endCoordinate, this.chosenTravelType);

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
}
