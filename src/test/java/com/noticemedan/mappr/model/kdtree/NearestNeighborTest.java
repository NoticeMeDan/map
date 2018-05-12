package com.noticemedan.mappr.model.kdtree;

import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.map.Type;
import com.noticemedan.mappr.model.pathfinding.TravelType;
import com.noticemedan.mappr.model.service.ForestService;
import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.model.util.Rect;
import io.vavr.collection.Vector;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.awt.geom.Path2D;

import static org.testng.Assert.*;


public class NearestNeighborTest {
	ForestService forestService;

	@BeforeTest
	public void setUpForestService() {
		Element motorway = new Element();
		Path2D motorwayShape = new Path2D.Double();
		motorwayShape.moveTo(1,1);
		motorwayShape.lineTo(2,3);
		motorwayShape.lineTo(3,5);
		motorway.setShape(motorwayShape);
		motorway.setAvgPoint(new Coordinate(3,2));
		motorway.setType(Type.MOTORWAY);

		Element footway = new Element();
		Path2D footwayShape = new Path2D.Double();
		footwayShape.moveTo(4,4);
		footwayShape.lineTo(5,4);
		footway.setShape(footwayShape);
		footway.setAvgPoint(new Coordinate(4.5,4));
		footway.setType(Type.FOOTWAY);

		Element cycleway = new Element();
		Path2D cyclewayShape = new Path2D.Double();
		cyclewayShape.moveTo(5,2);
		cyclewayShape.lineTo(6,2);
		cyclewayShape.lineTo(7,2);
		cycleway.setShape(cyclewayShape);
		cycleway.setAvgPoint(new Coordinate(6,2));
		cycleway.setType(Type.CYCLEWAY);

		Element roadway = new Element();
		Path2D roadwayShape = new Path2D.Double();
		roadwayShape.moveTo(6,6);
		roadwayShape.lineTo(7,5);
		roadwayShape.lineTo(8,4);
		roadway.setShape(roadwayShape);
		roadway.setAvgPoint(new Coordinate(7,5));
		roadway.setType(Type.ROAD);

		Vector<Element> coastlines = Vector.empty();
		Vector<Element> elements = Vector.empty();
		elements = elements.append(motorway);
		elements = elements.append(footway);
		elements = elements.append(cycleway);
		elements = elements.append(roadway);

		forestService = new ForestService(elements, coastlines);
	}


	@Test
	public void testNearestNeighbor_All_Positive1() {
		forestService.rangeSearch(new Rect(0,0,10,10), Double.POSITIVE_INFINITY);
		Element element = forestService.nearestNeighborInCurrentRangeSearch(new Coordinate(4,3), TravelType.ALL);
		assertEquals(Type.FOOTWAY, element.getType());
	}

	@Test
	public void testNearestNeighbor_All_Positive2() {
		forestService.rangeSearch(new Rect(0,0,10,10), Double.POSITIVE_INFINITY);
		Element element = forestService.nearestNeighborInCurrentRangeSearch(new Coordinate(1,4), TravelType.ALL);
		assertEquals(Type.MOTORWAY, element.getType());
	}

	@Test
	public void testNearestNeighbor_Walk_Positive() {
		forestService.rangeSearch(new Rect(0,0,10,10), Double.POSITIVE_INFINITY);
		Element element = forestService.nearestNeighborInCurrentRangeSearch(new Coordinate(1,4), TravelType.WALK);
		assertEquals(Type.FOOTWAY, element.getType());
	}

	@Test
	public void testNearestNeighbor_Bike_Positive1() {
		forestService.rangeSearch(new Rect(0,0,10,10), Double.POSITIVE_INFINITY);
		Element element = forestService.nearestNeighborInCurrentRangeSearch(new Coordinate(3,1), TravelType.BIKE);
		assertEquals(Type.CYCLEWAY, element.getType());
	}

	@Test
	public void testNearestNeighbor_Bike_Positive2() {
		forestService.rangeSearch(new Rect(0,0,10,10), Double.POSITIVE_INFINITY);
		Element element = forestService.nearestNeighborInCurrentRangeSearch(new Coordinate(7,6), TravelType.BIKE);
		assertEquals(Type.ROAD, element.getType());
	}

	@Test
	public void testExpandingRectNearestNeighborNode_All_Positive1() {
		Coordinate coordinate = forestService.nearestNode(new Coordinate(7,5.5), TravelType.ALL);
		assertEquals(coordinate.getX(), 7.0);
		assertEquals(coordinate.getY(), 5.0);
	}

	@Test
	public void testExpandingRectNearestNeighborNode_Car_Positive1() {
		Coordinate coordinate = forestService.nearestNode(new Coordinate(-100,50), TravelType.CAR);
		assertEquals(coordinate.getX(), 1.0);
		assertEquals(coordinate.getY(), 1.0);
	}

	@Test
	public void testExpandingRectNearestNeighborNode_Bike_Positive1() {
		Coordinate coordinate = forestService.nearestNode(new Coordinate(-100,50), TravelType.BIKE);
		assertEquals(coordinate.getX(), 5.0);
		assertEquals(coordinate.getY(), 2.0);
	}
}
