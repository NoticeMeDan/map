package com.noticemedan.map.model.utilities;

import io.vavr.control.Try;
import lombok.Data;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.Serializable;

@Data
public class Coordinate extends Point2D implements Serializable {
	private double x;
	private double y;

	public Coordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @param lat			The WGS-84 latitude value.
	 * @return				Mercator projection of latitude value (to be used in canvas).
	 */
	public static double lat2CanvasLat(double lat) {
		double latRad = Math.toRadians(lat);
		double projectionInRad = (Math.log(Math.tan(latRad/2 + Math.PI/4)));
		double projectionInDeg = Math.toDegrees(projectionInRad);
		return -projectionInDeg;
	}

	/**
	 * @param lon			The WGS-84 longitude value.
	 * @return				Mercator projection of longitude value (to be used in canvas).
	 */
	public static double lon2CanvasLon(double lon) {
		return lon;
	}

	/**
	 * @param canvasLat		The canvas latitude (using mercator projection)
	 * @return				WGS-84 latitude.
	 */
	public static double canvasLat2Lan(double canvasLat) {
		double canvasLatRad = Math.toRadians(canvasLat);
		double latInRad = -(2*Math.atan(Math.exp(canvasLatRad))-Math.PI/2);
		double latInDeg = Math.toDegrees(latInRad);
		return latInDeg;
	}

	/**
	 *
	 * @param viewportPoint 	A coordinate from viewport (e.g. from a mouse click).
	 * @param transform			The affine transformation.
	 * @return					The corrosponding point in canvas lat/lon.
	 */
	public static Point2D viewportPoint2canvasPoint(Point2D viewportPoint, AffineTransform transform) {
		try {
			return transform.inverseTransform(viewportPoint, null);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		return null;
	}
}
