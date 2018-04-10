package com.noticemedan.map.model;

import com.noticemedan.map.data.OSMParser;
import com.noticemedan.map.data.osm.*;
import com.noticemedan.map.model.Utilities.Rect;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Uses OSMParser to read from OSMRootNode class and create associated MapObjects
 *
 * This class it generally reaching the limit for Cognitive Complexity
 * TODO This will be divided into smaller classes / sub- and superclasses
 * @Simon
 *
 * @author Simon / Silind
 */

@Slf4j
public class OSMElementCreator implements OSMElementCreatorInterface {
	private static OSMElementCreator instance;
	private static OSMElementProperties mapObjectProperties = new OSMElementProperties();
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
	private EnumMap<OSMType, List<OSMMaterialElement>> mapObjectEnumMap;
	private List<OSMCoastlineElement> coastlineObjects;
	private Dimension dim;

	private OSMElementCreator(Dimension dim) {
		mapObjectEnumMap = new EnumMap<>(OSMType.class);
		OSMParser osmParser = new OSMParser();
		this.dim = dim;

		initializeCollections(osmParser.getRootNode());
		initializeBoundsAndMapConstants();
		populateObjectCollections();

		coastlineObjects = stitchCoastlines(coastwayCollection);
	}

	private static void initializeCollections(OSMRootNode rootNode) {
		bounds = rootNode.getBounds();
		wayMap = rootNode.getWays().stream().collect(Collectors.toMap(Way::getId, x -> x));
		relationMap = rootNode.getRelations().stream().collect(Collectors.toMap(Relation::getId, x -> x));
		nodeMap = rootNode.getNodes().stream().collect(Collectors.toMap(Node::getId, x -> x));
		wayList = rootNode.getWays();
		relationList = rootNode.getRelations();
	}

	public static OSMElementCreator getInstance(Dimension dim) {
		if (instance == null) instance = new OSMElementCreator(dim);
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
			OSMMaterialElement osmMaterialElement = getMapObjectFromWay(way, type);
			putMapObjectToEnumMap(type, osmMaterialElement);
		}
	}


	private void putMapObjectToEnumMap(OSMType key, OSMMaterialElement osmMaterialElement) {
		if (mapObjectEnumMap.containsKey(key))
			mapObjectEnumMap.get(key).add(osmMaterialElement);
		else {
			List<OSMMaterialElement> osmMaterialElementList = new LinkedList<>();
			osmMaterialElementList.add(osmMaterialElement);
			mapObjectEnumMap.put(key, osmMaterialElementList);
		}
	}

	private OSMMaterialElement getMapObjectFromWay(Way way, OSMType type) {
		List<Point2D> pointList = getPointListFromWay(way);
		Rect mapObjectBounds = getBoundsFromPointList(pointList);
		double avgX = (mapObjectBounds.getX1() + mapObjectBounds.getX2()) / 2;
		double avgY = (mapObjectBounds.getY1() + mapObjectBounds.getY2()) / 2;

		OSMMaterialElement osmMaterialElement = new OSMMaterialElement();
		osmMaterialElement.setOsmType(type);
		osmMaterialElement.setColor(getColor(type));
		osmMaterialElement.setPoints(pointList);
		osmMaterialElement.setBounds(mapObjectBounds);
		osmMaterialElement.setAvgPoint(new Point2D(avgX, avgY));

		return osmMaterialElement;
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
	public List<OSMCoastlineElement> stitchCoastlines(Collection<List<Point2D>> coastlines) {
		List<OSMCoastlineElement> listOfCoastlines = new LinkedList<>();

		List<Point2D> previousLine = null;
		List<Point2D> stichedPoints = new LinkedList<>();

		int counter = 0;
		for (List<Point2D> line : coastlines) {
			counter++;
			if (previousLine != null) {

				if (!previousLine.get(0).equals(line.get(line.size() - 1))) {
					OSMCoastlineElement coastlineObject = createCoastlineObject(stichedPoints);
					if (coastlineObject != null) listOfCoastlines.add(coastlineObject);
					stichedPoints.clear();
					previousLine = line;
					continue;
				}

				if (stichedPoints.isEmpty()) stichedPoints.addAll(0, previousLine);
				stichedPoints.addAll(0, line);

				if (counter == coastlines.size()) {
					OSMCoastlineElement coastlineObject = createCoastlineObject(stichedPoints);
					if (coastlineObject != null) listOfCoastlines.add(coastlineObject);
				}
			}
			previousLine = line;
		}
		return listOfCoastlines;
	}

	public OSMCoastlineElement createCoastlineObject(List<Point2D> pointlist) {
		if (pointlist.isEmpty()) return null;
		OSMCoastlineElement coastlineObject = new OSMCoastlineElement();
		coastlineObject.setPoints(new LinkedList<>(pointlist));
		coastlineObject.setColor(getColor(OSMType.COASTLINE));
		Rect coastlineBounds = getBoundsFromPointList(pointlist);
		coastlineObject.setBounds(coastlineBounds);
		coastlineObject.setAvgPoint(coastlineBounds.getAveragePoint());

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
	public Map<OSMType, List<OSMMaterialElement>> getMapObjectsByType() {
		return this.mapObjectEnumMap;
	}

	@Override
	public List<OSMCoastlineElement> getListOfCoastlineObjects() {
		return this.coastlineObjects;
	}

}
