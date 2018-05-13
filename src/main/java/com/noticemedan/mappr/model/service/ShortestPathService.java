package com.noticemedan.mappr.model.service;

import com.noticemedan.mappr.model.directions.Guide;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.pathfinding.*;
import com.noticemedan.mappr.model.util.Coordinate;

import io.vavr.collection.Vector;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.Random;

@Slf4j
public class ShortestPathService {

	private Dijkstra dijkstra;
	private NetworkParser networkParser;
	private Network network;

	private static final double averageWalkSpeed = 5000;
	private static final double averageCycleSpeed = 15500;

	public ShortestPathService(Vector<Element> elements) {
		this.networkParser = new NetworkParser(elements);
		this.network = networkParser.getNetwork();
	}

	public ShortestPath getShortestPath(Coordinate from, Coordinate to, TravelType type) {
		PathNode nodeFrom = this.network.getNodeFromCoords(from);
		PathNode nodeTo = this.network.getNodeFromCoords(to);
		Guide navigationGuide = new Guide();
		Vector<Shape> shapes = Vector.empty();
		Vector<PathEdge> edgeList = getShortestPath(nodeFrom, nodeTo, type);
		double distance = 0.0;
		double averageSpeed = 0.0;
		for (PathEdge pathEdge : edgeList) {
			shapes = shapes.append(pathEdge.toShape());
			distance += pathEdge.getWeight();
			averageSpeed += pathEdge.getSpeedLimit();
		}
		averageSpeed /= edgeList.length();
		averageSpeed *= 1000;

		double timeToTravel;
		if (type == TravelType.WALK) timeToTravel = distance / averageWalkSpeed;
		else if (type == TravelType.BIKE) timeToTravel = distance / averageCycleSpeed;
		else timeToTravel = distance / averageSpeed;

		ShortestPath shortestPath = new ShortestPath();
		shortestPath.setDistanceToTravel(distance);
		shortestPath.setTravelInstructions(navigationGuide.getDirections(edgeList, type));
		shortestPath.setTimeToTravel(timeToTravel);
		shortestPath.setShortestPathShapes(shapes);
		return shortestPath;
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

	public Vector<PathEdge> getShortestPath(PathNode from, PathNode to, TravelType type) {
		dijkstra = new Dijkstra(this.networkParser.getNetwork(), from, type);
		return dijkstra.derivePath(to);
	}

	private Vector<PathEdge> getRandomSP(TravelType type) {
		Random random = new Random();
		PathNode from = this.network.getAllNodes().get(random.nextInt(this.network.getAllNodes().size() - 1));
		PathNode to = this.network.getAllNodes().get(random.nextInt(this.network.getAllNodes().size() - 1));
		Vector<PathEdge> randomSP = getShortestPath(from, to, type);
		log.info("..............................");
		log.info("Random Dijkstra nodes created:");
		log.info("startNode: " + from.toString());
		log.info("endNode: " + to.toString());
		log.info("Travel by: " + type);
		if (!this.dijkstra.pathExists(to)) log.info("There exists NO path");
		else log.info("There exists a path from startNode to endNode");
		log.info("..............................");
		return randomSP;
	}
}
