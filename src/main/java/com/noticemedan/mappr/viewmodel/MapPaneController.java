package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.model.map.FileInfo;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.nio.file.Path;

public class MapPaneController {
	@FXML Pane mapPane;
	@FXML ListView mapListView;
	@Getter @FXML Button mapPaneCloseButton;
	@FXML Button createMapButton;
	@FXML Button loadMapButton;
	@FXML Button saveMapButton;
	@FXML Button deleteMapButton;
	@FXML StackPane noMapsYetPane;

	ObservableList<FileInfo> maps;
	@Setter
	MainViewController mainViewController;
	private DomainFacade domain;

	@Inject
	public MapPaneController(DomainFacade domainFacade) { this.domain = domainFacade; }

	public void initialize() {
		closeMapPane();
		readFiles();
		disableActionMenu();
		eventListeners();
	}

	private void readFiles() {
		this.maps = FXCollections.observableArrayList(this.domain.getAllFileInfoFromMapprDir().toJavaList());
		mapListView.setItems(maps);
		mapListView.setCellFactory(listView -> new MapCell()); //Custom cells for list
		handleNoMaps();
	}

	private void eventListeners() {
		ChangeListener<FileInfo> favoritePoiListener = (ObservableValue<? extends FileInfo> observable, FileInfo oldValue, FileInfo newValue) -> enableActionMenu();

		mapListView.getSelectionModel().selectedItemProperty().addListener(favoritePoiListener);

		createMapButton.setOnAction(this::createMapFromOsm);
		loadMapButton.setOnAction(this::loadMap);
		saveMapButton.setOnAction(this::updateMap);
		deleteMapButton.setOnAction(this::deleteMap);

		mapPaneCloseButton.setOnAction(event -> {
			closeMapPane();
			mainViewController.pushCanvas();
		});
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

	private void handleNoMaps() {
		if (maps == null || maps.size() == 0 ) {
			showNoMapsYetPane();
			enableActionMenu();
		} else {
			hideNoMapsYetPane();
			disableActionMenu();
		}
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
		noMapsYetPane.setVisible(true);
		noMapsYetPane.setManaged(true);
		mapListView.setVisible(false);
		mapListView.setManaged(false);
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
				.ExtensionFilter("OSM Filer (*.osm or *.zip)", "*.osm", "*.zip"));

		Option<Path> path = picker.getPath(stage);
		if (!path.isEmpty()) {
			new InfoBox("Vi danner kortet i baggrunden - du vil få besked når det er færdigt.").show();
			this.mainViewController.toggleLoadingMessage();
			InfoBox onComplete = new InfoBox("Kortet er nu oprettet, og du har muligheden for at tilgå det fra menuen.");
			InfoBox onFailed = new InfoBox("Der opsted en fejl under oprettelsen af kortet. Tilkald venligst dine nærmeste chimpanser.");
			domain.buildMapFromOsmPath(path.get(), x -> {
				onComplete.show();
				this.readFiles();
				this.mainViewController.toggleLoadingMessage();
			}, x -> onFailed.show());
		}
	}

	private void loadMap(ActionEvent event) {
		FileInfo map = (FileInfo) mapListView.getSelectionModel().getSelectedItem();
		this.domain.loadMap(map.getFileName());
		this.mainViewController.centerViewport();
		MainViewController.getCanvas().repaint();
	}

	private void updateMap(ActionEvent event) {
		FileInfo map = (FileInfo) mapListView.getSelectionModel().getSelectedItem();
		this.domain.updateMap(map.getFileName());
	}

	private void deleteMap(ActionEvent event) {
		FileInfo map = (FileInfo) mapListView.getSelectionModel().getSelectedItem();
		this.domain.deleteMap(map.getFileName());
		readFiles();
	}
}
