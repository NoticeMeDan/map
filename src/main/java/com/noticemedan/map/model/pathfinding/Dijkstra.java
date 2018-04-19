package com.noticemedan.map.model.pathfinding;

import com.noticemedan.map.model.utilities.IndexMinPQ;

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

	}

	private void validatePathNode(int index) {
		if (index < 0 || index > distTo.length) throw new ArrayIndexOutOfBoundsException("PathNode is out of bound");
	}

}
