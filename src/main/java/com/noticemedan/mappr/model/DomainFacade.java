package com.noticemedan.mappr.model;

import com.noticemedan.mappr.dao.ImageDao;
import com.noticemedan.mappr.dao.MapDao;
import com.noticemedan.mappr.dao.OsmDao;
import com.noticemedan.mappr.model.map.Address;
import com.noticemedan.mappr.model.map.Boundaries;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.pathfinding.PathEdge;
import com.noticemedan.mappr.model.pathfinding.PathNode;
import com.noticemedan.mappr.model.service.MapImportService;
import com.noticemedan.mappr.model.pathfinding.TravelType;
import com.noticemedan.mappr.model.service.ShortestPathService;
import com.noticemedan.mappr.model.service.ForestService;
import com.noticemedan.mappr.model.service.TextSearchService;
import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.model.map.FileInfo;
import com.noticemedan.mappr.model.util.Rect;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import io.vavr.control.Try;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.Executors;

@Slf4j
public class DomainFacade {
	// Domain data
	private MapData mapData;

	// Services
	private TextSearchService<Address> addressSearch;
	private ForestService forestService;
	private ShortestPathService shortestPathService;

	// Constants
	private final Path MAPPR_DIR = Paths.get(System.getProperty("user.home"), "/mappr/");

	public DomainFacade() {
		Try.of(() -> Paths.get(DomainFacade.class.getResource("/default.map").toURI()))
				.mapTry(new MapDao()::read)
				.onSuccess(this::initialize)
				.onFailure(e -> log.error("An error occurred while initializing program: ", e));
	}

	private void initialize(MapData mapdata) {
		this.mapData = mapdata;
		this.forestService = new ForestService(
				this.mapData.getElements(),
				this.mapData.getCoastlineElements());
		this.addressSearch = new TextSearchService<>(HashMap.ofEntries(
				this.mapData.getAddresses().map(x -> Tuple.of(x.toFullAddress(), x))));
		this.shortestPathService = new ShortestPathService(mapData.getElements());
	}

	/* SECTION START: VIEWPORT DATA */

	public Boundaries getBoundaries() { return this.mapData.getBoundaries(); }
	public Vector<Element> getCoastLines() { return this.forestService.getCoastlines(); }
	public Vector<Element> doRangeSearch(Rect area) { return this.forestService.rangeSearch(area); }
	public Vector<Element> doRangeSearch(Rect area, double zoom) { return this.forestService.rangeSearch(area, zoom); }
	public Element doNearestNeighborSearch(Coordinate queryPoint, double zoomLevel) { return this.forestService.nearestNeighbor(queryPoint, zoomLevel); }
	public Element doNearestNeighborInCurrentRangeSearch(Coordinate queryPoint, TravelType travelType) { return this.forestService.nearestNeighborInCurrentRangeSearch(queryPoint, travelType); }
	public Element doNearestNeighborNewRangeSearch(Coordinate queryPoint, TravelType travelType) {return this.forestService.nearestNeighborNewRangeSearch(queryPoint, travelType); }

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

	public Address getAddress(String search) {
		return this.addressSearch.getAddress(search);
	}

	/* SECTION END: ADDRESS SEARCHING */
	/* SECTION START: SHORTEST PATH */

	/**
	 * Derive shortest path as a Vector of Shapes given two coordinates
	 * @param from coordinate of the from-destination
	 * @param to coordinate of the to-destination
	 * @return Vector of Shapes
	 */
	public Vector<Shape> deriveShortestPathShapes(Coordinate from, Coordinate to, TravelType type) {
		return shortestPathService.getShortestPath(from, to, type);
	}

	/**
	 * Derive the distance of the shortest path between two given coordinates
	 * @param from coordinate of the from-destination
	 * @param to coordinate of the to-destination
	 * @return the distance of the path
	 */
	public double deriveShortestPathDistance(Coordinate from, Coordinate to, TravelType type) {
		return shortestPathService.getPathDistance(from, to, type);
	}

	/**
	 * DEVELOPMENT OPTION
	 * Derive a random shortest path as a Vector of Shapes
	 * From- and To-destination will be randomly picked from the network
	 * @return Vector of Shapes of random shortest path
	 */
	public Vector<Shape> deriveRandomShortestPathShapes() {
		return shortestPathService.getRandomShortestPath(TravelType.ALL);
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

	/**
	 * Derive a BufferedImage from a given filepath
	 * @param input path to file
	 * @return BufferedImage if file exists
	 */
	public Option<BufferedImage> getImageFromFS(Path input) {
		ImageDao dao = new ImageDao();
		return Try.of(() -> dao.read(input))
				.toOption();
	}

	/* SECTION END: IMAGE DAO */
	/* SECTION START: .map HANDLING */

	/**
	 * Builds .map from .osm/.zip in background, and notifies on completion
	 * @param from The path to get the .osm file from
	 * @param onSuccess What to do on success
	 * @param onFailed What to do on failure
	 */
	public void buildMapFromOsmPath(Path from, EventHandler<WorkerStateEvent> onSuccess, EventHandler<WorkerStateEvent> onFailed) {
		String filename = FilenameUtils.getBaseName(from.toString()) + "__" + UUID.randomUUID() + ".map";
		Path to = Paths.get(MAPPR_DIR.toString(), filename);
		MapImportService importer = new MapImportService(from, to, new OsmDao(), new MapDao());
		importer.setOnSucceeded(onSuccess);
		importer.setOnFailed(onFailed);
		Executors.newSingleThreadExecutor().execute(importer);
	}

	public Option<Path> loadMap(String name) {
		Path path = Paths.get(MAPPR_DIR.toString(), name);
		try {
			this.initialize(new MapDao().read(path));
			return Option.of(path);
		} catch (IOException e) {
			log.error("An error occurred while loading .map: ", e);
			return Option.none();
		}
	}

	public Option<Path> updateMap(String name) {
		Path path = Paths.get(MAPPR_DIR.toString(), name);
		try {
			new MapDao().write(path, this.mapData);
			return Option.of(path);
		} catch (IOException e) {
			log.error("An error occurred while saving .map: ", e);
			return Option.none();
		}
	}

	public Option<Path> deleteMap(String name) {
		Path path = Paths.get(MAPPR_DIR.toString(), name);
		return Try.of(() -> new MapDao().delete(path))
				.toOption();
	}

	public List<FileInfo> getAllFileInfoFromMapprDir() {
		return Try.of(() -> new MapDao().getAllFileInfoFromDirectory(MAPPR_DIR))
				.getOrElse(List.empty());
	}

	/* SECTION END: .map HANDLING */
}
