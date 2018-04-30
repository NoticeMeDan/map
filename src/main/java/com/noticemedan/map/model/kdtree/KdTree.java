package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.utilities.Coordinate;
import com.noticemedan.map.model.utilities.Quick;
import com.noticemedan.map.model.utilities.Rect;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KdTree {
	@Getter
	private KdNode rootNode;
	private int maxNumberOfElementsAtLeaf;
	private ArrayList<OsmElement> rangeSearchQueryResults;
	private OsmElement nearestNeighbor;

	public KdTree(OsmElement[] elements, int maxNumberOfElementsAtLeaf) {
		if (elements.length == 0) return;
		if (maxNumberOfElementsAtLeaf < 1)
			throw new RuntimeException("The maximum number of elements at a leaf cannot be less than 1");
		this.maxNumberOfElementsAtLeaf = maxNumberOfElementsAtLeaf;
		this.rootNode = constructKdTree(elements, 0);
	}

	/**
	 * @return Root node of kdtree.
	 */
	private KdNode constructKdTree(OsmElement[] elements, int depth) {
		// Define how many osmElements there are in a leaf node.
		if (elements.length <= maxNumberOfElementsAtLeaf) return new KdNode(elements, depth);

		Tuple2<OsmElement[], OsmElement[]> splitElements = splitPointArrayByMedian(elements);
		OsmElement[] firstHalfArray = splitElements._1;
		OsmElement[] secondHalfArray = splitElements._2;
		KdNode parent = new KdNode();
		parent.setDepth(depth);
		parent.setSplitElement(firstHalfArray[firstHalfArray.length - 1]);

		// If depth even, split by x-value, otherwise by y-value
		if (depth % 2 == 0) parent.setSplitValue(firstHalfArray[firstHalfArray.length - 1].getAvgPoint().getX());
		else parent.setSplitValue(firstHalfArray[firstHalfArray.length - 1].getAvgPoint().getY());

		// Recursively find the parent's left and right child.
		KdNode leftChild = constructKdTree(firstHalfArray, depth + 1);
		KdNode rightChild = constructKdTree(secondHalfArray, depth + 1);

		parent.setLeftChild(leftChild);
		parent.setRightChild(rightChild);

		return parent;
	}

	private Tuple2<OsmElement[], OsmElement[]> splitPointArrayByMedian(OsmElement[] elements) {
		int N = elements.length;
		// k is the index where the array should be split.
		int k = N / 2 + 1;

		// Handle small array cases:
		if (N == 0) throw new RuntimeException("Zero element array passed as parameter.");
		if (N == 1) throw new RuntimeException("One element array cannot be split further.");
		if (N == 2)
			return Tuple.of(new OsmElement[]{elements[0]}, new OsmElement[]{elements[1]}); //Two 1 element arrays

		//Arrange elements array such that median value is in middle of array.
		Quick.select(elements, N / 2);

		OsmElement[] firstHalf = new OsmElement[k];
		OsmElement[] secondHalf = new OsmElement[N - k];

		// Insert elements into two arrays from original array.
		int j = 0;
		for (int i = 0; i < N; i++) {
			if (elements[i].isDepthEven()) elements[i].setDepthEven(false);
			else elements[i].setDepthEven(true);
			if (i < k) firstHalf[i] = elements[i];
			if (i >= k) secondHalf[j++] = elements[i];
		}
		return Tuple.of(firstHalf, secondHalf);
	}

	public List<OsmElement> rangeSearch(Rect query) {
		rangeSearchQueryResults = new ArrayList<>();
		if (rootNode == null) return this.rangeSearchQueryResults;

		Rect startBoundingBox = new Rect(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		searchTree(rootNode, query, startBoundingBox);
		return this.rangeSearchQueryResults;
	}

	private void searchTree(KdNode parent, Rect searchQuery, Rect region) {
		Tuple2<Rect, Rect> regionSplitByMedian = createRegions(parent, region);
		Rect regionA = regionSplitByMedian._1; // The left/bottom search region (A).
		Rect regionB = regionSplitByMedian._2; // The right/top search region (B).
		boolean isLeaf = parent.getElements() != null; // Is parent a leaf?

		KdNode leftChild = parent.getLeftChild();
		KdNode rightChild = parent.getRightChild();

		if (isLeaf) addElementsToQueryResults(parent, searchQuery);
		else {
			investigateRegion(parent, leftChild, regionA, searchQuery);
			investigateRegion(parent, rightChild, regionB, searchQuery);
		}
	}

	private void investigateRegion(KdNode parent, KdNode child, Rect region, Rect searchQuery) {
		// Is region completely in search query?
		boolean regionCompletelyInSearchQuery = Rect.rectCompletelyInRect(region, searchQuery);
		boolean depthEven = parent.getDepth() % 2 == 0;
		// Do x-ranges for region and query intersect?
		boolean xRangesIntersect = Rect.rangeIntersectsRange(region.getX1(), region.getX2(), searchQuery.getX1(), searchQuery.getX2());
		// Do y-ranges for region and query intersect?
		boolean yRangesIntersect = Rect.rangeIntersectsRange(region.getY1(), region.getY2(), searchQuery.getY1(), searchQuery.getY2());

		if (regionCompletelyInSearchQuery) reportSubtree(child);
		else if (depthEven && xRangesIntersect) searchTree(child, searchQuery, region);
		else if (yRangesIntersect) searchTree(child, searchQuery, region);
	}

	private void addElementsToQueryResults(KdNode parent, Rect searchQuery) {
		for (int i = 0; i < parent.getElements().length; i++) {
			if (Rect.pointInRect(parent.getElements()[i], searchQuery)) //Check if elements lie in search query
				rangeSearchQueryResults.add(parent.getElements()[i]);
		}
	}

	// Using in order traversal (LVR: Left, Visit, Right)
	//Adds all elements to query results
	private void reportSubtree(KdNode parent) {
		if (parent.getLeftChild() != null) reportSubtree(parent.getLeftChild()); //L
		if (parent.getElements() != null) rangeSearchQueryResults.addAll(Arrays.asList(parent.getElements())); //V:
		if (parent.getRightChild() != null) reportSubtree(parent.getRightChild());//R
	}

	private Tuple2<Rect, Rect> createRegions(KdNode parent, Rect region) {
		boolean depthEven = parent.getDepth() % 2 == 0;
		if (depthEven)
			return Tuple.of(
					new Rect( // Define left region (A)
							region.getX1(),
							region.getY1(),
							parent.getSplitValue(),
							region.getY2()),
					new Rect( // Define right region (B)
							parent.getSplitValue(),
							region.getY1(),
							region.getX2(),
							region.getY2()));
		else return Tuple.of(
				new Rect( //Define bottom region (A)
						region.getX1(),
						region.getY1(),
						region.getX2(),
						parent.getSplitValue()),
				new Rect( //Define top region (B)
						region.getX1(),
						parent.getSplitValue(),
						region.getX2(),
						region.getY2()));
	}

	public OsmElement nearestNeighbor(Coordinate coordinate) {
		nearestNeighbor = new OsmElement();
		nearestNeighbor.setAvgPoint(new Coordinate(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
		if (rootNode == null) nearestNeighbor = null;
		nearestNeighborSearch(coordinate, rootNode);
		return nearestNeighbor;
	}

	private void nearestNeighborSearch(Coordinate queryPoint, KdNode root) {
		boolean isLeaf = root.getElements() != null;
		boolean depthEven = root.getDepth() % 2 == 0;

		KdNode nextBranch = null;
		KdNode oppositeBranch = null;

		if (isLeaf) searchForNNInArray(queryPoint, root);
		else if (depthEven && queryPoint.getX() <= root.getSplitValue() || !depthEven && queryPoint.getY() <= root.getSplitValue()) {
			nextBranch = root.getLeftChild();
			oppositeBranch = root.getRightChild();
		} else if (depthEven && queryPoint.getX() > root.getSplitValue() || !depthEven && queryPoint.getY() > root.getSplitValue()) {
			nextBranch = root.getRightChild();
			oppositeBranch = root.getLeftChild();
		}

		if(!isLeaf) {
			nearestNeighborSearch(queryPoint, nextBranch);
			lookInOppositeBranch(queryPoint, root, oppositeBranch);
		}
	}

	private void lookInOppositeBranch(Coordinate queryPoint, KdNode root, KdNode oppositeBranch) {
		boolean depthEven = root.getSplitValue() % 2 == 0;

		//Check various distances
		double currentNNRadius = Coordinate.euclidianDistance(queryPoint, nearestNeighbor.getAvgPoint());
		double distanceToXSplitValue = Math.abs(queryPoint.getX() - root.getSplitValue());
		double distanceToYSplitValue = Math.abs(queryPoint.getY() - root.getSplitValue());

		boolean currentNNRadiusOverlapsXSplitValue = depthEven && currentNNRadius > distanceToXSplitValue;
		boolean currentNNRadiusOverlapsYSplitValue = !depthEven && currentNNRadius > distanceToYSplitValue;

		//Look in opposite branches
		if (currentNNRadiusOverlapsXSplitValue) nearestNeighborSearch(queryPoint, oppositeBranch);
		if (currentNNRadiusOverlapsYSplitValue) nearestNeighborSearch(queryPoint, oppositeBranch);
	}

	private void searchForNNInArray(Coordinate queryPoint, KdNode leafNode) {
		for (int i = 0; i < leafNode.getElements().length; i++) {
			boolean newPointIsCloserThanCurrentNN =
					  Coordinate.euclidianDistance(queryPoint, leafNode.getElements()[i].getAvgPoint())
					< Coordinate.euclidianDistance(queryPoint, nearestNeighbor.getAvgPoint());
			if (newPointIsCloserThanCurrentNN) nearestNeighbor = leafNode.getElements()[i];
		}
	}
}
