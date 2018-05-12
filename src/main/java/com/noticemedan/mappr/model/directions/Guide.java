package com.noticemedan.mappr.model.directions;

import com.noticemedan.mappr.model.pathfinding.PathEdge;
import io.vavr.collection.Vector;
import lombok.Getter;

public class Guide {

	private double distance;
	private String direction;
	@Getter
	private double traveleddistance;

	Vector<String> getDirections(Vector<PathEdge> route) {
		Vector<String> directions = Vector.empty();
		this.traveleddistance = 0;
		this.distance = 0;

		for (int i = 0; i < route.length(); i++) {
			if (i == route.length()-1) {
				this.distance = (route.get(i).getWeight());
				this.direction = "You have arrived";
				directions = directions.append("After " + this.distance + "m " + this.direction);
				this.traveleddistance += this.distance;
				break;
			}

			this.direction = (route.get(i).getW().getEdges().length() > 2) ? "turn" : "Go straight";
			this.distance += (route.get(i).getWeight());
			if (this.direction.equals("turn")) {
				directions = directions.append("After " + this.distance + "m " + this.direction);
				this.traveleddistance += this.distance;
				this.distance = 0;
			}
		}
		return directions;
	}
}

