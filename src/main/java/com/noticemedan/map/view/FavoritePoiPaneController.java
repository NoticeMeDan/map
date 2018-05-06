package com.noticemedan.map.view;

import com.noticemedan.map.model.user.FavoritePoi;
import com.noticemedan.map.model.utilities.Coordinate;
import com.noticemedan.map.viewmodel.CanvasView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	@Setter
	CanvasView canvas;
	@Setter
	MainViewController mainViewController;


	public void initialize() {
		closeFavoritePoiPane();
		favoritePoiListView.setItems(favoritePois);
		showNoFavoritesYetPane();
		disableActionMenu();
		favoritePoiListView.setCellFactory(listView -> new FavoritePoiCell()); //Custom cells for list
		eventListeners();
	}

	private void eventListeners() {
		ChangeListener<FavoritePoi> favoritePoiListener = (ObservableValue<? extends FavoritePoi> observable, FavoritePoi oldValue, FavoritePoi newValue) -> {
			zoomToPoi();
			enableActionMenu();
		};
		favoritePoiListView.getSelectionModel().selectedItemProperty().addListener(favoritePoiListener);

		removeFavoritePoiButton.setOnAction(event -> {
			favoritePois.remove(favoritePoiListView.getSelectionModel().getSelectedItem());
			if (favoritePois.size() == 0) {
				disableActionMenu();
				showNoFavoritesYetPane();
			}
		});

		favoritePoiPaneCloseButton.setOnAction(event -> {
			closeFavoritePoiPane();
			mainViewController.pushCanvas();
		});
	}

	private void zoomToPoi() {
		FavoritePoi currentSelectedFavoritePoi = (FavoritePoi) favoritePoiListView.getSelectionModel().getSelectedItem();
		canvas.zoomToCoordinate(currentSelectedFavoritePoi.getCoordinate(), 30);
	}

	void openFavoritePane() {
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
