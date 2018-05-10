package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.model.NavigationAction;
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

import java.util.ArrayList;

public class RoutePaneController {
	@FXML Pane routePane;
	@FXML Button routePaneCloseButton;
	@FXML ToggleButton routeCarToggleButton;
	@FXML ToggleButton routeWalkToggleButton;
	@FXML ToggleButton routeBicycleToggleButton;
	@FXML TextField searchStartPointAddressField;
	@FXML TextField searchEndPointAddressField;
	@FXML ListView routeSearchResultsListView;
	@FXML ListView navigationInstructionsListView;

	@Setter
	MainViewController mainViewController;

	ObservableList<NavigationInstruction> navigationInstructions;

	public void initialize() {
		dummyNavigationInstructions();
		navigationInstructionsListView.setItems(navigationInstructions);
		navigationInstructionsListView.setCellFactory(listView -> new NavigationInstructionCell());
		closeRoutePane();
		hideAddressSearchResults();
		eventListeners();
	}

	private void eventListeners() {
		routePaneCloseButton.setOnAction(event -> {
			mainViewController.pushCanvas();
			closeRoutePane();
		});
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

	private void hideAddressSearchResults() {
		routeSearchResultsListView.setVisible(false);
		routeSearchResultsListView.setManaged(false);
		routeSearchResultsListView.getItems().clear();
	}

	private void showAddressSearchResults() {
		routeSearchResultsListView.setVisible(true);
		routeSearchResultsListView.setManaged(true);
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
