package com.noticemedan.map.view;

import javafx.scene.control.ListCell;

/**
 * NavigationInstructionCell is used to define a custom layout for each cell in the
 * NavigationInstructionsListView.
 */
public class NavigationInstructionCell extends ListCell<RoutePaneController.NavigationInstruction> {

	@Override
	public void updateItem(RoutePaneController.NavigationInstruction navigationInstruction, boolean empty) {
		super.updateItem(navigationInstruction, empty);
		if (empty) setGraphic(null);
		else {
			NavigationInstructionCellController navigationInstructionCellController = new NavigationInstructionCellController(navigationInstruction);
			setGraphic(navigationInstructionCellController.getCell());
		}
	}
}
