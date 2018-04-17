package com.noticemedan.map.model.utilities;

import java.util.Random;

public class Quick {

	private static Random random = new Random();

	private static int partition(Comparable[] a, int lo, int hi) {  // Partition into a[lo..i-1], a[i], a[i+1..hi].
		int i = lo, j = hi + 1;            // left and right scan indices
		Comparable v = a[lo];            // partitioning item
		while (true) {  // Scan right, scan left, check for scan complete, and exchange.
			while (less(a[++i], v)) if (i == hi) break;
			while (less(v, a[--j])) if (j == lo) break;
			if (i >= j) break;
			exch(a, i, j);
		}
		exch(a, lo, j);
		return j;
	}

	public static Comparable select(Comparable[] a, int k) {
		//shuffle(a); //TODO Should we have shuffle? 2x loading time... is it necessary for efficient quickselect?
		int lo = 0, hi = a.length - 1;
		while (hi > lo) {
			int j = partition(a, lo, hi);
			if     (j == k)  return a[k];
			else if (j > k)  hi = j - 1;
			else if (j < k)  lo = j + 1;
		}
		return a[k];
	}

	// is v < w ?
	private static boolean less(Comparable v, Comparable w) {
		if (v == w) return false;   // optimization when reference equals
		return v.compareTo(w) < 0;
	}

	// exchange a[i] and a[j]
	private static void exch(Object[] a, int i, int j) {
		Object swap = a[i];
		a[i] = a[j];
		a[j] = swap;
	}

	/**
	 * Rearrange the elements of an array in random order.
	 */
	private static void shuffle(Object[] a) {
		int N = a.length;
		for (int i = 0; i < N; i++) {
			int r = i + random.nextInt(N-i);     // between i and N-1
			Object temp = a[i];
			a[i] = a[r];
			a[r] = temp;
		}
	}
}
