package com.noticemedan.mappr.viewmodel;

import com.google.inject.Inject;
import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.model.map.Address;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	DomainFacade domain;
	//@Setter
	ObservableList<Address> addressSearchResults; //TODO: @emil this comes from address parser.

	@Inject
	public SearchPaneController(DomainFacade domainFacade) {
		this.domain = domainFacade;
	}

	public void initialize() {
		closeSearchPane();
		eventListeners();
	}

	private void eventListeners() {
		searchPaneCloseButton.setOnAction(event -> closeSearchPane());
		searchAddressField.setOnAction(event -> this.addressSearchResultsListView.setItems(FXCollections.observableArrayList(this.domain.searchAddress(searchAddressField.getCharacters().toString()).toJavaList())));
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
