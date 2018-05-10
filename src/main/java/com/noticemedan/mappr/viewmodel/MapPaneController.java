package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.view.MapInfo;
import com.noticemedan.mappr.view.util.FilePicker;
import com.noticemedan.mappr.view.util.InfoBox;
import io.vavr.control.Option;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.ArrayList;

public class MapPaneController {
	@FXML Pane mapPane;
	@FXML ListView mapListView;
	@Getter
	@FXML Button mapPaneCloseButton;
	@FXML Button loadMapButton;
	@FXML Button deleteMapButton;
	@FXML Button createMapButton;
	@FXML StackPane noMapsYetPane;

	@Setter
	ObservableList<MapInfo> maps;
	@Setter
	MainViewController mainViewController;
	DomainFacade domain;

	@Inject
	public MapPaneController(DomainFacade domainFacade) { this.domain = domainFacade; }

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

		createMapButton.setOnAction(this::createMapFromOsm);
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

	private void createMapFromOsm(ActionEvent event) {
		Stage stage = (Stage) this.mapPane.getScene().getWindow();
		FilePicker picker = new FilePicker(new FileChooser
				.ExtensionFilter("OSM Files (*.osm or *.osm.zip)", "*.osm", "*.osm.zip"));

		Option<Path> path = picker.getPath(stage);
		if (!path.isEmpty()) {
			new InfoBox("Vi danner kortet i baggrunden - du vil få besked når det er færdigt.").show();
			InfoBox onComplete = new InfoBox("Kortet er nu oprettet, og du har muligheden for at tilgå det fra menuen.");
			InfoBox onFailed = new InfoBox("Der opsted en fejl under oprettelsen af kortet. Tilkald venligst dine nærmeste chimpanser.");
			domain.buildMapFromOsmPath(path.get(), x -> onComplete.show(), x -> onFailed.show());
		}
	}
}
