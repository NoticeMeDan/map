package com.noticemedan.map.model;

import com.noticemedan.map.data.OsmParser;
import com.noticemedan.map.data.osm.*;
import com.noticemedan.map.model.KDTree.Rect;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Uses OsmParser to read from Osm class and create associated MapObjects
 *
 * This class it generally reaching the limit for Cognitive Complexity
 * TODO This will be divided into smaller classes / sub- and superclasses
 * @Simon
 *
 * @author Simon / Silind
 */

@Slf4j
public class MapObjectCreater implements MapObjectCreaterInterface {
	private static MapObjectCreater instance;
	private static MapObjectProperties mapObjectProperties = new MapObjectProperties();
	private static Collection<List<Point2D>> coastwayCollection = new LinkedList<>();
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
	private List<CoastlineObject> coastlineObjects;
	private Dimension dim;

	private MapObjectCreater(Dimension dim) {
		mapObjectEnumMap = new EnumMap<>(OSMType.class);
		OsmParser osmParser = new OsmParser();
		this.dim = dim;

		initializeCollections(osmParser.getRootNode());
		initializeBoundsAndMapConstants();
		populateObjectCollections();

		coastlineObjects = stichCoastlines(coastwayCollection);
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

	private void populateObjectCollections() {
		relationList.forEach(this::createAndPutMapObjectToCollection);
		wayList.forEach(this::createAndPutMapObjectToCollection);
	}

	private void createAndPutMapObjectToCollection(Relation relation) {
		if (relation == null) return;
		if (relation.getMembers() == null) return;
		for (Member member : relation.getMembers()) {
			if (member.getType().equals("relation"))
				createAndPutMapObjectToCollection(relationMap.get(member.getRef()));
			if (member.getType().equals("way"))
				createAndPutMapObjectToCollection(wayMap.get(member.getRef()));
		}
	}

	private void createAndPutMapObjectToCollection(Way way) {
		if (way == null) return;
		if (way.getNodeRefs() == null) return;
		OSMType type = OSMType.UNKNOWN;
		if (way.getTags() != null)
			for (Tag tag : way.getTags()) {
				if (type.equals(OSMType.UNKNOWN)) type = this.getOSMType(tag);
				else break;
			}

		if (type.equals(OSMType.COASTLINE)) {
			coastwayCollection.add(getPointListFromWay(way));
		} else {
			MapObject mapObject = getMapObjectFromWay(way, type);
			putMapObjectToEnumMap(type, mapObject);
		}
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
		List<Point2D> pointList = getPointListFromWay(way);
		Rect mapObjectBounds = getBoundsFromPointList(pointList);
		double avgX = (mapObjectBounds.getX1() + mapObjectBounds.getX2()) / 2;
		double avgY = (mapObjectBounds.getY1() + mapObjectBounds.getY2()) / 2;

		MapObject mapObject = new MapObject();
		mapObject.setOsmType(type);
		mapObject.setColor(getColor(type));
		mapObject.setPoints(pointList);
		mapObject.setBounds(mapObjectBounds);
		mapObject.setAvgPoint(new Point2D(avgX, avgY));

		return mapObject;
	}

	private Rect getBoundsFromPointList(List<Point2D> pointList) {
		double maxX = 0;
		double maxY = 0;
		double minX = pointList.get(0).getX();
		double minY = pointList.get(0).getY();
		for (Point2D p : pointList) {
			if (p.getX() > maxX) maxX = p.getX();
			if (p.getY() > maxY) maxY = p.getY();
			if (p.getX() < minX) minX = p.getX();
			if (p.getY() < minY) minY = p.getY();
		}

		return new Rect(minX, minY, maxX, maxY);
	}

	/**
	 * currently exciding the alowed Cognitive Complexity level
	 * TODO Refactor and simplify
	 *
	 * @Simon
	 */
	public List<CoastlineObject> stichCoastlines(Collection<List<Point2D>> coastlines) {
		List<CoastlineObject> listOfCoastlines = new LinkedList<>();

		List<Point2D> previousLine = null;
		List<Point2D> stichedPoints = new LinkedList<>();

		int counter = 0;
		for (List<Point2D> line : coastlines) {
			counter++;
			if (previousLine != null) {

				if (!previousLine.get(0).equals(line.get(line.size() - 1))) {
					CoastlineObject coastlineObject = createCoastlineObject(stichedPoints);
					if (coastlineObject != null) listOfCoastlines.add(coastlineObject);
					stichedPoints.clear();
					previousLine = line;
					continue;
				}

				if (stichedPoints.isEmpty()) stichedPoints.addAll(0, previousLine);
				stichedPoints.addAll(0, line);

				if (counter == coastlines.size()) {
					CoastlineObject coastlineObject = createCoastlineObject(stichedPoints);
					if (coastlineObject != null) listOfCoastlines.add(coastlineObject);
				}
			}
			previousLine = line;
		}
		return listOfCoastlines;
	}

	public CoastlineObject createCoastlineObject(List<Point2D> pointlist) {
		if (pointlist.isEmpty()) return null;
		CoastlineObject coastlineObject = new CoastlineObject();
		coastlineObject.setPoints(new LinkedList<>(pointlist));
		coastlineObject.setColor(getColor(OSMType.COASTLINE));
		Rect coastlineBounds = getBoundsFromPointList(pointlist);
		double avgX = (coastlineBounds.getX1() + coastlineBounds.getX2()) / 2;
		double avgY = (coastlineBounds.getY1() + coastlineBounds.getX2()) / 2;
		coastlineObject.setBounds(coastlineBounds);
		coastlineObject.setAvgPoint(new Point2D(avgX, avgY));

		return coastlineObject;
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
		// TODO Implement correct mercator projection
		/*return (((lon + 180) * (dim.getWidth() / 360)) * xFactor) - topLeftLon;*/
		return (lon * xFactor) - topLeftLon;
	}

	private double scaleLat(double lat) {
		// TODO Implement correct mercator projection
		/*double latRad = lat * Math.PI / 180;
		double mercN = Math.log(Math.tan((Math.PI / 4) + (latRad / 2)));
		return -(((dim.getHeight() / 2) - (dim.getWidth() * mercN / (2 * Math.PI)) * yFactor) - topLeftLat);*/
		return -((-lat * yFactor) - topLeftLat);
	}

	private OSMType getOSMType(Tag t) {
		return mapObjectProperties.derriveOSMTypeFromTag(t);
	}

	private Color getColor(OSMType t) {
		return mapObjectProperties.deriveColorFromOSMType(t);
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
		printSizeOfMapObjectList(OSMType.TREE_ROW);
		printSizeOfMapObjectList(OSMType.HEATH);
		printSizeOfMapObjectList(OSMType.UNKNOWN);

		log.info(this.coastlineObjects.toString());
	}

	@Override
	public Map<OSMType, List<MapObject>> getMapObjectsByType() {
		return this.mapObjectEnumMap;
	}

	@Override
	public List<CoastlineObject> getListOfCoastlineObjects() {
		return this.coastlineObjects;
	}

}
