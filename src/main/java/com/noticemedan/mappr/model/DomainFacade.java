package com.noticemedan.mappr.model;

import com.noticemedan.mappr.dao.ImageDao;
import com.noticemedan.mappr.dao.MapDao;
import com.noticemedan.mappr.dao.OsmDao;
import com.noticemedan.mappr.model.map.Address;
import com.noticemedan.mappr.model.map.Boundaries;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.pathfinding.PathEdge;
import com.noticemedan.mappr.model.pathfinding.PathNode;
import com.noticemedan.mappr.model.service.ShortestPathService;
import com.noticemedan.mappr.model.service.ForestService;
import com.noticemedan.mappr.model.service.TextSearchService;
import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.model.util.Rect;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class DomainFacade {
	// Domain data
	private MapData mapData;

	// Services
	private TextSearchService<Address> addressSearch;
	private ForestService forestService;
	private ShortestPathService shortestPathService;

	public DomainFacade() {
		Path path = Try.of(() -> Paths.get(DomainFacade.class.getResource("/default.map").toURI()))
				.getOrNull();
		this.initialize(path);
	}

	private void initialize(Path path) {
		try {
			this.mapData = new MapDao().read(path); // Switch to MapData
		} catch (IOException e) {
			log.error("An error occurred loading .map file", e);
		}

		this.forestService = new ForestService(
				this.mapData.getElements().toJavaList(),
				this.mapData.getCoastlineElements().toJavaList());
		this.addressSearch = new TextSearchService<>(HashMap.ofEntries(
				this.mapData.getAddresses().map(x -> Tuple.of(x.toFullAddress(), x))));
		this.shortestPathService = new ShortestPathService(mapData.getElements());
	}

	/* SECTION START: VIEWPORT DATA */

	public Boundaries getBoundaries() { return this.mapData.getBoundaries(); }
	public List<Element> getCoastLines() { return this.forestService.getCoastlines(); }
	public List<Element> doRangeSearch(Rect area) { return this.forestService.rangeSearch(area); }
	public List<Element> doRangeSearch(Rect area, double zoom) { return this.forestService.rangeSearch(area, zoom); }

	/* SECTION END: VIEWPORT DATA */
	/* SECTION START: ADDRESS SEARCHING */

	/**
	 * Query the AddressSearchService for matching addresses
	 * @param search The Address to query for
	 * @return List of Address strings matching the query
	 */
	public io.vavr.collection.List<String> doAddressSearch(String search) {
		return this.addressSearch.search(search)
				.map(Tuple2::_1);
	}

	/* SECTION END: SHORTEST PATH */

	/* SECTION START: SHORTEST PATH */

	/**
	 * Derive shortest path as a Vector of Shapes given two coordinates
	 * @param from coordinate of the from-destination
	 * @param to coordinate of the to-destination
	 * @return Vector of Shapes
	 */
	public Vector<Shape> deriveShortestPathShapes(Coordinate from, Coordinate to) {
		return shortestPathService.getShortestPath(from, to);
	}
	/**
	 * DEVELOPMENT OPTION
	 * Derive a random shortest path as a Vector of Shapes
	 * From- and To-destination will be randomly picked from the network
	 * @return Vector of Shapes of random shortest path
	 */
	public Vector<Shape> deriveRandomShortestPathShapes() {
		return shortestPathService.getRandomShortestPath();
	}

	/**
	 * DEVELOPMENT OPTION
	 * Derive every PathNode object related to Dijkstra network
	 * @return Vector of PathNode objects
	 */
	public Vector<PathNode> deriveAllDijkstraNodes() {
		return shortestPathService.getAllNodes();
	}

	/**
	 * DEVELOPMENT OPTION
	 * Derive every PathEdge object related to Dijkstra network
	 * @return Vector of PathEdge objects
	 */
	public Vector<PathEdge> deriveAllDijkstraEdges() {
		return shortestPathService.getAllEdges();
	}

	/* SECTION END: SHORTEST PATH */

	/* SECTION START: IMAGE DAO */
	public Option<BufferedImage> getImageFromFS(Path input) {
		ImageDao dao = new ImageDao();
		return Try.of(() -> dao.read(input))
				.toOption();
	}
}
