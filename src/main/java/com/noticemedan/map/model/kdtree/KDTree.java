package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.utilities.Quick;
import com.noticemedan.map.model.utilities.Rect;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KDTree {

	@Getter private KDTreeNode rootNode;
	private int maxNumberOfElementsAtLeaf;
	private ArrayList<OsmElement> rangeSearchQueryResults;

	public KDTree(OsmElement[] points, int maxNumberOfElementsAtLeaf) {
		if (points.length == 0) return;
		if (maxNumberOfElementsAtLeaf < 1 ) throw new RuntimeException("The maximum number of elements at a leaf cannot be less than 1");
		this.maxNumberOfElementsAtLeaf = maxNumberOfElementsAtLeaf;
		this.rootNode = buildKDTree(points, 0);
	}

	/**
	 * @return 		Root node of kdtree.
	 */
	private KDTreeNode buildKDTree(OsmElement[] points, int depth) {
		// Define how many osmElements there are in a leaf node.
		if (points.length <= maxNumberOfElementsAtLeaf ) return new KDTreeNode(points, depth);

		Tuple2<OsmElement[], OsmElement[]> pointsSplitted = splitPointArrayByMedian(points);
		OsmElement[] firstHalfArray = pointsSplitted._1;
		OsmElement[] secondHalfArray = pointsSplitted._2;
		KDTreeNode parent = new KDTreeNode();
		parent.setDepth(depth);

		if (depth % 2 == 0) { // If depth even, split by x-value
			parent.setSplitValue(firstHalfArray[firstHalfArray.length-1].getAvgPoint().getX());
		} else { // If depth odd, split by y-value
			parent.setSplitValue(firstHalfArray[firstHalfArray.length-1].getAvgPoint().getY());
		}

		// Recursively find the parent's left and right child.
		KDTreeNode leftChild = buildKDTree(firstHalfArray, depth+1);
		KDTreeNode rightChild = buildKDTree(secondHalfArray, depth+1);

		parent.setLeftChild(leftChild);
		parent.setRightChild(rightChild);

		return parent;
	}

	private Tuple2<OsmElement[], OsmElement[]> splitPointArrayByMedian(OsmElement[] points) {
		int N = points.length;
		// k is the index where the array should be split.
		int k = N/2+1;

		// Handle small array cases:
		if (N == 0) throw new RuntimeException("Zero element array passed as parameter.");
		if (N == 1) throw new RuntimeException("One element array cannot be split further.");
		if (N == 2) return Tuple.of(new OsmElement[]{points[0]}, new OsmElement[]{points[1]});

		Quick.select(points, N/2);


		OsmElement[] firstHalf = new OsmElement[k];
		OsmElement[] secondHalf = new OsmElement[N - k];

		// Insert elements into two arrays from original array.
		int j = 0;
		for (int i = 0; i < N; i++) {
			if (points[i].isDepthEven()) points[i].setDepthEven(false);
			else points[i].setDepthEven(true);

			if (i < k) firstHalf[i] = points[i];
			if (i >= k) secondHalf[j++] = points[i];
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

	private void searchTree(KDTreeNode parent, Rect searchQuery, Rect boundingBox) {
		//Create bounding boxes for search:
		Rect boundingBoxLeft;
		Rect boundingBoxRight;
		KDTreeNode leftChild = parent.getLeftChild();
		KDTreeNode rightChild = parent.getRightChild();

		if (parent.getDepth() % 2 == 0) { //If search depth is even
			boundingBoxLeft = new Rect(boundingBox.getX1(), boundingBox.getY1(), parent.getSplitValue(), boundingBox.getY2()); //lower
			boundingBoxRight = new Rect(parent.getSplitValue(), boundingBox.getY1(), boundingBox.getX2(), boundingBox.getY2()); //higher
		} else {
			boundingBoxLeft = new Rect(boundingBox.getX1(), boundingBox.getY1(), boundingBox.getX2(), parent.getSplitValue()); //lower
			boundingBoxRight = new Rect(boundingBox.getX1(), parent.getSplitValue(), boundingBox.getX2(), boundingBox.getY2()); //higher
		}

		// If current node is a leaf, check if point is within query;
		if (parent.getOsmElements() != null) {
			for (int i = 0; i < parent.getOsmElements().length; i++) {
				if (pointInRect(parent.getOsmElements()[i], searchQuery)) rangeSearchQueryResults.add(parent.getOsmElements()[i]);
			}
		} else {
			// If left bounding box for left child is completely in query, report all osmMaterialElements in this subtree
			if (rectCompletelyInRect(boundingBoxLeft, searchQuery)) {
				reportSubtree(leftChild);
			} else {
				if (parent.getDepth() % 2 == 0) { // Check depth of current search
					/* Because depth is equal, use x-values for checking if bounding box range intersects query range.
					 * If they do not intersect, it means that the query is not within the left subtree.
					 */
					if (rangeIntersectsRange(boundingBoxLeft.getX1(), boundingBoxLeft.getX2(), searchQuery.getX1(), searchQuery.getX2())) {
						// Because they do intersect, search that subtree further.
						searchTree(leftChild, searchQuery, boundingBoxLeft);
					}
				} else {
					// If depth is uneven, use y-values for checking if bounding box range intersects query range.
					if (rangeIntersectsRange(boundingBoxLeft.getY1(), boundingBoxLeft.getY2(), searchQuery.getY1(), searchQuery.getY2())) {
						// Because they do intersect, search that subtree further.
						searchTree(leftChild, searchQuery, boundingBoxLeft);
					}
				}
			}

			if (rectCompletelyInRect(boundingBoxRight, searchQuery)) {
				reportSubtree(rightChild);
			} else {
				if (parent.getDepth() % 2 == 0) {
					if (rangeIntersectsRange(boundingBoxRight.getX1(), boundingBoxRight.getX2(), searchQuery.getX1(), searchQuery.getX2())) {
						searchTree(rightChild, searchQuery, boundingBoxRight);
					}
				} else {
					if (rangeIntersectsRange(boundingBoxRight.getY1(), boundingBoxRight.getY2(), searchQuery.getY1(), searchQuery.getY2())) {
						searchTree(rightChild, searchQuery, boundingBoxRight);
					}
				}
			}
		}
	}

	// Using in order traversal (LVR: Left, Visit, Right)
	public void reportSubtree(KDTreeNode parent) {
		if (parent.getLeftChild() != null) 		reportSubtree(parent.getLeftChild()); //L
		if (parent.getOsmElements() != null) 		rangeSearchQueryResults.addAll(Arrays.asList(parent.getOsmElements())); //V
		if (parent.getRightChild() != null) 	reportSubtree(parent.getRightChild());//R
	}

	static public boolean rectCompletelyInRect(Rect smallRect, Rect largeRect) {
		boolean smallRectXRangeInLargeRectXRange = largeRect.getX1() <= smallRect.getX1() && smallRect.getX1() <= smallRect.getX2() && smallRect.getX2() <= largeRect.getX2();
		boolean smallRectYRangeInLargeRectYRange = largeRect.getY1() <= smallRect.getY1() && smallRect.getY1() <= smallRect.getY2() && smallRect.getY2() <= largeRect.getY2();
		return smallRectXRangeInLargeRectXRange && smallRectYRangeInLargeRectYRange;
	}

	static public boolean rangeIntersectsRange(double a, double b, double c, double d) {
		// Do range a-b and c-d intersect?
		return a <= d && b >= c;
	}

	//TODO: Not so pretty code with 'part1', 'part2'...
	static public boolean pointInRect(OsmElement osmElement, Rect rect) {
		boolean part1 = rect.getX1() <= osmElement.getAvgPoint().getX();
		boolean part2 = osmElement.getAvgPoint().getX() <= rect.getX2();
		boolean part3 = rect.getY1() <= osmElement.getAvgPoint().getY();
		boolean part4 = osmElement.getAvgPoint().getY() <= rect.getY2();
		return part1 && part2 && part3 && part4;
	}
}
