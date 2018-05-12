package com.noticemedan.mappr.viewmodel.poi;

import com.noticemedan.mappr.model.map.Element;
import javafx.scene.control.ListCell;

/**
 * FavoritePoiCell is used to define a custom layout for each cell in the
 * FavoritePoListView.
 */
public class FavoritePoiCell extends ListCell<Element> {

	@Override
	public void updateItem(Element favoritePoi, boolean empty) {
		super.updateItem(favoritePoi, empty);
		if (empty) setGraphic(null);
		else {
			FavoritePoiCellController favoritePoiCellController = new FavoritePoiCellController();
			favoritePoiCellController.setInformation(favoritePoi);
			setGraphic(favoritePoiCellController.getCell());
		}
	}
}
