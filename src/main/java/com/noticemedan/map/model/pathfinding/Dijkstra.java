package com.noticemedan.map.model.pathfinding;

import com.noticemedan.map.model.utilities.IndexMinPQ;
import io.vavr.collection.List;

/**
 * Dijkstra shortest path
 *
 * This implementation is an abbreviation of Dijkstra's algorithm from algs4 at
 * http://algs4.cs.princeton.edu/44sp/DijkstraSP.java
 * Algorithms, 4th Edition by Sedgewick & Wayne.
 *
 * @author Simon, Magnus
 */
public class Dijkstra {
	private double[] distTo;
	private PathEdge[] edgeTo;
	private IndexMinPQ<Double> priorityQueue;

	public Dijkstra(Network n, PathNode s) {
		n.getAllEdges().forEach(e -> {
			if (e.getWeight() < 0) throw new IllegalArgumentException("Weight less than zero");
		});

		distTo = new double[n.getAllNodes().length()];
		edgeTo = new PathEdge[n.getAllNodes().length()];

		validatePathNode(s.getId());

		for (int v = 0; v < n.getAllNodes().length(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;
		distTo[s.getId()] = 0.0;

		priorityQueue = new IndexMinPQ<>(n.getAllNodes().length());
		priorityQueue.insert(s.getId(), distTo[s.getId()]);
		while (!priorityQueue.isEmpty()) {
			int vId = priorityQueue.delMin();
			n.getAllNodes().get(vId).getEdges().forEach(this::relax);
		}
	}

	private void relax(PathEdge e) {
		int vId = e.getV().getId();
		int wId = e.getW().getId();
		if (distTo[wId] > distTo[vId] + e.getWeight()) {
			distTo[wId] = distTo[vId] + e.getWeight();
			edgeTo[wId] = e;
			if (priorityQueue.contains(wId)) priorityQueue.decreaseKey(wId, distTo[wId]);
			else priorityQueue.insert(wId, distTo[wId]);
		}
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
	public List<PathEdge> derivePath(PathNode v) {
		validatePathNode(v.getId());
		if (!pathExists(v)) return null;
		List<PathEdge> path = List.empty();
		for (PathEdge e = edgeTo[v.getId()]; e != null; e = edgeTo[e.getV().getId()])
			path = path.append(e);
		return path;
	}
}
