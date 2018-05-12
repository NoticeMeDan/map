package com.noticemedan.mappr.viewmodel;

import com.google.inject.Inject;
import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.model.NavigationAction;
import com.noticemedan.mappr.model.map.Address;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.pathfinding.TravelType;
import com.noticemedan.mappr.model.util.Coordinate;
import io.vavr.collection.List;
import io.vavr.collection.Vector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
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
	private DomainFacade domain;
	private Address startAddress;
	private Address endAddress;
	@Getter
	private Vector<Shape> djik;

	@Setter
	MainViewController mainViewController;

	ObservableList<NavigationInstruction> navigationInstructions;

	@Inject
	public RoutePaneController(DomainFacade domainFacade) {
		this.domain = domainFacade;
	}

	public void initialize() {
		//dummyNavigationInstructions();
		//navigationInstructionsListView.setItems(navigationInstructions);
		//navigationInstructionsListView.setCellFactory(listView -> new NavigationInstructionCell());
		closeRoutePane();
		toggleListView("search-start");
		eventListeners();
	}

	private void eventListeners() {
		routePaneCloseButton.setOnAction(event -> {
			mainViewController.pushCanvas();
			closeRoutePane();
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

		routeEndSearchResultsListView.getSelectionModel()
				.selectedItemProperty()
				.addListener((obs, oldVal, newVal) -> {
					searchEndPointAddressField.setText(newVal.toString());
					this.endAddress = this.domain.getAddress(newVal.toString());
					search();
				});
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
		Element startElement = this.domain.doNearestNeighborNewRangeSearch(this.startAddress.getCoordinate(), TravelType.ALL);
		Element endElement = this.domain.doNearestNeighborNewRangeSearch(this.endAddress.getCoordinate(), TravelType.ALL);
		Coordinate startCoordinate = startElement.getAvgPoint();
		Coordinate endCoordinate = endElement.getAvgPoint();

		this.djik = this.domain.deriveShortestPathShapes(startCoordinate, endCoordinate, TravelType.ALL);
		MainViewController.getCanvas().drawShortestPath(this.djik);
		System.out.println("hey");
	}

	public void openRoutePane() {
		routePane.setVisible(true);
		routePane.setManaged(true);
		searchStartPointAddressField.requestFocus();
	}

	public void toggleListView(String type) {
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
	}

	private void toggleRoute(boolean toggle) {
		navigationInstructionsListView.setVisible(toggle);
		navigationInstructionsListView.setManaged(toggle);
		routeInfoPane.setVisible(toggle);
		routeInfoPane.setManaged(toggle);
		if (!toggle) navigationInstructionsListView.getItems().clear();
	}

	private void toggleAddressStartSearchResults(boolean toggle) {
		routeStartSearchResultsListView.setVisible(toggle);
		routeStartSearchResultsListView.setManaged(toggle);
		if (!toggle) routeStartSearchResultsListView.getItems().clear();
	}

	private void toggleAddressEndSearchResults(boolean toggle) {
		routeEndSearchResultsListView.setVisible(toggle);
		routeEndSearchResultsListView.setManaged(toggle);
		if (!toggle) routeEndSearchResultsListView.getItems().clear();
	}

	// TODO @emil delete this inner class when proper class has been implemented
	@AllArgsConstructor
	public class NavigationInstruction {
		@Getter
		NavigationAction type;
		@Getter
		Double distance; //TODO @emil is this distance universally in meters or km? Or should we have a field for this? A double?
		@Getter
		String road;
		@Getter
		int roundAbout;
		// Coordinate is needed as well!
	}

	//TODO: Delete this when proper instruction class has been implemented.
	private void dummyNavigationInstructions() {
		ArrayList<NavigationInstruction> instructions = new ArrayList<>();
		instructions.add(new NavigationInstruction(NavigationAction.TURN_LEFT, 0.3, "Madvigsens Allé", 0));
		instructions.add(new NavigationInstruction(NavigationAction.ROUNDABOUT, 22.3, "Gaardagsvej", 1));
		instructions.add(new NavigationInstruction(NavigationAction.TURN_RIGHT, 2.5, "Holbæk motorvejen", 0));
		instructions.add(new NavigationInstruction(NavigationAction.STRAIGHT, 30.3, "Vigerslev gade", 0));
		instructions.add(new NavigationInstruction(NavigationAction.ROUNDABOUT, 0.76, "Jakobsens Allé", 3));
		instructions.add(new NavigationInstruction(NavigationAction.TURN_LEFT, 0.344, "Gunnersvanget", 0));
		instructions.add(new NavigationInstruction(NavigationAction.DESTINATION, 1.3, "Madvigsvej", 0));
		navigationInstructions = FXCollections.observableArrayList();
		navigationInstructions.addAll(instructions);
	}
}
