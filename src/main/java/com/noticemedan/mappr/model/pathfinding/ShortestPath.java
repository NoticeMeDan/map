package com.noticemedan.mappr.model.pathfinding;

import com.noticemedan.mappr.model.directions.NavigationInstruction;
import com.noticemedan.mappr.view.util.TextFormatter;
import io.vavr.collection.Vector;
import lombok.Getter;

import java.awt.*;

public class ShortestPath {
	@Getter private String distanceToTravel;
	@Getter private String timeToTravel;
	@Getter private Vector<Shape> shortestPathShapes;
	@Getter private Vector<NavigationInstruction> travelInstructions;

	public void setDistanceToTravel(double distanceToTravel) {
		this.distanceToTravel = TextFormatter.formatDistance(distanceToTravel, 2);
	}

	public void setTimeToTravel(double timeToTravel) {
		this.timeToTravel = TextFormatter.formatTime(timeToTravel);
	}

	public void setShortestPathShapes(Vector<Shape> shortestPathShapes) {
		this.shortestPathShapes = shortestPathShapes;
	}

	public void setTravelInstructions(Vector<NavigationInstruction> travelInstructions) {
		this.travelInstructions = travelInstructions;
	}
}
