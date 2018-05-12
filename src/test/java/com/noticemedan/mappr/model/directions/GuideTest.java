package com.noticemedan.mappr.model.directions;

import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.model.pathfinding.PathEdge;
import com.noticemedan.mappr.model.service.ShortestPathService;
import io.vavr.collection.Vector;
import org.testng.annotations.Test;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class GuideTest {

	private final DomainFacade domain = new DomainFacade();
	private Guide guide = new Guide();
	private Vector<PathEdge> route = domain.getRandomSP();

@Test
	void testDirections() {
		for(String s : guide.getDirections(route)) {
			System.out.println(s);
		}
		System.out.println();
		System.out.println("Distance: " + this.guide.getTraveleddistance());
	}/*

/*
	@Test
	void testAngle() {
		Point2D.Double p1 = new Point2D.Double(5.0,5.0);
		Point2D.Double p2 = new Point2D.Double(0.0,5.0);
		Point2D.Double p3 = new Point2D.Double(5.0,10.0);
		Point2D.Double p4 = new Point2D.Double(10.0,5.0);
		Point2D.Double p5 = new Point2D.Double(5.0,0.0);

		Line2D.Double east = new Line2D.Double(p2,p1); //EAST
		Line2D.Double west = new Line2D.Double(p4,p1); //WEST
		Line2D.Double north = new Line2D.Double(p5,p1); //NORTH
		Line2D.Double south = new Line2D.Double(p3,p1); //SOUTH

		Line2D.Double east2 = new Line2D.Double(p1,p4); //SOUTH
		Line2D.Double west2 = new Line2D.Double(p1,p2); //SOUTH
		Line2D.Double north2 = new Line2D.Double(p1,p3); //SOUTH
		Line2D.Double south2 = new Line2D.Double(p1,p5); //SOUTH



		System.out.println(Guide.angleBetweenLines(north, west) + " : north, east2");
		System.out.println(Guide.angleBetweenLines(north, west) + " : north, west2");
	}*/


}
