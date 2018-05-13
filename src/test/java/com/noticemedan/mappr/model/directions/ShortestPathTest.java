package com.noticemedan.mappr.model.directions;

import com.noticemedan.mappr.model.NavigationAction;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.map.Type;
import com.noticemedan.mappr.model.pathfinding.PathEdge;
import com.noticemedan.mappr.model.pathfinding.ShortestPath;
import com.noticemedan.mappr.model.pathfinding.TravelType;
import com.noticemedan.mappr.model.service.ShortestPathService;
import com.noticemedan.mappr.model.util.Coordinate;
import io.vavr.collection.Vector;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.awt.geom.Line2D;

import static org.testng.Assert.assertEquals;

/**
 * A picture of the test-network can be seen here:
 * https://drive.google.com/open?id=1KDfRC9EY_YWrHFAnHF7Gh045vMCkorBb
 */
public class ShortestPathTest {
	ShortestPathService shortestPathService;

	@BeforeTest
	void prepareNetwork() {
		Vector<Element> elements = Vector.empty();
		elements = elements.append(createElement(new Coordinate(3, 8), new Coordinate(4, 3), "f"));
		elements = elements.append(createElement(new Coordinate(4, 3), new Coordinate(7, 7), "l"));
		elements = elements.append(createElement(new Coordinate(3, 8), new Coordinate(7, 7), "i"));
		elements = elements.append(createElement(new Coordinate(3, 8), new Coordinate(5, 6), "g"));
		elements = elements.append(createElement(new Coordinate(5, 6), new Coordinate(7, 7), "h"));
		elements = elements.append(createElement(new Coordinate(7, 7), new Coordinate(9, 8), "k"));
		elements = elements.append(createElement(new Coordinate(7, 7), new Coordinate(12, 4), "j"));
		elements = elements.append(createElement(new Coordinate(7, 7), new Coordinate(8, 1), "m"));
		elements = elements.append(createElement(new Coordinate(8, 1), new Coordinate(14, 2), "p"));
		elements = elements.append(createElement(new Coordinate(9, 8), new Coordinate(14, 2), "q"));
		elements = elements.append(createElement(new Coordinate(14, 2), new Coordinate(14, 1), "n"));
		shortestPathService = new ShortestPathService(elements);
		printAllNodes();
		printAllEdges();
	}

	private Element createElement(Coordinate c1, Coordinate c2, String name) {
		Element element = new Element();
		element.setType(Type.ROAD);
		element.setShape(new Line2D.Double(c1, c2));
		element.setName(name);
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
	public void shortestPathTest() {
		Vector<PathEdge> pathEdges = shortestPathService.getShortestPath(shortestPathService.getAllNodes().get(0), shortestPathService.getAllNodes().get(8), TravelType.ALL);
		assertEquals(pathEdges.get(0).getRoadName(), "n");
		assertEquals(pathEdges.get(1).getRoadName(), "q");
		assertEquals(pathEdges.get(2).getRoadName(), "k");
		assertEquals(pathEdges.get(3).getRoadName(), "i");
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
		assertEquals(instructions.get(0).type, NavigationAction.SOUTHEAST);
		assertEquals(instructions.get(1).type, NavigationAction.NORTHEAST);
		assertEquals(instructions.get(2).type, NavigationAction.SOUTH);
	}
}
