package com.noticemedan.mappr.model.util;

import com.noticemedan.mappr.model.map.Element;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

public class ElementTransform {
	public static ArrayList<Element> determineRoadMultiplicity(Element road) {
		ArrayList<Element> identicalRoads =  new ArrayList<>();

		Rectangle roadBounds = road.getShape().getBounds();
		double roadBoundsEuclidianLength =
				Coordinate.euclidianDistance(
						new Coordinate(roadBounds.getX(), roadBounds.getY()),
						new Coordinate(roadBounds.getX() + roadBounds.getWidth(), roadBounds.getY() + roadBounds.getHeight()));

		if (roadBoundsEuclidianLength > 0.03) identicalRoads.addAll(cloneElementWithDifferentRepresentativePoint(road, 20));
		return identicalRoads;
	}

	public static ArrayList<Element> determineELementMultiplicity(Element element) {
		ArrayList<Element> identicalElements =  new ArrayList<>();

		Rectangle elementBounds = element.getShape().getBounds();
		double elementBoundsArea = elementBounds.getHeight() * elementBounds.getWidth();
		if (elementBoundsArea > 0.04) identicalElements.addAll(cloneElementWithDifferentRepresentativePoint(element, 20));
		return identicalElements;
	}

	public static ArrayList<Element> cloneElementWithDifferentRepresentativePoint(Element element, int j) {
		ArrayList<Element> clonedElements =  new ArrayList<>();
		int i = 0;
		for (PathIterator pi = element.getShape().getPathIterator(null); !pi.isDone(); pi.next()) {
			if (i % j == 0) {
				double[] currentShapePointCoordinateArray = new double[2];
				pi.currentSegment(currentShapePointCoordinateArray); // Inserts current coordinates into currentShapePointCoordinateArray;
				Coordinate currentShapePointCoordinate = new Coordinate(currentShapePointCoordinateArray[0], currentShapePointCoordinateArray[1]);
				Element clone = Element.cloneElement(element);
				clone.setAvgPoint(currentShapePointCoordinate);
				clone.setShape(element.getShape());
				clonedElements.add(clone);
			}
			i++;
		}
		return clonedElements;
	}

	public static Element elementToLowerResolution(Element element, int j) {
		Element elementLowerResolution = Element.cloneElement(element);
		Path2D elementPath = new Path2D.Double(Path2D.WIND_EVEN_ODD);
		int i = 0;
		for (PathIterator pi = element.getShape().getPathIterator(null); !pi.isDone(); pi.next()) {
			double[] currentShapePointCoordinateArray = new double[2];
			pi.currentSegment(currentShapePointCoordinateArray);
			if (i == 0) elementPath.moveTo(currentShapePointCoordinateArray[0], currentShapePointCoordinateArray[1]);
			if (i % j == 0 && i != 0) {
				elementPath.lineTo(currentShapePointCoordinateArray[0], currentShapePointCoordinateArray[1]);
			}
			i++;
		}
		elementLowerResolution.setShape(elementPath);
		return elementLowerResolution;
	}
}
