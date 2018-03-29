package com.noticemedan.map.model.KDTree;

public class Utility {
	public static boolean isEven(int x) {
		return x % 2 == 0;
	}

	/**
	 * TODO: Would like it to abstract it to Object class but I get Object cannot be casted to KDTreeNode when I do so.
	 *
	 * @param       objects
	 * @return      New array with first half of objects from input array median inclusive.
	 *              (Assuming objects has been sorted by some property).
	 */
	public static KDTreePoint[] firstHalfArray(KDTreePoint[] objects) {
		int N = objects.length;
		int K = N/2+1; // Calculate index K to stop copying elements into first half of the array.

		// Handle small array cases:
		if (N == 0) throw new RuntimeException("Zero element array passed as parameter.");
		if (N == 1) throw new RuntimeException("One element array cannot be split further.");
		if (N == 2) return new KDTreePoint[] {objects[0]};

		KDTreePoint[] firstHalf = new KDTreePoint[K];
		return firstHalf;
	}

	/**
	 * TODO: Make it treat generic objects instead of only KDTreeNode objects
	 *
	 * @param       objects
	 * @return      New array with second half of objects from input array median exclusive.
	 *              (Assuming objects has been sorted by some property).
	 */
	public static KDTreePoint[] secondHalfArray(KDTreePoint[] objects) {
		int N = objects.length;
		int M = 0;

		// Handle small array cases:
		if (N == 0) throw new RuntimeException("Zero element array passed as parameter.");
		if (N == 1) throw new RuntimeException("One element array cannot be split further.");
		if (N == 2) return new KDTreePoint[] {objects[1]};

		// Define index M from where to start copying elements into second half of array.
		if (Utility.isEven(N))      M = N-N/2+1;
		if (!Utility.isEven(N))     M = N-N/2;

		KDTreePoint[] secondHalf = new KDTreePoint[N-M];
		return secondHalf;
	}
}
