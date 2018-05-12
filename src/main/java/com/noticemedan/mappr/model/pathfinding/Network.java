package com.noticemedan.mappr.model.pathfinding;

import com.noticemedan.mappr.model.util.Coordinate;
import io.vavr.collection.*;
import io.vavr.control.Try;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j @Data
public class Network {
	private Vector<PathEdge> allEdges;
	private Vector<PathNode> allNodes;

	private Map<Coordinate, Integer> nodeFromCoordMap;

	public Network() {
		this.allNodes = Vector.empty();
		this.allEdges = Vector.empty();
		this.nodeFromCoordMap = HashMap.empty();
	}

	/**
	 * Add path to network
	 *
	 * @param from PathNode from
	 * @param to   PathNode to
	 */
	public void addPath(PathNode from, PathNode to, Set<TravelType> type) {
		PathNode fromNode = this.getNodeFromCoords(new Coordinate(from.getLon(), from.getLat()));
		PathNode toNode = this.getNodeFromCoords(new Coordinate(to.getLon(), to.getLat()));

		if (fromNode == null) {
			addPathNode(from);
			fromNode = from;
		}

		if (toNode == null) {
			addPathNode(to);
			toNode = to;
		}

		addEdge(fromNode, toNode, type);
	}

	/**
	 * Get PathNode from coordinate
	 *
	 * @param c Coordinate
	 * @return PathNode
	 */
	public PathNode getNodeFromCoords(Coordinate c) {
		return Try.of(() -> this.allNodes.get(this.nodeFromCoordMap.get(c).get())).getOrNull();
	}

	public void addEdge(PathNode v, PathNode w, Set<TravelType> type) {
		PathEdge pathEdgeV = new PathEdge(v, w);
		PathEdge pathEdgeW = new PathEdge(w, v);

		pathEdgeV.setTravelTypesAllowed(type);
		pathEdgeW.setTravelTypesAllowed(type);

		if (!v.getEdges().contains(pathEdgeV)) v.setEdges(v.getEdges().prepend(pathEdgeV));
		if (!w.getEdges().contains(pathEdgeW)) w.setEdges(w.getEdges().prepend(pathEdgeW));

		allEdges = allEdges.append(pathEdgeV);
	}

	private void addPathNode(PathNode pathNode) {
		this.nodeFromCoordMap = nodeFromCoordMap.put(new Coordinate(pathNode.getLon(), pathNode.getLat()), this.allNodes.length());
		this.allNodes = this.allNodes.append(pathNode);
	}
}
