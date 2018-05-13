package com.noticemedan.mappr.model.directions;

import com.noticemedan.mappr.model.NavigationAction;
import com.noticemedan.mappr.model.pathfinding.TravelType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class NavigationInstruction {
	@Getter NavigationAction type;
	@Getter Double distance;
	@Getter String road;
	@Getter TravelType travelType;

	public String getDescription() {
		String direction = "";
		String move = (travelType == TravelType.WALK) ? "Gå" : "Kør";
		if (this.type == NavigationAction.STRAIGHT) {
			move = "Fortsæt";
			direction = "ligeud";
		}

		if (this.type == NavigationAction.TURN_LEFT) direction = "til venstre";
		if (this.type == NavigationAction.TURN_RIGHT) direction = "til højre";
		if (this.type == NavigationAction.NORTH) direction = "nord";
		if (this.type == NavigationAction.WEST) direction = "vest";
		if (this.type == NavigationAction.SOUTH) direction = "syd";
		if (this.type == NavigationAction.EAST) direction = "øst";
		if (this.type == NavigationAction.NORTHEAST) direction = "nordøst";
		if (this.type == NavigationAction.SOUTHEAST) direction = "sydvest";
		if (this.type == NavigationAction.NORTHWEST) direction = "nordvest";
		if (this.type == NavigationAction.SOUTHWEST) direction = "sydvest";
		if (this.type == NavigationAction.UNKNOWN) direction = "?";

		return (this.type == NavigationAction.DESTINATION)
				? "Destinationen er længere nede af vejen"
				: move + " " + direction + " ad " + road;
	}
}
