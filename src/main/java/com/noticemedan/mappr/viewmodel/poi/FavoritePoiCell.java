package com.noticemedan.mappr.viewmodel.poi;

import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.user.FavoritePoi;
import javafx.scene.control.ListCell;

/**
 * FavoritePoiCell is used to define a custom layout for each cell in the
 * FavoritePoListView.
 */
public class FavoritePoiCell extends ListCell<FavoritePoi> {

	@Override
	public void updateItem(FavoritePoi favoritePoi, boolean empty) {
		super.updateItem(favoritePoi, empty);
		if (empty) setGraphic(null);
		else {
			FavoritePoiCellController favoritePoiCellController = new FavoritePoiCellController();
			favoritePoiCellController.setInformation(favoritePoi);
			setGraphic(favoritePoiCellController.getCell());
		}
	}
}
