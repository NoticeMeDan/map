package com.noticemedan.mappr.model.pathfinding;

import com.noticemedan.mappr.model.util.Coordinate;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;
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
	public void addPath(PathNode from, PathNode to) {
		for (int i = 1; i < 100; i++) {
			int flag = i * 100000;
			if (allNodes.size() == flag) log.info(flag + " reached");
		}

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

		addEdge(fromNode, toNode);
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

	private void addPathNode(PathNode pathNode) {
		this.nodeFromCoordMap = nodeFromCoordMap.put(new Coordinate(pathNode.getLon(), pathNode.getLat()), this.allNodes.length());
		this.allNodes = this.allNodes.append(pathNode);
	}

	public void addEdge(PathNode v, PathNode w) {
		PathEdge pathEdgeV = new PathEdge(v, w);
		PathEdge pathEdgeW = new PathEdge(w, v);

		if (!v.getEdges().contains(pathEdgeV)) v.setEdges(v.getEdges().prepend(pathEdgeV));
		if (!w.getEdges().contains(pathEdgeW)) w.setEdges(w.getEdges().prepend(pathEdgeW));
		pathEdgeV.setRoadType(v.getRoadType());
		allEdges = allEdges.append(pathEdgeV);
	}

	/**
	 * For testing purposes only
	 *
	 * @param coordinateList
	 */
	public void addPathNodes(List<Coordinate> coordinateList) {
		for (Coordinate coordinate : coordinateList) {
			this.allNodes = this.allNodes.append(
					PathNode.builder()
							.id(this.allNodes.length())
							.lon(coordinate.getX())
							.lat(coordinate.getY())
							.edges(Vector.empty())
							.build()
			);
		}
		System.out.println("List is now size: " + allNodes.length());
	}

}
