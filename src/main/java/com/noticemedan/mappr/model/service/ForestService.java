package com.noticemedan.mappr.model.service;

import com.noticemedan.mappr.model.kdtree.ForestInterface;
import com.noticemedan.mappr.model.kdtree.KdTree;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.pathfinding.TravelType;
import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.model.util.ElementTransform;
import com.noticemedan.mappr.model.util.Rect;
import io.vavr.collection.Vector;
import lombok.extern.slf4j.Slf4j;

import java.awt.geom.PathIterator;
import java.util.ArrayList;

@Slf4j
public class ForestService implements ForestInterface {
	private KdTree trees[];
	private ArrayList<ArrayList<Element>> coastlines;
	private Vector<Element> currentRangeSearch;
	private int N = 5; // Zoom-levels of elements in map
	private int C = 4; // Number of coastline resolutions

	public ForestService(Vector<Element> elements, Vector<Element> coastlineElements) {
		coastlines = new ArrayList<>();
		for (int i = 0; i < C; i++) coastlines.add(new ArrayList<>());
		coastlines.get(0).addAll(coastlineElements.toJavaList());
		sortElementsIntoZoomLevels(elements);
		createCoastlineResolutions(coastlineElements);
	}

	public Vector<Element> getCoastlines(double zoomLevel) {
		Vector<Element> coastlinesAtZoomLevel = Vector.empty();
		if 		(0.2 > zoomLevel && zoomLevel >= 0.0) return coastlinesAtZoomLevel = coastlinesAtZoomLevel.appendAll(coastlines.get(C-1));
		else if (0.45 > zoomLevel && zoomLevel >= 0.2) return coastlinesAtZoomLevel = coastlinesAtZoomLevel.appendAll(coastlines.get(C-2));
		else if (3 > zoomLevel && zoomLevel >= 0.45) return coastlinesAtZoomLevel = coastlinesAtZoomLevel.appendAll(coastlines.get(C-3));
		return coastlinesAtZoomLevel = coastlinesAtZoomLevel.appendAll(coastlines.get(0));
	}

	private void createCoastlineResolutions(Vector<Element> coastlineElements) {
		for (Element coastline : coastlineElements) {
			coastlines.get(1).add(ElementTransform.elementToLowerResolution(coastline, 10));
			coastlines.get(2).add(ElementTransform.elementToLowerResolution(coastline, 50));
			coastlines.get(3).add(ElementTransform.elementToLowerResolution(coastline, 75));
		}
	}

	private void sortElementsIntoZoomLevels(Vector<Element> elements) {
		ArrayList<ArrayList<Element>> kdTreeLevels = new ArrayList<>();
		for (int i = 0; i < N; i++) kdTreeLevels.add(new ArrayList<>());

		for (Element element : elements) {
			if (element.isRoad()) {
				ArrayList<Element> identicalRoads = ElementTransform.determineRoadMultiplicity(element);
				int zoomLevel = determineElementZoomLevelPosition(element);
				if (zoomLevel > -1) for(Element road : identicalRoads) kdTreeLevels.get(zoomLevel).add(road);
			} else {
				ArrayList<Element> identicalElements = ElementTransform.determineELementMultiplicity(element);
				int zoomLevel = determineElementZoomLevelPosition(element);
				if (zoomLevel > -1) for(Element identicalElement : identicalElements) kdTreeLevels.get(zoomLevel).add(identicalElement);
			}
		}
		constructKdTrees(kdTreeLevels);
	}

	public int determineElementZoomLevelPosition(Element element) {
		switch (element.getType()) {
			case MOTORWAY:
			case PRIMARY:
			case TRUNK:
				return 0;
			case SECONDARY:
			case TERTIARY:
				return 1;
			case GRASSLAND:
			case FOREST:
			case WATER:
			case HEATH:
			case PARK:
			case MOTORWAY_LINK:
			case AERODROME:
			case RUNWAY:
				return 2;
			case TAXIWAY:
			case ROAD:
			case RAIL:
			case RESIDENTIAL:
			case CYCLEWAY:
			case UNCLASSIFIED:
			case SERVICE:
				return 3;
			case PATH:
			case TRACK:
			case BUILDING:
			case PLAYGROUND:
			case FOOTWAY:
			case FOOTPATH:
			case PEDESTRIAN:
				return 4;
			default:
				break;
		}

		return -1; //Don't put element anywhere, since it's not recognized.
	}

	private void constructKdTrees(ArrayList<ArrayList<Element>> kdTreeLevels) {
		Element[][] elementArray = new Element[5][];
		int[] maxNumberOfElementsAtLeaf = new int[] {10000, 10000, 10000, 10000, 10000};
		for (int i = 0; i < N; i++ ) elementArray[i] = kdTreeLevels.get(i).toArray(new Element[0]);
		this.trees = new KdTree[N];
		for (int i = 0; i < N; i++) this.trees[i] = new KdTree(elementArray[i], maxNumberOfElementsAtLeaf[i]);
	}

