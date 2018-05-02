package com.noticemedan.map.view;

import com.noticemedan.map.model.NavigationAction;
import com.noticemedan.map.model.utilities.TextFormatter;
import io.vavr.control.Try;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

public class NavigationInstructionCellController {

	@FXML
	@Getter Pane cell;
	@FXML Pane navigationActionImageView;
	@FXML Label distanceToNextNavigationAction;
	@FXML Text navigationActionDescription;

	private RoutePaneController.NavigationInstruction navigationInstruction; // The object that this cell represents

	public NavigationInstructionCellController(RoutePaneController.NavigationInstruction navigationInstruction) {
		this.navigationInstruction = navigationInstruction;
		initialiseCell();
		setTextDescription();
		setIcon();
	}

	private void initialiseCell() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NavigationInstructionCell.fxml"));
		fxmlLoader.setController(this);
		Try.of(fxmlLoader::load);
	}

	private void setTextDescription() {
		distanceToNextNavigationAction.setText(TextFormatter.formatDistance(navigationInstruction.getDistance(), 1));

		if (navigationInstruction.getType() == NavigationAction.TURN_LEFT)
			navigationActionDescription.setText("Drej til venstre ind på " + navigationInstruction.getRoad());
		if (navigationInstruction.getType() == NavigationAction.TURN_RIGHT)
			navigationActionDescription.setText("Drej til højre ind på " + navigationInstruction.getRoad());
		if (navigationInstruction.getType() == NavigationAction.ROUNDABOUT)
			navigationActionDescription.setText("I rundkørsel tag " + navigationInstruction.getRoundAbout() + ". afkørsel ind på " + navigationInstruction.getRoad());
		if (navigationInstruction.getType() == NavigationAction.STRAIGHT)
			navigationActionDescription.setText("Forsæt ligeud ind på " + navigationInstruction.getRoad());
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
