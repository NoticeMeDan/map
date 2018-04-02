package com.noticemedan.map.model.KDTree;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

public class KDTree {

	@Getter private KDTreeNode rootNode;
	private int maxNumberOfElementsAtLeaf;
	private ArrayList<KDTreePoint> rangeSearchQueryResults;

	public KDTree(KDTreePoint[] points, int maxNumberOfElementsAtLeaf) {
		if (points.length == 0) throw new RuntimeException("Length of passed array to KD Tree is 0");
		if (maxNumberOfElementsAtLeaf < 1 ) throw new RuntimeException("The maximum number of elements at a leaf cannot be less than 1");

		this.maxNumberOfElementsAtLeaf = maxNumberOfElementsAtLeaf;
		this.rootNode = buildKDTree(points, 0);
	}

	/**
	 * @return 		Root node of KDTree.
	 */
	private KDTreeNode buildKDTree(KDTreePoint[] points, int depth) {
		KDTreePoint[] firstHalfArray;
		KDTreePoint[] secondHalfArray;
		KDTreeNode parent = new KDTreeNode();
		parent.setDepth(depth);

		// Define size of points array in leaf nodes.
		if (points.length <= maxNumberOfElementsAtLeaf ) {
			return new KDTreeNode(points, depth);
		} else if (depth % 2 == 0) { // If depth even, split by x-value
			Tuple2<KDTreePoint[], KDTreePoint[]> tuple2 = splitPointArrayByMedian(points);
			firstHalfArray = tuple2._1;
			secondHalfArray = tuple2._2;
			parent.setSplitValue(firstHalfArray[firstHalfArray.length-1].getX());
		} else { // If depth odd, split by y-value
			Tuple2<KDTreePoint[], KDTreePoint[]> tuple2 = splitPointArrayByMedian(points);
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

	private Tuple2<KDTreePoint[], KDTreePoint[]> splitPointArrayByMedian(KDTreePoint[] points) {
		int N = points.length;

		// Handle small array cases:
		if (N == 0) throw new RuntimeException("Zero element array passed as parameter.");
		if (N == 1) throw new RuntimeException("One element array cannot be split further.");
		if (N == 2) return Tuple.of(new KDTreePoint[] { points[0] }, new KDTreePoint[] { points[1] } );

		Quick.select(points, N/2);

		// k is the index where the array should be split.
		int k = N/2+1;
		KDTreePoint[] 	firstHalf = new KDTreePoint[k];
		KDTreePoint[]   secondHalf = new KDTreePoint[N-k];

		// Insert elements into two arrays from original array.
		int j = 0;
		for (int i = 0; i < N; i++) {
			if (points[i].getSortX()) {	points[i].setSortX(false); }
			else {						points[i].setSortX(true);  }
			if (i < k) 						firstHalf[i] = points[i];
			if (i >= k) 					secondHalf[j++] = points[i];
		}
		return Tuple.of(firstHalf, secondHalf);
	}

	public ArrayList<KDTreePoint> rangeSearch(double x1, double y1, double x2, double y2, int depth) {
		rangeSearchQueryResults = new ArrayList<>();
		searchTree(rootNode, x1, y1, x2, y2);
		return this.rangeSearchQueryResults;
	}

	public void searchTree(KDTreeNode parent, double x1, double y1, double x2, double y2) {

		// If current node is a leaf, check if point is within range R;
		if (parent.getPoints() != null) {
			for (int i = 0; i < parent.getPoints().length; i++) {
				//parent.getPoints()[i].getX();
			}
		}
		double inf = Double.POSITIVE_INFINITY;
		System.out.println(inf);

		if (parent.getDepth() % 2 == 0) {}

		//String[] test1 = new String[] {"Hello", "there", "how"};
		//Arrays.asList(test1);

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

	static public boolean pointInRect(KDTreePoint point, Rect rect) {
		boolean part1 = Math.abs(rect.getX1()) <= Math.abs(point.getX());
		boolean part2 = Math.abs(point.getX()) <= Math.abs(rect.getX2());
		boolean part3 = Math.abs(rect.getY1()) <= Math.abs(point.getY());
		boolean part4 = Math.abs(point.getY()) <= Math.abs(rect.getX2());
		return part1 && part2 && part3 && part4;
	}
}
