package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.view.MapInfo;
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
import java.util.ArrayList;

public class MapPaneController {
	@FXML Pane mapPane;
	@FXML ListView mapListView;
	@Getter
	@FXML Button mapPaneCloseButton;
	@FXML Button loadMapButton;
	@FXML Button deleteMapButton;
	@FXML StackPane noMapsYetPane;

	@Setter
	ObservableList<MapInfo> maps;
	@Setter
	MainViewController mainViewController;

	public void initialize() {
		closeMapPane();

		//TODO @emil / @elias fjern!
		ArrayList test = new ArrayList();
		test.add(new MapInfo("denmark-latest.osm", 23.65));
		test.add(new MapInfo("denmark-latest.osm", 23.65));
		test.add(new MapInfo("denmark-latest.osm", 23.65));
		maps = FXCollections.observableArrayList(test);

		mapListView.setItems(maps);
		showNoMapsYetPane();
		disableActionMenu();
		mapListView.setCellFactory(listView -> new MapCell()); //Custom cells for list
		eventListeners();
	}

	private void eventListeners() {
		ChangeListener<MapInfo> favoritePoiListener = (ObservableValue<? extends MapInfo> observable, MapInfo oldValue, MapInfo newValue) -> enableActionMenu();

		mapListView.getSelectionModel().selectedItemProperty().addListener(favoritePoiListener);

		deleteMapButton.setOnAction(event -> {
			maps.remove(mapListView.getSelectionModel().getSelectedItem());
			if (maps.size() == 0) {
				disableActionMenu();
				showNoMapsYetPane();
			}
		});

		mapPaneCloseButton.setOnAction(event -> closeMapPane());

	}

	public void openMapPane() {
		refresh();
		mapPane.setManaged(true);
		mapPane.setVisible(true);
	}

	private void closeMapPane() {
		mapPane.setManaged(false);
		mapPane.setVisible(false);
	}

	private void disableActionMenu() {
		loadMapButton.setDisable(true);
		deleteMapButton.setDisable(true);
		mapListView.refresh();
	}

	private void enableActionMenu() {
		loadMapButton.setDisable(false);
		deleteMapButton.setDisable(false);
	}

	private void showNoMapsYetPane() {
		if (maps == null || maps.size() == 0 ) {
			noMapsYetPane.setVisible(true);
			noMapsYetPane.setManaged(true);
			mapListView.setVisible(false);
			mapListView.setManaged(false);
		}
	}

	private void hideNoMapsYetPane() {
		noMapsYetPane.setVisible(false);
		noMapsYetPane.setManaged(false);
		mapListView.setVisible(true);
		mapListView.setManaged(true);
	}

	private void refresh() {
		mapListView.setItems(maps);
		if (maps.size() > 0) hideNoMapsYetPane();
	}
}
