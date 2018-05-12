package com.noticemedan.mappr.model.pathfinding;

import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.map.Type;
import io.vavr.collection.Vector;
import lombok.Getter;

import java.awt.geom.PathIterator;

public class NetworkParser {

	@Getter
	private Network network;
	private double[] coords = new double[6];

	public NetworkParser(Vector<Element> elements) {
		this.network = new Network();
		elements.filter(element -> element.isRoad()).forEach(e -> {
			PathNode from = null;
			for(PathIterator pi = e.getShape().getPathIterator(null); !pi.isDone(); pi.next()) {
				int type = pi.currentSegment(coords);
				double[] pathCoords = {type, coords[0], coords[1]};
				PathNode to = createPathNode(pathCoords[1], pathCoords[2]);
				if (from == null) from = createPathNode(pathCoords[1],pathCoords[2]);
				else {
					network.addPath(from,to);
					from = to;
				}
			}

		});
	}

	private PathNode createPathNode(double lon, double lat) {
		return PathNode.builder()
				.id(this.network.getAllNodes().length())
				.lon(lon)
				.lat(lat)
				.edges(Vector.empty())
				.build();
	}

	//Testing purpose
	private void iterateThroughPoints(Element e) {
		for(PathIterator pi = e.getShape().getPathIterator(null); !pi.isDone(); pi.next()) {
			int type = pi.currentSegment(coords);
			double[] pathCoords = {type, coords[0], coords[1]};
			System.out.println("Coords: " + pathCoords[1] + " , " + pathCoords[2]);
		}
	}
}
