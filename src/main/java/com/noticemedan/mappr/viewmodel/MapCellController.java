package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.model.map.FileInfo;
import io.vavr.control.Try;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.Getter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;

/**
 * Each FavoritePoiCell has its own FavoritePoiCellController that manages the cell.
 */
class MapCellController {
	@Getter
	@FXML Pane cell;
	@FXML Label mapNameLabel;
	@FXML Label mapCreatedLabel;
	@FXML Label mapSizeLabel;


	MapCellController() {
		initialiseCell();
		setFXMLNodes();
	}

	private void setFXMLNodes() {
		//The @FXML label doesn't work with other tags than the root node. So this is necessary.
		mapNameLabel = (Label) cell.getChildren().get(0);
		mapCreatedLabel = (Label) cell.getChildren().get(1);
		mapSizeLabel = (Label) cell.getChildren().get(2);
	}

	private void initialiseCell() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MapCell.fxml"));
		fxmlLoader.setController(this);
		Try.of(fxmlLoader::load);
	}

	void setInformation(FileInfo map) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		NumberFormat numberFormatter = new DecimalFormat("#.#");
		double SIZE_MB = Math.pow(1024, 2);
		mapNameLabel.setText(map.getDisplayName());
		mapCreatedLabel.setText(dateFormatter.format(map.getLastEdited()));
		mapSizeLabel.setText("Størrelse: " + numberFormatter.format(map.getSize()/SIZE_MB) + " MB");
	}
}
