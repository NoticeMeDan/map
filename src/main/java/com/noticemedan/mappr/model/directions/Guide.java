package com.noticemedan.mappr.model.directions;

import com.noticemedan.mappr.model.NavigationAction;
import com.noticemedan.mappr.model.pathfinding.PathEdge;
import io.vavr.collection.Vector;

public class Guide {

	private double distance;
	private Vector<NavigationInstruction> directions = Vector.empty();

	public Vector<NavigationInstruction> getDirections(Vector<PathEdge> route) {
		this.distance = 0;

		route.forEach(e-> {
			this.distance += e.getWeight();
			if ((e.getW().getEdges().length() > 2)) {
				this.directions = directions.prepend(new NavigationInstruction(NavigationAction.TURN_LEFT, this.distance, e.getRoadName()));
				this.distance = 0;
			}
		});

		this.directions = directions.append(new NavigationInstruction(NavigationAction.DESTINATION, this.distance, "Ankommet"));

		return directions;
	}
}

