package com.noticemedan.mappr.model.pathfinding;

import com.noticemedan.mappr.model.util.IndexMinPQ;
import io.vavr.collection.Iterator;
import io.vavr.collection.Vector;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

/**
 * Dijkstra shortest path
 *zx
 * This implementation is an abbreviation of Dijkstra's algorithm from algs4 at
 * http://algs4.cs.princeton.edu/44sp/DijkstraSP.java
 * Algorithms, 4th Edition by Sedgewick & Wayne.
 *
 * @author Simon, Magnus
 */
@Slf4j
public class Dijkstra {
	private double[] distTo;
	private PathEdge[] edgeTo;
	private IndexMinPQ<Double> priorityQueue;
	private TravelType travelType;

	public Dijkstra(Network n, PathNode s, TravelType travelType) {
		n.getAllEdges().forEach(e -> {
			if (e.getWeight() < 0) throw new IllegalArgumentException("Weight less than zero");
		});

		this.distTo = new double[n.getAllNodes().size()];
		this.edgeTo = new PathEdge[n.getAllNodes().size()];
		this.travelType = travelType;

		validatePathNode(s.getId());

		for (int v = 0; v < n.getAllNodes().size(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;
		distTo[s.getId()] = 0.0;

		priorityQueue = new IndexMinPQ<>(n.getAllNodes().size());
		priorityQueue.insert(s.getId(), distTo[s.getId()]);
		while (!priorityQueue.isEmpty()) {
			int vId = priorityQueue.delMin();
			n.getAllNodes().get(vId).getEdges().filter(this::hasTravelType).forEach(this::relax);
		}
	}

	private void relax(PathEdge e) {
		int vId = e.getV().getId();
		int wId = e.getW().getId();
		double weight = (this.travelType.equals(TravelType.CAR)) ? (e.getWeight() / e.getSpeedLimit()) : e.getWeight();
		if (e.getSpeedLimit() == 0)
			System.out.println("Division by zero!");
		if (distTo[wId] > distTo[vId] + weight) {
			distTo[wId] = distTo[vId] + weight;
			edgeTo[wId] = e;
			if (priorityQueue.contains(wId)) priorityQueue.decreaseKey(wId, distTo[wId]);
			else priorityQueue.insert(wId, distTo[wId]);
		}
	}

	private boolean hasTravelType(PathEdge e) {
		return e.getTravelTypesAllowed().contains(this.travelType);
	}

	private void validatePathNode(int index) {
		if (index < 0 || index > distTo.length) throw new ArrayIndexOutOfBoundsException("PathNode is out of bound");
	}

	/**
	 * Returns the length of the distance between the source node s and node v
	 *
	 * @param v destination node
	 * @return length as double value
	 */
	public double pathDistance(PathNode v) {
		validatePathNode(v.getId());
		return distTo[v.getId()];
	}

	/**
	 * Returns true if a path from the source node s to node v exists
	 *
	 * @param v destination node
	 * @return boolean
	 */
	public boolean pathExists(PathNode v) {
		validatePathNode(v.getId());
		return distTo[v.getId()] < Double.POSITIVE_INFINITY;
	}

	/**
	 * Returns the shortest path between the source node s and node v
	 *
	 * @param v destination node
	 * @return a list of edges between the source and destination
	 */
	public Vector<PathEdge> derivePath(PathNode v) {
		validatePathNode(v.getId());
		if (!pathExists(v)) return Vector.empty();
		Vector<PathEdge> path = Vector.empty();
		for (PathEdge e = edgeTo[v.getId()]; e != null; e = edgeTo[e.getV().getId()]) {
			path = path.append(e);
		}
		return path;
	}
}