	@Override
	public Vector<Element> rangeSearch(Rect searchQuery, double zoomLevel) {
		currentRangeSearch = Vector.empty();

		if (zoomLevel > 20) currentRangeSearch = currentRangeSearch.appendAll(trees[4].rangeSearch(searchQuery));
		if (zoomLevel > 6) currentRangeSearch = currentRangeSearch.appendAll(trees[3].rangeSearch(searchQuery));
		if (zoomLevel > 2) currentRangeSearch = currentRangeSearch.appendAll(trees[2].rangeSearch(searchQuery));
		if (zoomLevel > 1) currentRangeSearch = currentRangeSearch.appendAll(trees[1].rangeSearch(searchQuery));
		if (zoomLevel > 0) currentRangeSearch = currentRangeSearch.appendAll(trees[0].rangeSearch(searchQuery));

		return currentRangeSearch;
	}

	public Element nearestNeighborUsingRangeSearch(Coordinate queryPoint, TravelType travelType, double zoomLevel) {
		Element nearestNeighbor = new Element();
		Coordinate NNCoordinate = new Coordinate(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		nearestNeighbor.setAvgPoint(NNCoordinate);

		double r = 0.01;
		while (nearestNeighbor.getAvgPoint().getX() == Double.POSITIVE_INFINITY) {
			Rect queryRect = new Rect(queryPoint.getX() - r, queryPoint.getY() - r, queryPoint.getX() + r, queryPoint.getY() + r );
			rangeSearch(queryRect, zoomLevel);
			nearestNeighbor = nearestNeighborInCurrentRangeSearch(queryPoint, travelType);
			r = r + 0.01;
		}

		return nearestNeighbor;
	}

	/**
	 * Brute force nearest neighbor search
	 * Is much more accurate but also slower than other nearest neighbor search
	 * @param queryPoint			The point to search through nearest neighbor.
	 * @return 						Nearest Element to input point according to travelType.
	 */
	public Element nearestNeighborInCurrentRangeSearch(Coordinate queryPoint, TravelType travelType) {
		Element currentNN = new Element();
		Coordinate currentNNCoordinate = new Coordinate(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		currentNN.setAvgPoint(currentNNCoordinate);

		for (int i = 0; i < currentRangeSearch.size(); i++) {
			boolean searchCriteria = false;
			if (travelType == TravelType.ALL) searchCriteria = currentRangeSearch.get(i).isRoad();
			if (travelType == TravelType.CAR) searchCriteria = currentRangeSearch.get(i).isDrivable();
			if (travelType == TravelType.WALK) searchCriteria = currentRangeSearch.get(i).isWalkable();
			if (travelType == TravelType.BIKE) searchCriteria = currentRangeSearch.get(i).isCyclable();

			if (searchCriteria) {
				for (PathIterator pi = currentRangeSearch.get(i).getShape().getPathIterator(null); !pi.isDone(); pi.next()) {
					double[] currentShapePointCoordinateArray = new double[2];
					pi.currentSegment(currentShapePointCoordinateArray); // Inserts current coordinates into currentShapePointCoordinateArray;
					Coordinate currentShapePointCoordinate = new Coordinate(currentShapePointCoordinateArray[0], currentShapePointCoordinateArray[1]);
					double distanceCurrentNNToQueryPoint = Coordinate.euclidianDistance(currentNNCoordinate, queryPoint);
					double distanceCandidateToQueryPoint = Coordinate.euclidianDistance(currentShapePointCoordinate, queryPoint);
					if (distanceCandidateToQueryPoint < distanceCurrentNNToQueryPoint) {
						currentNNCoordinate = currentShapePointCoordinate;
						currentNN = currentRangeSearch.get(i);
					}
				}
			}
		}

		//Copy object contents to new element object
		Element nearestNeighbor = Element.cloneElement(currentNN);
		nearestNeighbor.setAvgPoint(currentNNCoordinate);
		return nearestNeighbor;
	}

	@Override
	public Element nearestNeighbor(Coordinate queryPoint, double zoomLevel) {
		int excludeTrees = 0;

		if (zoomLevel > 0) excludeTrees = 4;
		if (zoomLevel > 1) excludeTrees = 3;
		if (zoomLevel > 2) excludeTrees = 2;
		if (zoomLevel > 6) excludeTrees = 1;
		if (zoomLevel > 20) excludeTrees = 0;

		Element currentNN = new Element();
		currentNN.setAvgPoint(new Coordinate(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));

		//Get NN from each tree, then calculate NN from those elements
		for (int i = 0; i < trees.length-excludeTrees; i++) {
			Element candidate = trees[i].nearestNeighbor(queryPoint);
			double distanceCurrentNNToQueryPoint = Coordinate.euclidianDistance(currentNN.getAvgPoint(), queryPoint);
			double distanceCandidateToQueryPoint = Coordinate.euclidianDistance(candidate.getAvgPoint(), queryPoint);
			if (distanceCandidateToQueryPoint < distanceCurrentNNToQueryPoint) currentNN = candidate;
		}

		return currentNN;
	}
}
