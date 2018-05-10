package com.noticemedan.mappr.model.service;

import com.noticemedan.mappr.model.kdtree.ForestInterface;
import com.noticemedan.mappr.model.kdtree.KdTree;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.map.Type;
import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.model.util.Rect;
import io.vavr.collection.Vector;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.awt.geom.PathIterator;

@Slf4j
public class ForestService implements ForestInterface {
	private KdTree trees[];
	@Getter
	private Vector<Element> coastlines;
	private Vector<Element> currentRangeSearch;

	public ForestService(Vector<Element> elements, Vector<Element> coastlineElements) {
		//TODO create different amounts of leafs for zoom levels
		int[] maxNumberOfElementsAtLeaf = new int[] {100, 100, 100, 100, 100};
		this.coastlines = coastlineElements;
		Element[][] elementArray = new Element[5][];

		Vector<Element> zoom0 = Vector.empty();
		Vector<Element> zoom1 = Vector.empty();
		Vector<Element> zoom2 = Vector.empty();
		Vector<Element> zoom3 = Vector.empty();
		Vector<Element> zoom4 = Vector.empty();

		for (Element osmElement : elements) {
			switch (osmElement.getType()) {
				case MOTORWAY:
					zoom0 = zoom0.append(osmElement);
					break;
				case PRIMARY:
					zoom1 = zoom1.append(osmElement);
					break;
				case SECONDARY:
				case TERTIARY:
					zoom2 = zoom2.append(osmElement);
					break;
				case WATER:
				case GRASSLAND:
				case HEATH:
				case PARK:
				case ROAD:
				case FOREST:
					zoom3 = zoom3.append(osmElement);
					break;
				case BUILDING:
				case PLAYGROUND:
					zoom4 = zoom4.append(osmElement);
					break;
				default:
					break;
			}
		}

		elementArray[0] = zoom0.toJavaList().toArray(new Element[0]);
		elementArray[1] = zoom1.toJavaList().toArray(new Element[0]);
		elementArray[2] = zoom2.toJavaList().toArray(new Element[0]);
		elementArray[3] = zoom3.toJavaList().toArray(new Element[0]);
		elementArray[4] = zoom4.toJavaList().toArray(new Element[0]);

		this.trees = new KdTree[elementArray.length];
		for (int i = 0; i < trees.length; i++) {
			this.trees[i] = new KdTree(elementArray[i], maxNumberOfElementsAtLeaf[i]);
		}
	}

	@Override
	public Vector<Element> rangeSearch(Rect searchQuery, double zoomLevel) {
		Vector<Element> searchResults = Vector.empty();

		if (zoomLevel > 50) searchResults = searchResults.appendAll(trees[4].rangeSearch(searchQuery));
		if (zoomLevel > 15) searchResults = searchResults.appendAll(trees[3].rangeSearch(searchQuery));
		if (zoomLevel > 1) searchResults = searchResults.appendAll(trees[2].rangeSearch(searchQuery));
		if (zoomLevel > 0.5) searchResults = searchResults.appendAll(trees[1].rangeSearch(searchQuery));
		if (zoomLevel > 0) searchResults = searchResults.appendAll(trees[0].rangeSearch(searchQuery));

		currentRangeSearch = searchResults;
		return searchResults;
	}

	//Range search as if only having one zoom level.
	public Vector<Element> rangeSearch(Rect searchQuery) {
		return rangeSearch(searchQuery, 0);
	}

	/**
	 * Brute force nearest neighbor (more accurate, but slower than other NN method).
	 * @param queryPoint
	 * @return
	 */
	public Element nearestNeighbor(Coordinate queryPoint) {
		Element currentNN = new Element();
		Coordinate currentNNCoordinate = new Coordinate(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

		for (int i = 0; i < currentRangeSearch.size(); i++) {
			if (isWay(currentRangeSearch.get(i))) {
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
		return currentNN;
	}

	private boolean isWay(Element e) {
		return e.getType() == Type.MOTORWAY ||
				e.getType() == Type.PRIMARY ||
				e.getType() == Type.SECONDARY ||
				e.getType() == Type.TERTIARY ||
				e.getType() == Type.ROAD;
	}

	@Override
	public Element nearestNeighbor(Coordinate queryPoint, double zoomLevel) {
		int excludeTrees = 0;

		if (zoomLevel > 0) excludeTrees = 4;
		if (zoomLevel > 0.5) excludeTrees = 3;
		if (zoomLevel > 1) excludeTrees = 2;
		if (zoomLevel > 15) excludeTrees = 1;
		if (zoomLevel > 50) excludeTrees = 0;

		Element currentNN = new Element();
		currentNN.setAvgPoint(new Coordinate(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));

		//Get NN from each tree, than calculate NN from those elements
		for (int i = 0; i < trees.length-excludeTrees; i++) {
			Element candidate = trees[i].nearestNeighbor(queryPoint);
			double distanceCurrentNNToQueryPoint = Coordinate.euclidianDistance(currentNN.getAvgPoint(), queryPoint);
			double distanceCandidateToQueryPoint = Coordinate.euclidianDistance(candidate.getAvgPoint(), queryPoint);
			if (distanceCandidateToQueryPoint < distanceCurrentNNToQueryPoint) currentNN = candidate;
		}

		return currentNN;
	}
}
