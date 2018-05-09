package com.noticemedan.mappr.viewmodel;

import com.google.inject.Inject;
import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.model.map.Address;
import io.vavr.collection.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SearchPaneController {
	@FXML Pane searchPane;
	@FXML TextField searchAddressField;
	@FXML Button searchPaneCloseButton;
	@FXML ListView addressSearchResultsListView;
	private DomainFacade domain;

	@Setter
	MainViewController mainViewController;

	@Inject
	public SearchPaneController(DomainFacade domainFacade) {
		this.domain = domainFacade;
	}

	public void initialize() {
		closeSearchPane();
		eventListeners();
	}

	private void eventListeners() {
		searchPaneCloseButton.setOnAction(event -> {
			closeSearchPane();
			mainViewController.pushCanvas();
		});

		searchAddressField.setOnKeyTyped(event -> handleAddressSearch());
	}

	void openSearchPane() {
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

	private void handleAddressSearch() {
		String search = searchAddressField.getText();
		// Dont run empty queries (will get *all* addresses)
		if (search.isEmpty()) addressSearchResultsListView.getItems().removeAll(); // TODO: Find a way that actually clears the listview
		List<String> results = this.domain.doAddressSearch(search).take(20);
		addressSearchResultsListView.setItems(
			FXCollections.observableArrayList(results.toJavaList())
		);
	}
}
