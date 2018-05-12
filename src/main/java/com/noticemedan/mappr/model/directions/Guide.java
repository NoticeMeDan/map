package com.noticemedan.mappr.model.directions;

import com.noticemedan.mappr.model.pathfinding.PathEdge;
import io.vavr.collection.Vector;
import lombok.Getter;

public class Guide {

	private double distance;
	private Vector<String> directions = Vector.empty();
	@Getter
	//test only
	private double traveleddistance;

	Vector<String> getDirections(Vector<PathEdge> route) {
		this.traveleddistance = 0;
		this.distance = 0;

		route.forEach(e-> {
			this.distance += e.getWeight();
			if ((e.getW().getEdges().length() > 2)) {
				this.directions = directions.append("Follow the road " + this.distance + "m, turn_left_or_right");
				this.traveleddistance += this.distance;
				this.distance = 0;
			}
		});
		if(this.distance != 0) this.directions = directions.append("Follow the road " + this.distance + "m ");
		this.traveleddistance += this.distance;
		this.directions = directions.append("You have arrived");

		return directions;
	}
}

