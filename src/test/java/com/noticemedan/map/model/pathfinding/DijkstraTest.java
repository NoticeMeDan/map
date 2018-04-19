package com.noticemedan.map.model.pathfinding;

import com.noticemedan.map.model.utilities.Coordinate;
import io.vavr.collection.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DijkstraTest {
	List<Coordinate> coordinates;
	Network network;

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

		addAllNodesTest();
	}

	private void addAllNodesTest() {
		network.addPathNodes(this.coordinates);

		PathNode n0 = network.getAllNodes().get(0);
		PathNode n1 = network.getAllNodes().get(1);
		PathNode n2 = network.getAllNodes().get(2);
		PathNode n3 = network.getAllNodes().get(3);
		PathNode n4 = network.getAllNodes().get(4);
		PathNode n5 = network.getAllNodes().get(5);
		PathNode n6 = network.getAllNodes().get(6);
		PathNode n7 = network.getAllNodes().get(7);

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

		printEdges(3);
		printEdges(4);
		printEdges(0);
		printEdges(7);
	}

	private void printEdges(int n) {
		System.out.println("TEST Node" + n);
		System.out.println(network.getAllNodes().get(n).getEdges().length());
		network.getAllNodes().get(n).getEdges().forEach(System.out::println);
		System.out.println("TEST END: //////////////");
	}

	@Test
	void pathNodeDegreeTest() {
		assertEquals(2, network.getAllNodes().get(0).getEdges().length());
		assertEquals(4, network.getAllNodes().get(1).getEdges().length());
		assertEquals(4, network.getAllNodes().get(2).getEdges().length());
		assertEquals(5, network.getAllNodes().get(3).getEdges().length());
		assertEquals(3, network.getAllNodes().get(4).getEdges().length());
		assertEquals(4, network.getAllNodes().get(5).getEdges().length());
		assertEquals(4, network.getAllNodes().get(6).getEdges().length());
		assertEquals(2, network.getAllNodes().get(7).getEdges().length());
	}

}
