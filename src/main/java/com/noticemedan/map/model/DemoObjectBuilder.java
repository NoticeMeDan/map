package com.noticemedan.map.model;

import javafx.geometry.Point2D;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DemoObjectBuilder {

	@Getter Map<OSMType,List<DemoObject>> MapObjectsByType;

	public DemoObjectBuilder(){
		MapObjectsByType = new HashMap<>();
		demo();
	}

	private void demo(){
		Roads();
		Highways();
		Sand();
	}

	private void Roads(){
		List<DemoObject> roadsList = new LinkedList<>();

		DemoObject d = new DemoObject();
		d.setOSMType(OSMType.ROAD);
		Point2D dp = new Point2D(100.0,0.0);
		Point2D dp2 = new Point2D(100.0,200.0);
		Point2D dp3 = new Point2D(100.0,400.0);
		Point2D dp4 = new Point2D(100.0,600.0);
		Point2D dp5 = new Point2D(100.0,800.0);
		List<Point2D> pointList = new LinkedList<>();
		pointList.add(dp);
		pointList.add(dp2);
		pointList.add(dp3);
		pointList.add(dp4);
		pointList.add(dp5);
		d.setPoints(pointList);

		DemoObject d2 = new DemoObject();
		d2.setOSMType(OSMType.ROAD);
		Point2D d2p = new Point2D(300.0,0.0);
		Point2D d2p2 = new Point2D(300.0,200.0);
		Point2D d2p3 = new Point2D(300.0,400.0);
		Point2D d2p4 = new Point2D(300.0,600.0);
		Point2D d2p5 = new Point2D(300.0,800.0);
		List<Point2D> pointList2 = new LinkedList<>();
		pointList2.add(d2p);
		pointList2.add(d2p2);
		pointList2.add(d2p3);
		pointList2.add(d2p4);
		pointList2.add(d2p5);
		d2.setPoints(pointList);

		roadsList.add(d);
		roadsList.add(d2);
		MapObjectsByType.put(OSMType.ROAD,roadsList);
	}

	private void Highways(){
		List<DemoObject> highwaysList = new LinkedList<>();

		DemoObject d = new DemoObject();
		d.setOSMType(OSMType.HIGHWAY);
		Point2D dp = new Point2D(600.0,0.0);
		Point2D dp2 = new Point2D(600.0,200.0);
		Point2D dp3 = new Point2D(550.0,400.0);
		Point2D dp4 = new Point2D(500.0,500.0);
		Point2D dp5 = new Point2D(400.0,650.0);
		Point2D dp6 = new Point2D(300.0,700.0);
		Point2D dp7 = new Point2D(200.0,800.0);
		Point2D dp8 = new Point2D(100.0,800.0);
		Point2D dp9 = new Point2D(000.0,800.0);
		List<Point2D> pointList = new LinkedList<>();
		pointList.add(dp);
		pointList.add(dp2);
		pointList.add(dp3);
		pointList.add(dp4);
		pointList.add(dp5);
		pointList.add(dp6);
		pointList.add(dp7);
		pointList.add(dp8);
		pointList.add(dp9);
		d.setPoints(pointList);

		highwaysList.add(d);
		MapObjectsByType.put(OSMType.HIGHWAY,highwaysList);
	}

	private void Sand(){
		List<DemoObject> sandList = new LinkedList<>();

		DemoObject d = new DemoObject();
		d.setOSMType(OSMType.SAND½);
		Point2D dp = new Point2D(610.0,0.0);
		Point2D dp2 = new Point2D(610.0,200.0);
		Point2D dp3 = new Point2D(560.0,400.0);
		Point2D dp4 = new Point2D(510.0,500.0);
		Point2D dp5 = new Point2D(410.0,650.0);
		Point2D dp6 = new Point2D(310.0,710.0);
		Point2D dp7 = new Point2D(200.0,810.0);
		Point2D dp8 = new Point2D(100.0,810.0);
		Point2D dp9 = new Point2D(000.0,810.0);

		Point2D dp10 = new Point2D(0.0,860.0);
		Point2D dp11 = new Point2D(100.0,850.0);
		Point2D dp12 = new Point2D(200.0,850.0);
		Point2D dp13 = new Point2D(310.0,850.0);
		Point2D dp14 = new Point2D(610.0,750.0);
		Point2D dp15 = new Point2D(700.0,650.0);
		Point2D dp16 = new Point2D(750.0,550.0);
		Point2D dp17 = new Point2D(750.0,400.0);
		Point2D dp18 = new Point2D(750.0,000.0);
		List<Point2D> pointList = new LinkedList<>();
		pointList.add(dp);
		pointList.add(dp2);
		pointList.add(dp3);
		pointList.add(dp4);
		pointList.add(dp5);
		pointList.add(dp6);
		pointList.add(dp7);
		pointList.add(dp8);
		pointList.add(dp9);
		pointList.add(dp10);
		pointList.add(dp11);
		pointList.add(dp12);
		pointList.add(dp13);
		pointList.add(dp14);
		pointList.add(dp15);
		pointList.add(dp16);
		pointList.add(dp17);
		pointList.add(dp18);
		d.setPoints(pointList);

		sandList.add(d);
		MapObjectsByType.put(OSMType.SAND,sandList);
	}

}
