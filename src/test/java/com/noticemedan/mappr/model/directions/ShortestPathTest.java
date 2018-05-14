package com.noticemedan.mappr.model.directions;

import com.noticemedan.mappr.model.NavigationAction;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.map.Type;
import com.noticemedan.mappr.model.pathfinding.PathEdge;
import com.noticemedan.mappr.model.pathfinding.ShortestPath;
import com.noticemedan.mappr.model.pathfinding.TravelType;
import com.noticemedan.mappr.model.service.ShortestPathService;
import com.noticemedan.mappr.model.util.Coordinate;
import io.vavr.collection.Set;
import io.vavr.collection.Vector;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

import static org.testng.Assert.assertEquals;

/**
 * A picture of the test-network can be seen here:
 * https://drive.google.com/open?id=1xq906-f1p80HTDRqyPXTvzIi40Sg9SzV
 */
public class ShortestPathTest {
	ShortestPathService shortestPathService;

	@BeforeTest
	void prepareNetwork() {
		Vector<Element> elements = Vector.empty();
		elements = elements.append(createElement(new Coordinate(3, 8), new Coordinate(4, 3), "f", 100));
		elements = elements.append(createElement(new Coordinate(4, 3), new Coordinate(7, 7), "l", 50));
		elements = elements.append(createElement(new Coordinate(3, 8), new Coordinate(7, 7), "i", 100));
		elements = elements.append(createElement(new Coordinate(3, 8), new Coordinate(5, 6), "g", 100));
		elements = elements.append(createElement(new Coordinate(5, 6), new Coordinate(7, 7), "h", 50));
		elements = elements.append(createElement(new Coordinate(7, 7), new Coordinate(9, 8), "k", 20));
		elements = elements.append(createElement(new Coordinate(7, 7), new Coordinate(12, 4), "j", 20));
		elements = elements.append(createElement(new Coordinate(7, 7), new Coordinate(8, 1), "m", 20));
		elements = elements.append(createElement(new Coordinate(8, 1), new Coordinate(14, 2), "p", 50));
		elements = elements.append(createElement(new Coordinate(9, 8), new Coordinate(14, 2), "q", 50));
		elements = elements.append(createElement(new Coordinate(14, 2), new Coordinate(14, 1), "n", 20));
		shortestPathService = new ShortestPathService(elements);
		printAllNodes();
		printAllEdges();
	}

	private Element createElement(Coordinate c1, Coordinate c2, String name, int speed) {
		Element element = new Element();
		element.setType(Type.ROAD);
		element.setShape(new Path2D.Double(new Line2D.Double(c1, c2)));
		element.setName(name);
		element.setMaxspeed(speed);
		return element;
	}

	private void printAllNodes() {
		System.out.println("Print all nodes\n............");
		shortestPathService.getAllNodes().forEach(System.out::println);
	}

	private void printAllEdges() {
		System.out.println("Print all edges\n............");
		shortestPathService.getAllEdges().forEach(System.out::println);
	}

	@Test
	public void edgeDegreeTest() {
		assertEquals(shortestPathService.getAllNodes().get(0).degree(), 3);
		assertEquals(shortestPathService.getAllNodes().get(1).degree(), 2);
		assertEquals(shortestPathService.getAllNodes().get(2).degree(), 6);
		assertEquals(shortestPathService.getAllNodes().get(3).degree(), 2);
		assertEquals(shortestPathService.getAllNodes().get(4).degree(), 2);
		assertEquals(shortestPathService.getAllNodes().get(5).degree(), 1);
		assertEquals(shortestPathService.getAllNodes().get(6).degree(), 2);
		assertEquals(shortestPathService.getAllNodes().get(7).degree(), 3);
		assertEquals(shortestPathService.getAllNodes().get(8).degree(), 1);
	}

	@Test
	public void shortestPathWalkTest() {
		Vector<PathEdge> pathEdges = shortestPathService.getShortestPath(shortestPathService.getAllNodes().get(0), shortestPathService.getAllNodes().get(8), TravelType.WALK);
		assertEquals(pathEdges.get(0).getRoadName(), "i");
		assertEquals(pathEdges.get(1).getRoadName(), "k");
		assertEquals(pathEdges.get(2).getRoadName(), "q");
		assertEquals(pathEdges.get(3).getRoadName(), "n");
	}

	@Test
	public void shortestPathCarTest() {
		Vector<PathEdge> pathEdges = shortestPathService.getShortestPath(shortestPathService.getAllNodes().get(0), shortestPathService.getAllNodes().get(8), TravelType.CAR);
		pathEdges.forEach(e -> System.out.println(e.getRoadName()));

		assertEquals(pathEdges.get(0).getRoadName(), "i");
		assertEquals(pathEdges.get(1).getRoadName(), "m");
		assertEquals(pathEdges.get(2).getRoadName(), "p");
		assertEquals(pathEdges.get(3).getRoadName(), "n");
	}

	/**
	 * The expected value of this test is calculated based on weight of the test-network,
	 * and the haversine distance formula found on: https://www.movable-type.co.uk/scripts/latlong.html
	 */
	@Test
	public void shortestDistanceTest() {
		ShortestPath sp = shortestPathService.getShortestPath(new Coordinate(3,8), new Coordinate(14,1), TravelType.ALL);
		assertEquals(sp.getDistanceToTravel(), "1771.81 km");
	}

	@Test
	public void directionsTest() {
		ShortestPath sp = shortestPathService.getShortestPath(new Coordinate(3,8), new Coordinate(14,1), TravelType.ALL);
		Vector<NavigationInstruction> instructions = sp.getTravelInstructions();
		assertEquals(instructions.get(0).getType(), NavigationAction.NORTHEAST);
		assertEquals(instructions.get(1).getType(), NavigationAction.TURN_LEFT);
		assertEquals(instructions.get(2).getType(), NavigationAction.DESTINATION);
	}
}
