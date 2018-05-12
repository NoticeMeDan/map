package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.view.MapInfo;
import io.vavr.control.Try;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.Getter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Each FavoritePoiCell has its own FavoritePoiCellController that manages the cell.
 */
public class MapCellController {
	@Getter
	@FXML Pane cell;
	@FXML Label mapNameLabel;
	@FXML Label mapCreatedLabel;
	@FXML Label mapSizeLabel;


	public MapCellController() {
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

	public void setInformation(MapInfo map) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		mapNameLabel.setText(map.getName());
		mapCreatedLabel.setText(dateFormatter.format(map.getDate()));
		mapSizeLabel.setText("Størrelse: " + map.getSize() + " MB");
	}
}
