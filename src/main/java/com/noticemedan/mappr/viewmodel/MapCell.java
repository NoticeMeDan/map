package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.view.MapInfo;
import javafx.scene.control.ListCell;

/**
 * MapCell is used to define a custom layout for each cell in the
 * mapListView.
 */
public class MapCell extends ListCell<MapInfo> {
	@Override
	public void updateItem(MapInfo map, boolean empty) {
		super.updateItem(map, empty);
		if (empty) setGraphic(null);
		else {
			MapCellController mapCellController = new MapCellController();
			mapCellController.setInformation(map);
			setGraphic(mapCellController.getCell());
		}
	}
}
