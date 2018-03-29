package com.noticemedan.map.model.KDTree;

import io.vavr.Tuple;
import io.vavr.Tuple2;

public class KDTree {

	KDTreeNode rootNode;
	KDTreePoint[] points;
	int N; //Number of nodes to build

	public KDTree(int N) {
		this.N = N;
		buildNodes();

		Stopwatch stopwatch = new Stopwatch();
		this.rootNode = buildKDTree(points, 0);
		System.out.println(stopwatch.elapsedTime());
	}

	public static void main(String[] args) {
		KDTree tree = new KDTree(23000000);
	}

	private void buildNodes() {
		points = new KDTreePoint[N];

		for(int i = 0; i < N; i++) {
			KDTreePoint point = new KDTreePoint(Math.random(), Math.random());
			points[i] = point;
		}
	}

	//With a single list!
	public KDTreeNode buildKDTree(KDTreePoint[] points, int depth) {
		KDTreePoint[] firstHalfArray;
		KDTreePoint[] secondHalfArray;
		KDTreeNode parent = new KDTreeNode();
		parent.setDepth(depth);

		//Define size of points array
		if (points.length < 1000 ) {
			return new KDTreeNode(points);
		} else if (Utility.isEven(depth)) {
			Tuple2<KDTreePoint[], KDTreePoint[]> splitPointArrays = splitPointArrayByMedian(points);
			firstHalfArray = splitPointArrays._1;
			secondHalfArray = splitPointArrays._2;
			parent.setSplitValue(firstHalfArray[firstHalfArray.length-1].getX());
		} else {
			Tuple2<KDTreePoint[], KDTreePoint[]> splitPointArrays = splitPointArrayByMedian(points);
			firstHalfArray = splitPointArrays._1;
			secondHalfArray = splitPointArrays._2;
			parent.setSplitValue(firstHalfArray[firstHalfArray.length-1].getY());
		}

		//Recursively find left and right child of parent nodes.
		KDTreeNode leftChild = buildKDTree(firstHalfArray, depth+1);
		KDTreeNode rightChild = buildKDTree(secondHalfArray, depth+1);

		parent.setLeftChild(leftChild);
		parent.setRightChild(rightChild);

		return parent;
	}

	private Tuple2<KDTreePoint[], KDTreePoint[]> splitPointArrayByMedian(KDTreePoint points[]) {
		int N = points.length;
		int K = N/2+1; // Calculate index K to stop copying elements into first half of the array.
		int M = 0;

		// Handle small array cases:
		if (N == 0) throw new RuntimeException("Zero element array passed as parameter.");
		if (N == 1) throw new RuntimeException("One element array cannot be split further.");
		if (N == 2) return Tuple.of(new KDTreePoint[] { points[0] }, new KDTreePoint[] { points[1] } );

		// Define index M from where to start copying elements into second half of array.
		if (Utility.isEven(N))      M = N-N/2+1;
		if (!Utility.isEven(N))     M = N-N/2;

		Quick.select(points, K);

		KDTreePoint[] firstHalf = new KDTreePoint[K];
		KDTreePoint[] secondHalf = new KDTreePoint[N-M];

		int j = 0;
		for (int i = 0; i < N; i++) {
			if (points[i].getSortX()) points [i].setSortX(false);
			if (!points[i].getSortX()) points [i].setSortX(true);
			if (i < K) firstHalf[i] = points[i];
			if (i >= K) secondHalf[j++] = points[i];
		}
		return Tuple.of(firstHalf, secondHalf);
	}
}
