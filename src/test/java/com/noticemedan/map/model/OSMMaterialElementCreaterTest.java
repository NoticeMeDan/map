//package com.noticemedan.map.model;
//
//import javafx.geometry.Point2D;
//import lombok.extern.slf4j.Slf4j;
//import org.testng.annotations.Test;
//
//import java.awt.*;
//import java.util.Collection;
//import java.util.LinkedList;
//import java.util.List;
//
//@Slf4j
//public class OSMMaterialElementCreaterTest {
//	OSMElementCreator osmElementCreator = OSMElementCreator.getInstance(new Dimension(1600, 1600));
//
//	public Collection<List<Point2D>> createTestLand() {
//		Collection<List<Point2D>> land = new LinkedList<>();
//
//		List<Point2D> line1 = new LinkedList<>();
//		line1.add(new Point2D(8,10));
//		line1.add(new Point2D(6,11));
//		line1.add(new Point2D(4,6));
//
//		List<Point2D> line2 = new LinkedList<>();
//		line2.add(new Point2D(12,7));
//		line2.add(new Point2D(14,9));
//		line2.add(new Point2D(12,12));
//		line2.add(new Point2D(8,10));
//
//		List<Point2D> line3 = new LinkedList<>();
//		line3.add(new Point2D(6,2));
//		line3.add(new Point2D(7,5));
//		line3.add(new Point2D(14,4));
//		line3.add(new Point2D(12,7));
//
//		List<Point2D> line4 = new LinkedList<>();
//		line4.add(new Point2D(11,4));
//		line4.add(new Point2D(10,3));
//		line4.add(new Point2D(10,2));
//
//		List<Point2D> line5 = new LinkedList<>();
//		line5.add(new Point2D(13,1));
//		line5.add(new Point2D(13,3));
//		line5.add(new Point2D(12,3));
//		line5.add(new Point2D(11,4));
//
//		List<Point2D> line6 = new LinkedList<>();
//		line6.add(new Point2D(11,1));
//		line6.add(new Point2D(12,2));
//		line6.add(new Point2D(13,1));
//
//		List<Point2D> line7 = new LinkedList<>();
//		line7.add(new Point2D(5,10));
//		line7.add(new Point2D(2,13));
//		line7.add(new Point2D(1,12));
//
//		List<Point2D> line8 = new LinkedList<>();
//		line8.add(new Point2D(3,9));
//		line8.add(new Point2D(3,8));
//		line8.add(new Point2D(5,10));
//
//		land.add(line1);
//		land.add(line2);
//		land.add(line3);
//		land.add(line4);
//		land.add(line5);
//		land.add(line6);
//		land.add(line7);
//		land.add(line8);
//
//		return land;
//	}
//
//	@Test
//	public void testStichCoastlines() throws Exception {
//		List<OSMCoastlineElement> testList = osmElementCreator.stitchCoastlines(createTestLand());
//		log.info("TestList size: " + testList.size());
//		testList.forEach(l -> {
//			System.out.println("OSMCoastlineElement: " + l.streetAndHouseToString());
//			l.getPoints().forEach(System.out::println);
//		});
//	}
//}
