package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.model.OSMMaterialElement;
import com.noticemedan.map.model.utilities.Quick;
import com.noticemedan.map.model.utilities.Rect;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KDTree {

	@Getter private KDTreeNode rootNode;
	private int maxNumberOfElementsAtLeaf;
	private ArrayList<OSMMaterialElement> rangeSearchQueryResults;

	public KDTree(OSMMaterialElement[] points, int maxNumberOfElementsAtLeaf) {
		if (points.length == 0) throw new RuntimeException("Length of passed array to KD Tree is 0");
		if (maxNumberOfElementsAtLeaf < 1 ) throw new RuntimeException("The maximum number of elements at a leaf cannot be less than 1");

		this.maxNumberOfElementsAtLeaf = maxNumberOfElementsAtLeaf;
		this.rootNode = buildKDTree(points, 0);
	}

	//TODO: Not so pretty code with 'part1', 'part2'...
	static public boolean pointInRect(OSMMaterialElement point, Rect rect) {
		boolean part1 = Math.abs(rect.getX1()) <= Math.abs(point.getAvgPoint().getX());
		boolean part2 = Math.abs(point.getAvgPoint().getX()) <= Math.abs(rect.getX2());
		boolean part3 = Math.abs(rect.getY1()) <= Math.abs(point.getAvgPoint().getY());
		boolean part4 = Math.abs(point.getAvgPoint().getY()) <= Math.abs(rect.getY2());
		return part1 && part2 && part3 && part4;
	}

	/**
	 * @return 		Root node of kdtree.
	 */
	private KDTreeNode buildKDTree(OSMMaterialElement[] points, int depth) {
		OSMMaterialElement[] firstHalfArray;
		OSMMaterialElement[] secondHalfArray;
		KDTreeNode parent = new KDTreeNode();
		parent.setDepth(depth);

		// Define size of osmMaterialElements array in leaf nodes.
		if (points.length <= maxNumberOfElementsAtLeaf ) {
			return new KDTreeNode(points, depth);
		} else if (depth % 2 == 0) { // If depth even, split by x-value
			Tuple2<OSMMaterialElement[], OSMMaterialElement[]> tuple2 = splitPointArrayByMedian(points);
			firstHalfArray = tuple2._1;
			secondHalfArray = tuple2._2;
			parent.setSplitValue(firstHalfArray[firstHalfArray.length-1].getAvgPoint().getX());
		} else { // If depth odd, split by y-value
			Tuple2<OSMMaterialElement[], OSMMaterialElement[]> tuple2 = splitPointArrayByMedian(points);
			firstHalfArray = tuple2._1;
			secondHalfArray = tuple2._2;
			parent.setSplitValue(firstHalfArray[firstHalfArray.length-1].getAvgPoint().getY());
		}

		// Recursively find the parent's left and right child.
		KDTreeNode leftChild = buildKDTree(firstHalfArray, depth+1);
		KDTreeNode rightChild = buildKDTree(secondHalfArray, depth+1);

		parent.setLeftChild(leftChild);
		parent.setRightChild(rightChild);

		return parent;
	}

	private Tuple2<OSMMaterialElement[], OSMMaterialElement[]> splitPointArrayByMedian(OSMMaterialElement[] points) {
		int N = points.length;

		// Handle small array cases:
		if (N == 0) throw new RuntimeException("Zero element array passed as parameter.");
		if (N == 1) throw new RuntimeException("One element array cannot be split further.");
		if (N == 2) return Tuple.of(new OSMMaterialElement[]{points[0]}, new OSMMaterialElement[]{points[1]});

		Quick.select(points, N/2);

		// k is the index where the array should be split.
		int k = N/2+1;
		OSMMaterialElement[] firstHalf = new OSMMaterialElement[k];
		OSMMaterialElement[] secondHalf = new OSMMaterialElement[N - k];

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
		if (parent.getOsmMaterialElements() != null) {
			for (int i = 0; i < parent.getOsmMaterialElements().length; i++) {
				if (pointInRect(parent.getOsmMaterialElements()[i], searchQuery)) rangeSearchQueryResults.add(parent.getOsmMaterialElements()[i]);
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
		if (parent.getOsmMaterialElements() != null) 		rangeSearchQueryResults.addAll(Arrays.asList(parent.getOsmMaterialElements())); //V
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

	public List<OSMMaterialElement> rangeSearch(Rect query) {
		rangeSearchQueryResults = new ArrayList<>();
		Rect startBoundingBox = new Rect(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		searchTree(rootNode, query, startBoundingBox);
		return this.rangeSearchQueryResults;
	}
}
