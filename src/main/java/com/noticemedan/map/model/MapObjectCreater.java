package com.noticemedan.map.model;

import com.noticemedan.map.data.OsmParser;
import com.noticemedan.map.data.osm.*;
import javafx.geometry.Point2D;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Uses OsmParser to read from Osm class and create associated MapObjects
 * @author Simon / Silind
 */

@Slf4j
public class MapObjectCreater implements MapObjectCreaterInterface {
	private static MapObjectCreater instance;
	private static Bounds bounds;
	private static Map<Long, Node> nodeMap;
	private static Map<Long, Way> wayMap;
	private static Map<Long, Relation> relationMap;
	private static List<Way> wayList;
	private static List<Relation> relationList;
	private double xFactor;
	private double yFactor;
	private double topLeftLon;
	private double topLeftLat;
	private EnumMap<OSMType, List<MapObject>> mapObjectEnumMap;
	private Dimension dim;

	private MapObjectCreater(Dimension dim) {
		mapObjectEnumMap = new EnumMap<>(OSMType.class);
		OsmParser osmParser = new OsmParser();
		this.dim = dim;

		initializeCollections(osmParser.getRootNode());
		initializeBoundsAndMapConstants();
		populateMapObjectEnumMap();
	}

	private static void initializeCollections(Osm rootNode) {
		bounds = rootNode.getBounds();
		wayMap = rootNode.getWays().stream().collect(Collectors.toMap(Way::getId, x -> x));
		relationMap = rootNode.getRelations().stream().collect(Collectors.toMap(Relation::getId, x -> x));
		nodeMap = rootNode.getNodes().stream().collect(Collectors.toMap(Node::getId, x -> x));
		wayList = rootNode.getWays();
		relationList = rootNode.getRelations();
	}

	public static MapObjectCreater getInstance(Dimension dim) {
		if (instance == null) instance = new MapObjectCreater(dim);
		return instance;
	}

	private void initializeBoundsAndMapConstants() {
		double minLon = bounds.getMinlon();
		double minLat = -bounds.getMinlat();
		double maxLon = bounds.getMaxlon();
		double maxLat = -bounds.getMaxlat();
		xFactor = dim.getWidth() / (maxLon - minLon);
		yFactor = dim.getHeight() / (maxLat - minLat);
		topLeftLon = minLon * xFactor;
		topLeftLat = maxLat * yFactor;
	}

	private void populateMapObjectEnumMap() {
		relationList.forEach(this::createAndPutMapObjectToEnumMap);
		wayList.forEach(this::createAndPutMapObjectToEnumMap);
	}

	private void createAndPutMapObjectToEnumMap(Relation relation) {
		if (relation == null) return;
		if (relation.getMembers() == null) return;
		for (Member member : relation.getMembers()) {
			if (member.getType().equals("relation"))
				createAndPutMapObjectToEnumMap(relationMap.get(member.getRef()));
			if (member.getType().equals("way"))
				createAndPutMapObjectToEnumMap(wayMap.get(member.getRef()));
		}
	}

	private void createAndPutMapObjectToEnumMap(Way way) {
		if (way == null) return;
		if (way.getNodeRefs() == null) return;
		OSMType type = OSMType.UNKNOWN;
		if (way.getTags() != null) {
			for (Tag tag : way.getTags()) {
				if (type.equals(OSMType.UNKNOWN)) type = this.getOSMType(tag);
				else break;
			}
		}
		MapObject mapObject = getMapObjectFromWay(way, type);
		putMapObjectToEnumMap(type, mapObject);
	}

	private void putMapObjectToEnumMap(OSMType key, MapObject mapObject) {
		if (mapObjectEnumMap.containsKey(key))
			mapObjectEnumMap.get(key).add(mapObject);
		else {
			List<MapObject> mapObjectList = new LinkedList<>();
			mapObjectList.add(mapObject);
			mapObjectEnumMap.put(key, mapObjectList);
		}
	}

