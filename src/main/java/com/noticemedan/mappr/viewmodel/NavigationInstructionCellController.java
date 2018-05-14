package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.model.NavigationAction;
import com.noticemedan.mappr.model.directions.NavigationInstruction;
import com.noticemedan.mappr.model.util.TextFormatter;
import io.vavr.control.Try;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import lombok.Getter;

public class 	NavigationInstructionCellController {

	@FXML
	@Getter Pane cell;
	@FXML Pane navigationActionImageView;
	@FXML Label distanceToNextNavigationAction;
	@FXML Text navigationActionDescription;

	private NavigationInstruction navigationInstruction; // The object that this cell represents

	public NavigationInstructionCellController(NavigationInstruction navigationInstruction) {
		this.navigationInstruction = navigationInstruction;
		initialiseCell();
		setInformation();
	}

	private void initialiseCell() {
		FXMLLoader fxmlLoader = new FXMLLoader(NavigationInstructionCellController.class.getResource("/fxml/NavigationInstructionCell.fxml"));
		fxmlLoader.setController(this);
		Try.of(fxmlLoader::load);
	}

	private void setInformation() {
		setDistance();
		setTextualDescription();
		setIcon();
	}

	private void setDistance() {
		distanceToNextNavigationAction.setText(TextFormatter.formatDistance(navigationInstruction.getDistance(), 2));
	}

	private void setTextualDescription() {
		navigationActionDescription.setText(navigationInstruction.getDescription());
	}

	private void setIcon() {
		if (navigationInstruction.getType() == NavigationAction.NORTH) navigationActionImageView.getStyleClass().add("north");
		if (navigationInstruction.getType() == NavigationAction.WEST) navigationActionImageView.getStyleClass().add("west");
		if (navigationInstruction.getType() == NavigationAction.SOUTH) navigationActionImageView.getStyleClass().add("south");
		if (navigationInstruction.getType() == NavigationAction.EAST) navigationActionImageView.getStyleClass().add("east");
		if (navigationInstruction.getType() == NavigationAction.NORTHEAST) navigationActionImageView.getStyleClass().add("northeast");
		if (navigationInstruction.getType() == NavigationAction.SOUTHEAST) navigationActionImageView.getStyleClass().add("southeast");
		if (navigationInstruction.getType() == NavigationAction.SOUTHWEST) navigationActionImageView.getStyleClass().add("southwest");
		if (navigationInstruction.getType() == NavigationAction.NORTHWEST) navigationActionImageView.getStyleClass().add("northwest");

		if (navigationInstruction.getType() == NavigationAction.TURN_LEFT) navigationActionImageView.getStyleClass().add("turnLeft");
		if (navigationInstruction.getType() == NavigationAction.TURN_RIGHT) navigationActionImageView.getStyleClass().add("turnRight");
		if (navigationInstruction.getType() == NavigationAction.STRAIGHT) navigationActionImageView.getStyleClass().add("straight");
		if (navigationInstruction.getType() == NavigationAction.DESTINATION) navigationActionImageView.getStyleClass().add("destination");
	}
}
