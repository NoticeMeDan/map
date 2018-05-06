package com.noticemedan.mappr.model.pathfinding;

import com.noticemedan.mappr.model.util.Coordinate;
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
		int id = 0;
		for (Coordinate coordinate : coordinateList) {
			this.allNodes = this.allNodes.append(
					PathNode.builder()
							.id(id++)
							.lat(coordinate.getY())
							.lon(coordinate.getX())
							.edges(List.empty())
							.build()
			);
		}
	}

	public void addEdge(PathNode v, PathNode w) {
		PathEdge pathEdge = new PathEdge(v, w);
		v.setEdges(v.getEdges().append(pathEdge));
		allEdges = allEdges.append(pathEdge);
	}

}
