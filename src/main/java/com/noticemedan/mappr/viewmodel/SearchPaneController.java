package com.noticemedan.mappr.viewmodel;

import com.google.inject.Inject;
import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.model.map.Address;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.util.Coordinate;
import io.vavr.collection.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.geom.Point2D;


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
		ChangeListener<String> searchAddressListener =
				(ObservableValue<? extends String> observable, String oldValue, String newValue) -> zoomToAddress();

		addressSearchResultsListView.getSelectionModel().selectedItemProperty().addListener(searchAddressListener);

		searchPaneCloseButton.setOnAction(event -> {
			closeSearchPane();
			mainViewController.pushCanvas();
		});

		searchAddressField.setOnKeyTyped(event -> handleAddressSearch());
	}

	private void zoomToAddress() {
		String currentSelectedAddressString = (String) addressSearchResultsListView.getSelectionModel().getSelectedItem();
		if (currentSelectedAddressString != null) {
			Address currentSelectedAddress = this.domain.getAddress(currentSelectedAddressString);

			mainViewController.getCanvas().zoomToCoordinate(
					new Coordinate(currentSelectedAddress.getCoordinate().getX(),
							Coordinate.canvasLatToLat(currentSelectedAddress.getCoordinate().getY())), 30);
			mainViewController.getCanvas().setPointerPosition(currentSelectedAddress.getCoordinate());
			mainViewController.getCanvas().repaint();
		}
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
		if (search.isEmpty()) addressSearchResultsListView.getItems().clear();
		else {
			List<String> results = this.domain.doAddressSearch(search).take(20);
			addressSearchResultsListView.setItems(
					FXCollections.observableArrayList(results.toJavaList())
			);
		}
	}
}
