package com.noticemedan.map.model.KDTree;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.Getter;

public class KDTree {

	@Getter private KDTreeNode rootNode;
	private KDTreePoint[] points;
	private int N; // Number of nodes to build

	public KDTree(int N) {
		this.N = N;
		buildNodes();
		this.rootNode = buildKDTree(points, 0);
	}

	public static void main(String[] args) {
		KDTree tree = new KDTree(1000);
	}

	// TODO: Remove method when implemented using shapes.
	private void buildNodes() {
		points = new KDTreePoint[N];

		System.out.println("Begin making points");
		for(int i = 0; i < N; i++) {
			KDTreePoint point = new KDTreePoint(Math.random(), Math.random());
			points[i] = point;
		}
		System.out.println("Points ended, begin building kd tree");
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
		if (points.length < 10 ) {
			return new KDTreeNode(points, depth);
		} else if (depth % 2 == 0) { // If depth even, split by x-value
			firstHalfArray = splitPointArrayByMedian(points)._1;
			secondHalfArray = splitPointArrayByMedian(points)._2;
			parent.setSplitValue(firstHalfArray[firstHalfArray.length-1].getX());
		} else { // If depth odd, split by y-value
			firstHalfArray = splitPointArrayByMedian(points)._1;
			secondHalfArray = splitPointArrayByMedian(points)._2;
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
			if (points[i].getSortX()) 		points [i].setSortX(false);
			if (!points[i].getSortX()) 		points [i].setSortX(true);
			if (i < k) 						firstHalf[i] = points[i];
			if (i >= k) 					secondHalf[j++] = points[i];
		}
		return Tuple.of(firstHalf, secondHalf);
	}
}
