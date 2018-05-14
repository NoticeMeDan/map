package com.noticemedan.mappr.model.util;

import com.noticemedan.mappr.model.map.Element;
import io.vavr.collection.Vector;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;

public class ElementMutator {
	public static Vector<Element> determineRoadMultiplicity(Element road) {
		Vector<Element> identicalRoads =  Vector.empty();

		Rectangle roadBounds = road.getShape().getBounds();
		double roadBoundsEuclidianLength =
				Coordinate.euclidianDistance(
						new Coordinate(roadBounds.getX(), roadBounds.getY()),
						new Coordinate(roadBounds.getX() + roadBounds.getWidth(), roadBounds.getY() + roadBounds.getHeight()));

		if (roadBoundsEuclidianLength > 0.03) identicalRoads = identicalRoads.appendAll(cloneElementWithDifferentRepresentativePoint(road, 20));
		return identicalRoads;
	}

	public static Vector<Element> determineELementMultiplicity(Element element) {
		Vector<Element> identicalElements =  Vector.empty();

		Rectangle elementBounds = element.getShape().getBounds();
		double elementBoundsArea = elementBounds.getHeight() * elementBounds.getWidth();
		if (elementBoundsArea > 0.04) identicalElements = identicalElements.appendAll(cloneElementWithDifferentRepresentativePoint(element, 20));
		return identicalElements;
	}

	public static Vector<Element> cloneElementWithDifferentRepresentativePoint(Element element, int j) {
		Vector<Element> clonedElements =  Vector.empty();
		int i = 0;
		for (PathIterator pi = element.getShape().getPathIterator(null); !pi.isDone(); pi.next()) {
			if (i % j == 0) {
				double[] currentShapePointCoordinateArray = new double[2];
				pi.currentSegment(currentShapePointCoordinateArray); // Inserts current coordinates into currentShapePointCoordinateArray;
				Coordinate currentShapePointCoordinate = new Coordinate(currentShapePointCoordinateArray[0], currentShapePointCoordinateArray[1]);
				Element clone = Element.cloneElement(element);
				clone.setAvgPoint(currentShapePointCoordinate);
				clone.setShape(element.getShape());
				clonedElements = clonedElements.append(clone);
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
