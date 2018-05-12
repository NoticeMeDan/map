package com.noticemedan.mappr.model.pathfinding;

import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.map.Type;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.collection.Vector;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.geom.PathIterator;

@Slf4j
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
				PathNode to = createPathNode(pathCoords[1], pathCoords[2],e.getMaxspeed());
				if (from == null) from = createPathNode(pathCoords[1],pathCoords[2],e.getMaxspeed());
				else {
					network.addPath(from, to, this.getTravelType(e));
					from = to;
				}
			}
		});

		log.info("Dijkstra node size: " + network.getAllNodes().length());
		log.info("Dijkstra edge size: " + network.getAllEdges().length());
	}

	private PathNode createPathNode(double lon, double lat, int maxspeed) {
		return PathNode.builder()
				.id(this.network.getAllNodes().length())
				.lon(lon)
				.lat(lat)
				.maxspeed(maxspeed)
				.edges(Vector.empty())
				.build();
	}

	private Set<TravelType> getTravelType(Element e) {
		Set<TravelType> allowedTypes = HashSet.empty();

		allowedTypes = allowedTypes.add(TravelType.ALL);

		if (e.getType() == Type.FOOTWAY) allowedTypes = allowedTypes.add(TravelType.WALK);
		if (e.getType() == Type.CYCLEWAY) allowedTypes = allowedTypes.add(TravelType.BIKE);
		if (e.getType() == Type.MOTORWAY || e.getType() == Type.SECONDARY || e.getType() == Type.PRIMARY) allowedTypes = allowedTypes.add(TravelType.CAR);
		if (e.getType() == Type.ROAD || e.getType() == Type.SERVICE) {
			allowedTypes = allowedTypes.add(TravelType.CAR);
			allowedTypes = allowedTypes.add(TravelType.BIKE);
			allowedTypes = allowedTypes.add(TravelType.WALK);
		}
		if (e.getType() == Type.TRACK || e.getType() == Type.PATH || e.getType() == Type.UNCLASSIFIED) {
			allowedTypes = allowedTypes.add(TravelType.BIKE);
			allowedTypes = allowedTypes.add(TravelType.WALK);
		}

		return allowedTypes;
	}
}
