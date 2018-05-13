package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.model.map.FileInfo;
import javafx.scene.control.ListCell;

/**
 * MapCell is used to define a custom layout for each cell in the
 * mapListView.
 */
public class MapCell extends ListCell<FileInfo> {
	@Override
	public void updateItem(FileInfo map, boolean empty) {
		super.updateItem(map, empty);
		if (empty) setGraphic(null);
		else {
			MapCellController mapCellController = new MapCellController();
			mapCellController.setInformation(map);
			setGraphic(mapCellController.getCell());
		}
	}
}
