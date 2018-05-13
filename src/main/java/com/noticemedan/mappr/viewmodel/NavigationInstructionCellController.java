package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.model.NavigationAction;
import com.noticemedan.mappr.model.directions.NavigationInstruction;
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
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NavigationInstructionCell.fxml"));
		fxmlLoader.setController(this);
		Try.of(fxmlLoader::load);
	}

	private void setInformation() {
		setDistance();
		setTextualDescription();
		setIcon();
	}

	private void setDistance() {
		if(navigationInstruction.getDistance() > 1) distanceToNextNavigationAction.setText(String.valueOf(navigationInstruction.getDistance() + " km"));
		else										distanceToNextNavigationAction.setText(String.valueOf(navigationInstruction.getDistance() * 1000) + " m");
	}

	private void setTextualDescription() {
		if (navigationInstruction.getType() == NavigationAction.TURN_LEFT)
			navigationActionDescription.setText("Drej til venstre ved " + navigationInstruction.getRoad());
		if (navigationInstruction.getType() == NavigationAction.TURN_RIGHT)
			navigationActionDescription.setText("Drej til højre ved " + navigationInstruction.getRoad());
		if (navigationInstruction.getType() == NavigationAction.STRAIGHT)
			navigationActionDescription.setText("Forsæt ligeud ved " + navigationInstruction.getRoad());
		if (navigationInstruction.getType() == NavigationAction.DESTINATION)
			navigationActionDescription.setText("Destinationen er længere nede af vejen.");
	}

	private void setIcon() {
		if (navigationInstruction.getType() == NavigationAction.TURN_LEFT) navigationActionImageView.getStyleClass().add("turnLeft");
		if (navigationInstruction.getType() == NavigationAction.TURN_RIGHT) navigationActionImageView.getStyleClass().add("turnRight");
		if (navigationInstruction.getType() == NavigationAction.ROUNDABOUT) navigationActionImageView.getStyleClass().add("roundabout");
		if (navigationInstruction.getType() == NavigationAction.STRAIGHT) navigationActionImageView.getStyleClass().add("straight");
		if (navigationInstruction.getType() == NavigationAction.DESTINATION) navigationActionImageView.getStyleClass().add("destination");
	}
}
