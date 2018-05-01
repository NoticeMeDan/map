package com.noticemedan.mappr.viewmodel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class SearchPaneController {
	@FXML Pane searchPane;
	@FXML TextField searchAddressField;
	@FXML Button searchPaneCloseButton;
	@FXML ListView addressSearchResultsListView;
	//@Setter
	//ObservableList<Address> addressSearchResults; //TODO: @emil this comes from address parser.

	public void initialize() {
		closeSearchPane();
		eventListeners();
	}

	private void eventListeners() {
		searchPaneCloseButton.setOnAction(event -> closeSearchPane());
	}

	public void openSearchPane() {
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
}
