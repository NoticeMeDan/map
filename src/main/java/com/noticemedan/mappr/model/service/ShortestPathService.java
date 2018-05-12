package com.noticemedan.mappr.model.service;

import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.pathfinding.*;
import com.noticemedan.mappr.model.util.Coordinate;
import io.vavr.collection.Vector;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
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

	public Vector<Shape> getShortestPath(Coordinate from, Coordinate to, TravelType type) {
		PathNode nodeFrom = this.network.getNodeFromCoords(from);
		PathNode nodeTo = this.network.getNodeFromCoords(to);
		Vector<Shape> shapes = Vector.empty();
		for (PathEdge pathEdge : getShortestPath(nodeFrom, nodeTo, type)) {
			shapes = shapes.append(pathEdge.toShape());
		}
		return shapes;
	}

	public Vector<Shape> getRandomShortestPath(TravelType type) {
		Vector<Shape> shapes = Vector.empty();
		for (PathEdge pathEdge : getRandomSP(type)) {
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

	public double getPathDistance(Coordinate from, Coordinate to, TravelType type) {
		PathNode nodeFrom = this.network.getNodeFromCoords(from);
		PathNode nodeTo = this.network.getNodeFromCoords(to);
		dijkstra = new Dijkstra(this.networkParser.getNetwork(), nodeFrom, type);
		return dijkstra.pathDistance(nodeTo);
	}

	private Vector<PathEdge> getShortestPath(PathNode from, PathNode to, TravelType type) {
		dijkstra = new Dijkstra(this.networkParser.getNetwork(), from, type);
		return dijkstra.derivePath(to);
	}
	public Vector<PathEdge> getRandomSP(TravelType type) {
		Random random = new Random();
		PathNode from = this.network.getAllNodes().get(random.nextInt(this.network.getAllNodes().size() - 1));
		PathNode to = this.network.getAllNodes().get(random.nextInt(this.network.getAllNodes().size() - 1));
		Vector<PathEdge> randomSP = getShortestPath(from, to, type);
		log.info("..............................");
		log.info("Random Dijkstra nodes created:");
		log.info("startNode: " + from.toString());
		log.info("endNode: " + to.toString());
		log.info("Travel by: " + type);
		log.info("Distance: " + getPathDistance(new Coordinate(from.getLon(),from.getLat()),new Coordinate(to.getLon(),to.getLat()),type) + "km");
		if (!this.dijkstra.pathExists(to)) {
			log.info("There exists NO path");
			log.info("..............................");
			randomSP = getRandomSP(type);
		}
		else log.info("There exists a path from startNode to endNode");
		log.info("..............................");
		return randomSP;
	}
}
