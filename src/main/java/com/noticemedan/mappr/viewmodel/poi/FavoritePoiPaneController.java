package com.noticemedan.mappr.viewmodel.poi;

import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.model.user.FavoritePoi;
import com.noticemedan.mappr.viewmodel.MainViewController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

public class FavoritePoiPaneController {
	@FXML Pane favoritePoiPane;
	@FXML ListView favoritePoiListView;
	@Getter
	@FXML Button favoritePoiPaneCloseButton;
	@FXML Button navigateToFavoritePoiButton;
	@FXML Button removeFavoritePoiButton;
	@FXML StackPane noFavoritesYetPane;
	@Setter
	MainViewController mainViewController;

	ObservableList<FavoritePoi> poi;
	DomainFacade domain;

	@Inject
	public FavoritePoiPaneController(DomainFacade domainFacade) {
		this.domain = domainFacade;
	}

	public void initialize() {
		closeFavoritePoiPane();
		readPoi();
		showNoFavoritesYetPane();
		disableActionMenu();
		eventListeners();
	}

	private void readPoi() {
		this.poi = FXCollections.observableArrayList(this.domain.getAllPoi().toJavaList());
		this.favoritePoiListView.setItems(this.poi);
		favoritePoiListView.setCellFactory(listView -> new FavoritePoiCell()); //Custom cells for list
		if (poi.size() == 0) {
			disableActionMenu();
			showNoFavoritesYetPane();
		} else {
			hideNoFavoritesYetPane();
		}
	}

	private void eventListeners() {
		ChangeListener<FavoritePoi> favoritePoiListener = (ObservableValue<? extends FavoritePoi> observable, FavoritePoi oldValue, FavoritePoi newValue) -> {
			zoomToPoi();
			enableActionMenu();
		};

		favoritePoiListView.getSelectionModel().selectedItemProperty().addListener(favoritePoiListener);

		removeFavoritePoiButton.setOnAction(event -> {
			FavoritePoi poi = (FavoritePoi) favoritePoiListView.getSelectionModel().getSelectedItem();
			this.domain.removePoi(poi);
			this.readPoi();
		});

		favoritePoiPaneCloseButton.setOnAction(event -> {
			closeFavoritePoiPane();
			mainViewController.pushCanvas();
		});
	}

	private void zoomToPoi() {
		FavoritePoi currentSelectedFavoritePoi = (FavoritePoi) favoritePoiListView.getSelectionModel().getSelectedItem();
		if (currentSelectedFavoritePoi != null) mainViewController.getCanvas().zoomToCoordinate(currentSelectedFavoritePoi.getCoordinate(), 30);
	}

	public void openFavoritePane() {
		readPoi();
		favoritePoiPane.setManaged(true);
		favoritePoiPane.setVisible(true);
	}

	private void closeFavoritePoiPane() {
		favoritePoiPane.setManaged(false);
		favoritePoiPane.setVisible(false);
	}

	private void disableActionMenu() {
		navigateToFavoritePoiButton.setDisable(true);
		removeFavoritePoiButton.setDisable(true);
		favoritePoiListView.refresh();
	}

	private void enableActionMenu() {
		navigateToFavoritePoiButton.setDisable(false);
		removeFavoritePoiButton.setDisable(false);
	}

	private void showNoFavoritesYetPane() {
		if (poi == null || poi.size() == 0 ) {
			noFavoritesYetPane.setVisible(true);
			noFavoritesYetPane.setManaged(true);
			favoritePoiListView.setVisible(false);
			favoritePoiListView.setManaged(false);
		}
	}

	private void hideNoFavoritesYetPane() {
		noFavoritesYetPane.setVisible(false);
		noFavoritesYetPane.setManaged(false);
		favoritePoiListView.setVisible(true);
		favoritePoiListView.setManaged(true);
	}
}
