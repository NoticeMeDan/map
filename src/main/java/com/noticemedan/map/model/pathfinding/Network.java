package com.noticemedan.map.model.pathfinding;

import com.noticemedan.map.model.utilities.Coordinate;
import io.vavr.collection.List;
import lombok.Data;

@Data
public class Network {
	private List<PathNode> allNodes;
	private List<PathEdge> allEdges;

	public Network() {
		this.allNodes = List.empty();
		this.allEdges = List.empty();
	}

	public void addPathNodes(List<Coordinate> coordinateList) {
		this.allNodes = coordinateList.map(c -> PathNode.builder()
				.lat(c.getY())
				.lon(c.getX())
				.edges(List.empty())
				.build());
	}

	public void addEdge(PathNode v, PathNode w) {
		PathEdge pathEdge = new PathEdge(v, w);
		v.setEdges(v.getEdges().append(pathEdge));
		allEdges = allEdges.append(pathEdge);
	}

}
