package com.noticemedan.mappr.model.directions;

import com.noticemedan.mappr.model.NavigationAction;
import com.noticemedan.mappr.model.pathfinding.PathEdge;
import com.noticemedan.mappr.model.pathfinding.TravelType;
import com.noticemedan.mappr.model.util.Coordinate;
import io.vavr.collection.Vector;

public class Guide {

	private double distance;
	private Vector<NavigationInstruction> directions = Vector.empty();
	private PathEdge edgeBefore;

	public Vector<NavigationInstruction> getDirections(Vector<PathEdge> route, TravelType type) {
		this.distance = 0;

		route.forEach(e-> {
			this.distance += e.getWeight();
			if (e.getV().degree() > 3) {
				if (e.getRoadName() == null) return;
				if (this.distance < 0.01) return;
				this.directions = directions.append(
								new NavigationInstruction(determineMapDirection(e),
								this.distance, e.getRoadName(),
								type,
								new Coordinate(e.getV().getLon(), e.getV().getLat())));
				this.distance = 0;
			}
			if (e.getW().degree() == 3) {
				if (e.getRoadName() == null) return;
				if (this.distance < 0.01) return;
				this.directions = directions.append(
								new NavigationInstruction(determineLeftRightDirection(e, edgeBefore),
								this.distance,
								e.getRoadName(),
								type,
								new Coordinate(e.getV().getLon(), e.getV().getLat())));
			}
			edgeBefore = e;
		});

		this.directions = directions.append(
						new NavigationInstruction(NavigationAction.DESTINATION,
						this.distance,
						"Ankommet",
						type,
						new Coordinate(route.get(route.length() - 1).getW().getLon(), route.get(route.length() - 1).getW().getLat())));
		return directions;
	}

	private NavigationAction determineMapDirection(PathEdge next) {
		double[] vectorNext = {next.getW().getLon() - next.getV().getLon(), next.getW().getLat() - next.getV().getLat()};
		return vectorDirection(vectorNext);
	}

	private NavigationAction determineLeftRightDirection(PathEdge before, PathEdge next) {
		if (before == null || next == null) return NavigationAction.STRAIGHT;
		double[] vectorBefore = {before.getV().getLon() - before.getW().getLon(), before.getV().getLat() - before.getW().getLat()};
		double[] vectorNext = {next.getV().getLon() - next.getW().getLon(), next.getV().getLat() - next.getW().getLat()};

		if (vectorDirection(vectorBefore) == NavigationAction.NORTHWEST) {
			if (vectorDirection(vectorNext) == NavigationAction.NORTHEAST) return NavigationAction.TURN_RIGHT;
			if (vectorDirection(vectorNext) == NavigationAction.SOUTHWEST) return NavigationAction.TURN_LEFT;
		}
		if (vectorDirection(vectorBefore) == NavigationAction.NORTHEAST) {
			if (vectorDirection(vectorNext) == NavigationAction.SOUTHEAST) return NavigationAction.TURN_RIGHT;
			if (vectorDirection(vectorNext) == NavigationAction.NORTHWEST) return NavigationAction.TURN_LEFT;
		}
		if (vectorDirection(vectorBefore) == NavigationAction.SOUTHEAST) {
			if (vectorDirection(vectorNext) == NavigationAction.SOUTHWEST) return NavigationAction.TURN_RIGHT;
			if (vectorDirection(vectorNext) == NavigationAction.NORTHEAST) return NavigationAction.TURN_LEFT;
		}
		if (vectorDirection(vectorBefore) == NavigationAction.SOUTHWEST) {
			if (vectorDirection(vectorNext) == NavigationAction.NORTHWEST) return NavigationAction.TURN_RIGHT;
			if (vectorDirection(vectorNext) == NavigationAction.SOUTHEAST) return NavigationAction.TURN_LEFT;
		}
		return NavigationAction.STRAIGHT;
	}

	private NavigationAction vectorDirection(double[] v) {
		if (v[0] > 0 && v[1] > 0) return NavigationAction.NORTHEAST;
		if (v[0] > 0 && v[1] < 0) return NavigationAction.SOUTHEAST;
		if (v[0] < 0 && v[1] > 0) return NavigationAction.NORTHWEST;
		if (v[0] < 0 && v[1] < 0) return NavigationAction.SOUTHWEST;
		if (v[0] == 0 && v[1] > 0) return NavigationAction.NORTH;
		if (v[0] < 0 && v[1] == 0) return NavigationAction.WEST;
		if (v[0] == 0 && v[1] < 0) return NavigationAction.SOUTH;
		if (v[0] > 0 && v[1] == 0) return NavigationAction.EAST;
		return NavigationAction.UNKNOWN;
	}
}

