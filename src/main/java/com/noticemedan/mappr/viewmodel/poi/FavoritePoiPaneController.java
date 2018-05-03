package com.noticemedan.mappr.viewmodel.poi;

import com.google.inject.Inject;
import com.noticemedan.mappr.model.user.FavoritePoi;
import com.noticemedan.mappr.viewmodel.CanvasView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;

public class FavoritePoiPaneController {
	@FXML Pane favoritePoiPane;
	@FXML ListView favoritePoiListView;
	@Getter
	@FXML Button favoritePoiPaneCloseButton;
	@FXML Button navigateToFavoritePoiButton;
	@FXML Button removeFavoritePoiButton;
	@FXML StackPane noFavoritesYetPane;
	@Setter
	ObservableList<FavoritePoi> favoritePois;
	CanvasView canvas;

	@Inject
	public FavoritePoiPaneController(CanvasView canvas) { this.canvas = canvas; }

	public void initialize() {
		closeFavoritePoiPane();
		favoritePoiListView.setItems(favoritePois);
		showNoFavoritesYetPane();
		disableActionMenu();
		favoritePoiListView.setCellFactory(listView -> new FavoritePoiCell()); //Custom cells for list
		eventListeners();
	}

	private void eventListeners() {
		favoritePoiListView.setOnMousePressed(event -> enableActionMenu());

		removeFavoritePoiButton.setOnAction(event -> {
			favoritePois.remove(favoritePoiListView.getSelectionModel().getSelectedItem());
			if (favoritePois.size() == 0) {
				disableActionMenu();
				showNoFavoritesYetPane();
			}
		});

		favoritePoiPaneCloseButton.setOnAction(event -> closeFavoritePoiPane());
	}

	public void openFavoritePane() {
		refresh();
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
		if (favoritePois == null || favoritePois.size() == 0 ) {
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

	private void refresh() {
		favoritePoiListView.setItems(favoritePois);
		if (favoritePois.size() > 0) hideNoFavoritesYetPane();
	}
}
