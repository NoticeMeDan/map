package com.noticemedan.map.model.KDTree;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

public class KDTree {

	@Getter private KDTreeNode rootNode;
	private int maxNumberOfElementsAtLeaf;
	private ArrayList<KDMapObject> rangeSearchQueryResults;

	public KDTree(KDMapObject[] points, int maxNumberOfElementsAtLeaf) {
		if (points.length == 0) throw new RuntimeException("Length of passed array to KD Tree is 0");
		if (maxNumberOfElementsAtLeaf < 1 ) throw new RuntimeException("The maximum number of elements at a leaf cannot be less than 1");

		this.maxNumberOfElementsAtLeaf = maxNumberOfElementsAtLeaf;
		this.rootNode = buildKDTree(points, 0);
	}

	/**
	 * @return 		Root node of KDTree.
	 */
	private KDTreeNode buildKDTree(KDMapObject[] points, int depth) {
		KDMapObject[] firstHalfArray;
		KDMapObject[] secondHalfArray;
		KDTreeNode parent = new KDTreeNode();
		parent.setDepth(depth);

		// Define size of points array in leaf nodes.
		if (points.length <= maxNumberOfElementsAtLeaf ) {
			return new KDTreeNode(points, depth);
		} else if (depth % 2 == 0) { // If depth even, split by x-value
			Tuple2<KDMapObject[], KDMapObject[]> tuple2 = splitPointArrayByMedian(points);
			firstHalfArray = tuple2._1;
			secondHalfArray = tuple2._2;
			parent.setSplitValue(firstHalfArray[firstHalfArray.length-1].getX());
		} else { // If depth odd, split by y-value
			Tuple2<KDMapObject[], KDMapObject[]> tuple2 = splitPointArrayByMedian(points);
			firstHalfArray = tuple2._1;
			secondHalfArray = tuple2._2;
			parent.setSplitValue(firstHalfArray[firstHalfArray.length-1].getY());
		}

		// Recursively find the parent's left and right child.
		KDTreeNode leftChild = buildKDTree(firstHalfArray, depth+1);
		KDTreeNode rightChild = buildKDTree(secondHalfArray, depth+1);

		parent.setLeftChild(leftChild);
		parent.setRightChild(rightChild);

		return parent;
	}

	private Tuple2<KDMapObject[], KDMapObject[]> splitPointArrayByMedian(KDMapObject[] points) {
		int N = points.length;

		// Handle small array cases:
		if (N == 0) throw new RuntimeException("Zero element array passed as parameter.");
		if (N == 1) throw new RuntimeException("One element array cannot be split further.");
		if (N == 2) return Tuple.of(new KDMapObject[] { points[0] }, new KDMapObject[] { points[1] } );

		Quick.select(points, N/2);

		// k is the index where the array should be split.
		int k = N/2+1;
		KDMapObject[] 	firstHalf = new KDMapObject[k];
		KDMapObject[]   secondHalf = new KDMapObject[N-k];

		// Insert elements into two arrays from original array.
		int j = 0;
		for (int i = 0; i < N; i++) {
			if (points[i].isDepthEven()) {	points[i].setDepthEven(false); }
			else {							points[i].setDepthEven(true);  }
			if (i < k) 						firstHalf[i] = points[i];
			if (i >= k) 					secondHalf[j++] = points[i];
		}
		return Tuple.of(firstHalf, secondHalf);
	}

	public ArrayList<KDMapObject> rangeSearch(Rect query) {
		rangeSearchQueryResults = new ArrayList<>();
		Rect startBoundingBox = new Rect(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		searchTree(rootNode, query, startBoundingBox);
		return this.rangeSearchQueryResults;
	}

	private void searchTree(KDTreeNode parent, Rect query, Rect boundingBox) {
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
		if (parent.getPoints() != null) {
			for (int i = 0; i < parent.getPoints().length; i++) {
				if (pointInRect(parent.getPoints()[i], query)) rangeSearchQueryResults.add(parent.getPoints()[i]);
			}
		} else {
			// If left bounding box for left child is completely in query, report all points in this subtree
			if (rectCompletelyInRect(boundingBoxLeft, query)) {
				reportSubtree(leftChild);
			} else {
				if (parent.getDepth() % 2 == 0) { // Check depth of current search
					/* Because depth is equal, use x-values for checking if bounding box range intersects query range.
					 * If they do not intersect, it means that the query is not within the left subtree.
					 */
					if (rangeIntersectsRange(boundingBoxLeft.getX1(), boundingBoxLeft.getX2(), query.getX1(), query.getX2())) {
						// Because they do intersect, search that subtree further.
						searchTree(leftChild, query, boundingBoxLeft);
					}
				} else {
					// If depth is uneven, use y-values for checking if bounding box range intersects query range.
					if (rangeIntersectsRange(boundingBoxLeft.getY1(), boundingBoxLeft.getY2(), query.getY1(), query.getY2())) {
						// Because they do intersect, search that subtree further.
						searchTree(leftChild, query, boundingBoxLeft);
					}
				}
			}

			if (rectCompletelyInRect(boundingBoxRight, query)) {
				reportSubtree(rightChild);
			} else {
				if (parent.getDepth() % 2 == 0) {
					if (rangeIntersectsRange(boundingBoxRight.getX1(), boundingBoxRight.getX2(), query.getX1(), query.getX2())) {
						searchTree(rightChild, query, boundingBoxRight);
					}
				} else {
					if (rangeIntersectsRange(boundingBoxRight.getY1(), boundingBoxRight.getY2(), query.getY1(), query.getY2())) {
						searchTree(rightChild, query, boundingBoxRight);
					}
				}
			}
		}
	}

	// Using in order traversal (LVR: Left, Visit, Right)
	public void reportSubtree(KDTreeNode parent) {
		if (parent.getLeftChild() != null) 		reportSubtree(parent.getLeftChild()); //L
		if (parent.getPoints() != null) 		rangeSearchQueryResults.addAll(Arrays.asList(parent.getPoints())); //V
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
	static public boolean pointInRect(KDMapObject point, Rect rect) {
		boolean part1 = Math.abs(rect.getX1()) <= Math.abs(point.getX());
		boolean part2 = Math.abs(point.getX()) <= Math.abs(rect.getX2());
		boolean part3 = Math.abs(rect.getY1()) <= Math.abs(point.getY());
		boolean part4 = Math.abs(point.getY()) <= Math.abs(rect.getY2());
		return part1 && part2 && part3 && part4;
	}
}
