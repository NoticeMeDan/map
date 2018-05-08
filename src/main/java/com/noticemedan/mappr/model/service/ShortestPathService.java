package com.noticemedan.mappr.model.service;

import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.pathfinding.*;
import com.noticemedan.mappr.model.util.Coordinate;
import io.vavr.collection.Vector;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.Random;

@Slf4j
public class ShortestPathService {

	@Getter
	private Dijkstra dijkstra;
	private NetworkParser networkParser;
	private Network network;

	public ShortestPathService(Vector<Element> elements) {
		this.networkParser = new NetworkParser(elements);
		this.network = networkParser.getNetwork();
	}

	public Vector<Shape> getShortestPath(Coordinate from, Coordinate to) {
		PathNode nodeFrom = this.network.getNodeFromCoords(from);
		PathNode nodeTo = this.network.getNodeFromCoords(to);
		Vector<Shape> shapes = Vector.empty();
		for (PathEdge pathEdge : getShortestPath(nodeFrom, nodeTo)) {
			shapes = shapes.append(pathEdge.toShape());
		}
		return shapes;
	}

	public Vector<Shape> getRandomShortestPath() {
		Vector<Shape> shapes = Vector.empty();
		for (PathEdge pathEdge : getRandomSP()) {
			shapes = shapes.append(pathEdge.toShape());
		}
		return shapes;
	}

	public Vector<PathNode> getAllNodes() {
		return this.network.getAllNodes();
	}
	public Vector<PathEdge> getAllEdges() {
		return this.network.getAllEdges();
	}

	private Vector<PathEdge> getShortestPath(PathNode from, PathNode to) {
		dijkstra = new Dijkstra(this.networkParser.getNetwork(), from);
		return dijkstra.derivePath(to);
	}
	private Vector<PathEdge> getRandomSP() {
		/*Random random = new Random();
		PathNode from = this.network.getAllNodes().get(random.nextInt(this.network.getAllNodes().size() - 1));
		PathNode to = this.network.getAllNodes().get(random.nextInt(this.network.getAllNodes().size() - 1));*/
		PathNode from = this.network.getAllNodes().get(31464);
		PathNode to = this.network.getAllNodes().get(29395);
		Vector<PathEdge> randomSP = getShortestPath(from, to);
		log.info("..............................");
		log.info("Random Dijkstra nodes created:");
		log.info("startNode: " + from.toString());
		log.info("endNode: " + to.toString());
		if (!this.dijkstra.pathExists(to)) log.info("There exists NO path");
		else log.info("There exists a path from startNode to endNode");
		log.info("..............................");
		return randomSP;
	}
}
