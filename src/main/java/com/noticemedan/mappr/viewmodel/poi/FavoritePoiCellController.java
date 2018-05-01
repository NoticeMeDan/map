package com.noticemedan.mappr.viewmodel.poi;

import com.noticemedan.mappr.model.user.FavoritePoi;
import com.noticemedan.mappr.model.util.Coordinate;
import io.vavr.control.Try;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.Getter;

/**
 * Each FavoritePoiCell has its own FavoritePoiCellController that manages the cell.
 */
public class FavoritePoiCellController {
	@Getter
	@FXML Pane cell;
	@FXML Label favoritePoiNameLabel;
	@FXML Label favoritePoiLatLonLabel;

	public FavoritePoiCellController() {
		initialiseCell();
		setFXMLNodes();
	}

	private void setFXMLNodes() {
		//The @FXML label doesn't work with other tags than the root node. So this is necessary.
		favoritePoiNameLabel = (Label) cell.getChildren().get(0);
		favoritePoiLatLonLabel = (Label) cell.getChildren().get(1);
	}

	private void initialiseCell() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/FavoritePoiCell.fxml"));
		fxmlLoader.setController(this);
		//TODO: @emil ask @elias about this part
		Try.of(fxmlLoader::load);
	}

	public void setInformation(FavoritePoi favoritePoi) {
		favoritePoiNameLabel.setText(favoritePoi.getName());
		Coordinate coordinate = favoritePoi.getCoordinate();

		String shortCoordinateX = String.valueOf(coordinate.getX());
		shortCoordinateX = shortCoordinateX.substring(0,9);

		String shortCoordinateY = String.valueOf(coordinate.getY());
		shortCoordinateY = shortCoordinateY.substring(0,10); //TODO 9 @emil when not canvas coords.

		favoritePoiLatLonLabel.setText(shortCoordinateX + ", " + shortCoordinateY);
	}
}
