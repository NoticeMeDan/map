package com.noticemedan.map.model.model;

import com.noticemedan.map.data.io.XMLMapData;
import com.noticemedan.map.data.osm.*;
import com.noticemedan.map.view.App;
import io.vavr.control.Try;
import javafx.geometry.Point2D;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

@Slf4j
public class MapObjectBuilder implements MapObjectBuilderInterface {
	private static MapObjectBuilder instance;
	private Bounds bounds;
	private static Map<Long, Node> nodeMap;
	private static Map<Long, Way> wayMap;
	private static Map<Long, Relation> relationMap;
	private static List<Way> wayList;
	private static List<Relation> relationList;
	private EnumMap<OSMType, List<MapObject>> mapObjectEnumMap;
	private double minLon, minLat, maxLon, maxLat;
	private double lonFactor, averageLat, xFactor, yFactor;
	private double topLeftLon, topLeftLat;
	private static Dimension dim;

	private MapObjectBuilder(Dimension dim) {
		mapObjectEnumMap = new EnumMap<>(OSMType.class);
		this.dim = dim;

		XMLMapData xmlMapper = new XMLMapData();

		ZipInputStream inputStream = new ZipInputStream(App.class.getResourceAsStream("/small.osm.zip"));
		Try.of(inputStream::getNextEntry)
				.onFailure(x -> log.error("Error while getting entry in zip file", x))
				.getOrNull();

		Osm rootNode = xmlMapper.deserialize(inputStream);

		initializeCollections(rootNode);
		initializeBoundsAndMapConstants();
		populateMapObjectEnumMap();

		log.info("# of Nodes: " + rootNode.getNodes().size());
	}

	private void initializeCollections(Osm rootNode) {
		bounds = rootNode.getBounds();
		wayMap = rootNode.getWays().stream().collect(Collectors.toMap(Way::getId, x -> x));
		relationMap = rootNode.getRelations().stream().collect(Collectors.toMap(Relation::getId, x -> x));
		nodeMap = rootNode.getNodes().stream().collect(Collectors.toMap(Node::getId, x -> x));
		wayList = rootNode.getWays();
		relationList = rootNode.getRelations();
	}

	public static MapObjectBuilder getInstance(Dimension dim) {
		if (instance == null) instance = new MapObjectBuilder(dim);
		return instance;
	}

	private void initializeBoundsAndMapConstants() {
		minLon = bounds.getMinlon();
		minLat = bounds.getMinlat();
		maxLon = bounds.getMaxlon();
		maxLat = bounds.getMaxlat();
		averageLat = minLat + (maxLat - minLat) / 2;
		lonFactor = Math.cos(averageLat / 180 * Math.PI);
		minLon *= lonFactor;
		minLat = -minLat;
		maxLon *= lonFactor;
		maxLat = -maxLat;
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
			switch (member.getType()) {
				case "relation":
					createAndPutMapObjectToEnumMap(relationMap.get(member.getRef()));
					break;
				case "way":
					createAndPutMapObjectToEnumMap(wayMap.get(member.getRef()));
					break;
				default:
					break;
			}
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
		return (lon * lonFactor * xFactor) - topLeftLon;
	}

	private double scaleLat(double lat) {
		return -((-lat * yFactor) - topLeftLat);
	}

	private OSMType getOSMType(Tag t) {
		log.info(t.getK() + " : " + t.getV());
		OSMType osmType = OSMType.UNKNOWN;
		switch (t.getK()) {
			case "building":
				osmType = OSMType.BUILDING;
				break;
			case "natural":
				switch (t.getV()) {
					case "water":
						osmType = OSMType.WATER;
						break;
					case "tree":
						osmType = OSMType.TREE;
						break;
					case "grassland":
						osmType = OSMType.GRASSLAND;
						break;
					case "sand":
						osmType = OSMType.SAND;
						break;
					case "tree_row":
						osmType = OSMType.TREE_ROW;
						break;
					case "heath":
						osmType = OSMType.HEATH;
						break;
					case "coastline":
						osmType = OSMType.COASTLINE;
						break;
				}
				break;
			case "leisure":
				switch (t.getV()) {
					case "playground":
						osmType = OSMType.PLAYGROUND;
						break;
					case "garden":
						osmType = OSMType.GARDEN;
						break;
					case "park":
						osmType = OSMType.PARK;
						break;
				}
				break;
			case "highway":
				if (t.getV().equals("highway")) osmType = OSMType.HIGHWAY;
				else osmType = OSMType.ROAD;
			}
		return osmType;
	}

	// For  testing purposes
	private void printSizeOfMapObjectList(OSMType type) {
		if (mapObjectEnumMap.containsKey(type))
			log.info(type + ": " + mapObjectEnumMap.get(type).size());
		else {
			log.info(type + ": No list");
		}
	}

	// For  testing purposes
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
