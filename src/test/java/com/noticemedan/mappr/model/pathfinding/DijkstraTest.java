package com.noticemedan.mappr.model.pathfinding;

import com.noticemedan.mappr.model.util.Coordinate;
import io.vavr.collection.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DijkstraTest {
	private List<Coordinate> coordinates;
	private Network network;
	private PathNode pathNodeFrom;
	private PathNode pathNodeTo;

	@BeforeMethod
	void setUp() {
		network = new Network();
		coordinates = List.of(
				new Coordinate(4, 6),
				new Coordinate(3, 16),
				new Coordinate(6, 10),
				new Coordinate(13, 14),
				new Coordinate(12, 5),
				new Coordinate(18, 7),
				new Coordinate(19, 18),
				new Coordinate(22, 11)
		);

		network.addPathNodes(this.coordinates);

		PathNode n0 = network.getAllNodes().get(0);
		PathNode n1 = network.getAllNodes().get(1);
		PathNode n2 = network.getAllNodes().get(2);
		PathNode n3 = network.getAllNodes().get(3);
		PathNode n4 = network.getAllNodes().get(4);
		PathNode n5 = network.getAllNodes().get(5);
		PathNode n6 = network.getAllNodes().get(6);
		PathNode n7 = network.getAllNodes().get(7);

		pathNodeFrom = n0;
		pathNodeTo = n7;

		network.addEdge(n0, n1);
		network.addEdge(n0, n2);

		network.addEdge(n1, n0);
		network.addEdge(n1, n2);
		network.addEdge(n1, n3);
		network.addEdge(n1, n6);

		network.addEdge(n2, n0);
		network.addEdge(n2, n1);
		network.addEdge(n2, n3);
		network.addEdge(n2, n4);

		network.addEdge(n3, n1);
		network.addEdge(n3, n2);
		network.addEdge(n3, n4);
		network.addEdge(n3, n5);
		network.addEdge(n3, n6);

		network.addEdge(n4, n2);
		network.addEdge(n4, n3);
		network.addEdge(n4, n5);

		network.addEdge(n5, n3);
		network.addEdge(n5, n4);
		network.addEdge(n5, n6);
		network.addEdge(n5, n7);

		network.addEdge(n6, n1);
		network.addEdge(n6, n3);
		network.addEdge(n6, n5);
		network.addEdge(n6, n7);

		network.addEdge(n7, n5);
		network.addEdge(n7, n6);

		printDijkstraPath();
	}

	private void printEdges(int n) {
		System.out.println("Node: " + n);
		System.out.println("Degree: " + network.getAllNodes().get(n).getEdges().length());
		network.getAllNodes().get(n).getEdges().forEach(System.out::println);
		System.out.println("TEST END: //////////////");
	}

	private void printDijkstraPath() {
		System.out.println("DIJKSTRA TEST");
		System.out.println("____________________________\n");
		System.out.println("The shortest path between " + pathNodeFrom + " and " + pathNodeTo + " :");
		System.out.println();
		Dijkstra dijkstra = new Dijkstra(network, pathNodeFrom);
		if (dijkstra.pathExists(pathNodeTo)) {
			dijkstra.derivePath(pathNodeTo).forEach(System.out::println);
		} else System.out.println("There is no path");
	}

	@Test
	void pathNodeDegreeTest() {
		assertEquals(network.getAllNodes().get(0).getEdges().length(), 2);
		assertEquals(network.getAllNodes().get(1).getEdges().length(), 4);
		assertEquals(network.getAllNodes().get(2).getEdges().length(), 4);
		assertEquals(network.getAllNodes().get(3).getEdges().length(), 5);
		assertEquals(network.getAllNodes().get(4).getEdges().length(), 3);
		assertEquals(network.getAllNodes().get(5).getEdges().length(), 4);
		assertEquals(network.getAllNodes().get(6).getEdges().length(), 4);
		assertEquals(network.getAllNodes().get(7).getEdges().length(), 2);
	}

	@Test
	void dijkstraTest() {
		Dijkstra dijkstra = new Dijkstra(network, pathNodeFrom);
		double pathDistance = dijkstra.pathDistance(pathNodeTo);
		pathDistance *= 100;
		pathDistance = Math.round(pathDistance);
		pathDistance /= 100;

		assertEquals(dijkstra.pathExists(pathNodeTo), true);
		assertEquals(pathDistance, 24.26);
	}

}
