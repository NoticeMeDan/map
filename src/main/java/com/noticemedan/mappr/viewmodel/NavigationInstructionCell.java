package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.model.directions.NavigationInstruction;
import javafx.scene.control.ListCell;

/**
 * NavigationInstructionCell is used to define a custom layout for each cell in the
 * NavigationInstructionsListView.
 */
public class NavigationInstructionCell extends ListCell<NavigationInstruction> {

	@Override
	public void updateItem(NavigationInstruction navigationInstruction, boolean empty) {
		super.updateItem(navigationInstruction, empty);
		if (empty) setGraphic(null);
		else {
			NavigationInstructionCellController navigationInstructionCellController = new NavigationInstructionCellController(navigationInstruction);
			setGraphic(navigationInstructionCellController.getCell());
		}
	}
}
