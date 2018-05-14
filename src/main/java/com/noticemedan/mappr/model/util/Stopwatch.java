package com.noticemedan.mappr.model.util;

public class Stopwatch {

	private final long start;

	/**
	 * Initializes a new stopwatch.
	 */
	public Stopwatch() {
		start = System.currentTimeMillis();
	}


	/**
	 * Returns the elapsed CPU time (in seconds) since the stopwatch was created.
	 *
	 * @return elapsed CPU time (in seconds) since the stopwatch was created
	 */
	public double elapsedTime() {
		long now = System.currentTimeMillis();
		return (now - start) / 1000.0;
	}

	public String toString() {
		return String.valueOf(elapsedTime());
	}
}
