package com.noticemedan.map.view;

import com.noticemedan.map.model.user.FavoritePoi;
import javafx.scene.control.ListCell;

/**
 * FavoritePoiCell is used to define a custom layout for each cell in the
 * FavoritePOIListView.
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
