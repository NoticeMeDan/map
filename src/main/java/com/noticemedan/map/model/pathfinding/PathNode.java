package com.noticemedan.map.model.pathfinding;

import io.vavr.collection.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PathNode {
	private int id;
	private double lon;
	private double lat;
	List<PathEdge> edges;

	public String toString() {
		return "[id: " + id + " :: {" + lon + ", " + lat + "}]";
	}
}
