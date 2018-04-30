package com.noticemedan.map.model.utilities;

import lombok.Data;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.Serializable;

@Data
public class Coordinate extends Point2D implements Serializable {
	private double x; // lon
	private double y; // lat

	// TODO @Magnus why even have this constructor? Can't we just call its parent constructur?
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
	public static double canvasLat2Lat(double canvasLat) {
		double canvasLatRad = Math.toRadians(canvasLat);
		double latInRad = -(2*Math.atan(Math.exp(canvasLatRad))-Math.PI/2);
		double latInDeg = Math.toDegrees(latInRad);
		return latInDeg;
	}

	/**
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

	/**
	 * Source of maths: https://www.movable-type.co.uk/scripts/latlong.html
	 * @param a 				First coordinate with WGS-84 lat/lon
	 * @param b					Second coordinate with WGS-84 lat/lon
	 * @param R					Radius of the earth (e.g. 6378 km if result should be in km)
	 * @return					The distance between two points on sphere
	 */
	public static double haversineDistance(Coordinate a, Coordinate b, double R) {
		double lat1 = Math.toRadians(a.getY());
		double lat2 = Math.toRadians(b.getY());
		double dLat = Math.toRadians(b.getY() - a.getY());
		double dLon = Math.toRadians(b.getX() - a.getX());
		double f = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon/2) * Math.sin(dLon/2);
		double c = 2 * Math.atan2(Math.sqrt(f), Math.sqrt(1-f));
		return R * c;
	}

	/**
	 * @param a 				First coordinate with WGS-84 lat/lon
	 * @param b					Second coordinate with WGS-84 lat/lon
	 * @return					The distance between two points on a plane.
	 */
	public static double euclidianDistance(Coordinate a, Coordinate b) {
		double dX = b.getX() - a.getX();
		double dY = b.getY() - a.getY();
		return Math.sqrt(Math.pow(dX , 2) + Math.pow(dY, 2));
	}

	/**
	 * @Param query 			The query coordinate
	 * @param a 				First coordinate
	 * @param b					Second coordinate
	 * @return					The closest coordinate
	 */
	public static Coordinate closest(Coordinate query, Coordinate a, Coordinate b) {
		double distanceQA = euclidianDistance(query, a);
		double distanceQB = euclidianDistance(query, b);
		if (distanceQA < distanceQB) return  a;
		else return b;
	}
}