	private MapObject getMapObjectFromWay(Way way, OSMType type) {
		MapObject mapObject = new MapObject();
		mapObject.setOsmType(type);
		mapObject.setPoints(getPointListFromWay(way));
		return mapObject;
	}

	private List<Point2D> getPointListFromWay(Way way) {
		List<Point2D> point2DList = new LinkedList<>();
		way.getNodeRefs().forEach(nr -> {
			Node node = nodeMap.get(nr.getRef());
			Point2D point = new Point2D(scaleLon(node.getLon()), scaleLat(node.getLat()));
			point2DList.add(point);
		});
		return point2DList;
	}

	private double scaleLon(double lon) {
		// TODO Mercator stuff
		/*return (((lon + 180) * (dim.getWidth() / 360)) * xFactor) - topLeftLon;*/
		return (lon * xFactor) - topLeftLon;
	}

	private double scaleLat(double lat) {
		// TODO Mercator stuff
		/*double latRad = lat * Math.PI / 180;
		double mercN = Math.log(Math.tan((Math.PI / 4) + (latRad / 2)));
		return -(((dim.getHeight() / 2) - (dim.getWidth() * mercN / (2 * Math.PI)) * yFactor) - topLeftLat);*/
		return -((-lat * yFactor) - topLeftLat);
	}

	// TODO This mess is gonna get cleaned up
	// @Simon
	private OSMType getOSMType(Tag t) {
		if (t.getK().equals("building"))
			return OSMType.BUILDING;
		if (t.getK().equals("natural")) {
			if (t.getV().equals("water"))
				return OSMType.WATER;
			if (t.getV().equals("tree"))
				return OSMType.TREE;
			if (t.getV().equals("grassland"))
				return OSMType.GRASSLAND;
			if (t.getV().equals("sans"))
				return OSMType.SAND;
			if (t.getV().equals("tree_row"))
				return OSMType.TREE_ROW;
			if (t.getV().equals("heath"))
				return OSMType.HEATH;
			if (t.getV().equals("coastline"))
				return OSMType.COASTLINE;
		}
		if (t.getK().equals("leisure")) {
			if (t.getV().equals("playground"))
				return OSMType.PLAYGROUND;
			if (t.getV().equals("garden"))
				return OSMType.GARDEN;
			if (t.getV().equals("park"))
				return OSMType.PARK;
		}
		if (t.getK().equals("highway")) {
			if (t.getV().equals("highway"))
				return OSMType.HIGHWAY;
			else
				return OSMType.ROAD;
		}
		return OSMType.UNKNOWN;
	}

	/**
	 * For testing purposes
	 * Prints the amount of MapOjects associated to a given OSMType
	 */
	private void printSizeOfMapObjectList(OSMType type) {
		if (mapObjectEnumMap.containsKey(type))
			log.info(type + ": " + mapObjectEnumMap.get(type).size());
		else {
			log.info(type + ": No list");
		}
	}

	/**
	 * For testing purposes
	 * Alter this method as needed
	 */
	public void writeOut() {
		printSizeOfMapObjectList(OSMType.ROAD);
		printSizeOfMapObjectList(OSMType.HIGHWAY);
		printSizeOfMapObjectList(OSMType.WATER);
		printSizeOfMapObjectList(OSMType.TREE);
		printSizeOfMapObjectList(OSMType.GRASSLAND);
		printSizeOfMapObjectList(OSMType.SAND);
		printSizeOfMapObjectList(OSMType.BUILDING);
		printSizeOfMapObjectList(OSMType.COASTLINE);
		printSizeOfMapObjectList(OSMType.TREE_ROW);
		printSizeOfMapObjectList(OSMType.HEATH);
		printSizeOfMapObjectList(OSMType.UNKNOWN);
	}

	@Override
	public Map<OSMType, List<MapObject>> getMapObjectsByType() {
		return mapObjectEnumMap;
	}

}
