package com.noticemedan.mappr.model;

import com.noticemedan.mappr.dao.ImageDao;
import com.noticemedan.mappr.dao.MapDao;
import com.noticemedan.mappr.dao.OsmDao;
import com.noticemedan.mappr.model.map.Address;
import com.noticemedan.mappr.model.map.Boundaries;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.map.FileInfo;
import com.noticemedan.mappr.model.pathfinding.PathEdge;
import com.noticemedan.mappr.model.pathfinding.PathNode;
import com.noticemedan.mappr.model.pathfinding.ShortestPath;
import com.noticemedan.mappr.model.pathfinding.TravelType;
import com.noticemedan.mappr.model.service.ForestService;
import com.noticemedan.mappr.model.service.MapImportService;
import com.noticemedan.mappr.model.service.ShortestPathService;
import com.noticemedan.mappr.model.service.TextSearchService;
import com.noticemedan.mappr.model.user.FavoritePoi;
import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.model.util.JarHelper;
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
		Try.of(() -> JarHelper.resourceToPath(DomainFacade.class.getResource("/default.map")))
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

	/**
	 * @return 					The boundaries of current loaded map.
	 */
	public Boundaries getBoundaries() { return this.mapData.getBoundaries(); }

	/**
	 * @param zoomLevel			The zoom level of the map.
	 * @return 					Coastline elements in a resolution according to the input zoom level.
	 */
	public Vector<Element> getCoastLines(double zoomLevel) { return this.forestService.getCoastlines(zoomLevel); }

	/**
	 * @param area				The area (defined as a rectangle) used to search for elements in forestService
	 * @param zoomLevel			The zoom level of the map
	 * @return					The elements positioned at the zoom level within the area.
	 */
	public Vector<Element> doRangeSearch(Rect area, double zoomLevel) { return this.forestService.rangeSearch(area, zoomLevel); }

	/**
	 * Uses nearest neighbor algorithm as described by the slides provided by Rasmus Pagh (1).
	 * (1) Pagh R. (2018). Lecture 5: Spatial Data Structures. (Slides found on Learn-IT).
	 * @param queryPoint		The point of interest to find nearest neighbor
	 * @param zoomLevel			The zoom level of the map
	 * @return					The nearest Element (neighbor) to the point of interest.
	 * 							Can be all types of elements stored in forestService.
	 */
	public Element doNearestNeighborSearch(Coordinate queryPoint, double zoomLevel) { return this.forestService.nearestNeighbor(queryPoint, zoomLevel); }

	/**
	 * A brute-force nearest neighbor algorithm that examines all elements currently in the map,
	 * by examining each element's nodes.
	 * @param queryPoint		The point of interest to find nearest neighbor
	 * @param travelType        The type of road element of interest (e.g. CAR to return elements that a car can drive on)
	 * @return					The nearest Element (neighbor) to the point of interest.
	 * 							The element will be a type of road.
	 */
	public Element doNearestNeighborInCurrentRangeSearch(Coordinate queryPoint, TravelType travelType) { return this.forestService.nearestNeighborInCurrentRangeSearch(queryPoint, travelType); }

	/**
	 * A nearest neighbor algorithm that efficiently uses multiple expanding range searches to find
	 * nearest neighbor of interest.
	 * @param queryPoint		The point of interest to find nearest neighbor
	 * @param travelType        The type of road element of interest (e.g. CAR to return elements that a car can drive on)
	 * @param zoomLevel			The zoom level of the map
	 * @return					The nearest Element (neighbor) to the point of interest.
	 * 							The element will be a type of road.
	 */
	public Element doNearestNeighborUsingRangeSearch(Coordinate queryPoint, TravelType travelType, double zoomLevel) {return this.forestService.nearestNeighborUsingRangeSearch(queryPoint, travelType, zoomLevel); }

	/* SECTION END: VIEWPORT DATA */
	/* SECTION START: ADDRESS SEARCHING */

	/**
	 * Query the AddressSearchService for matching addresses
	 * @param search 			The Address to query for
	 * @return 					List of Address strings matching the query
	 */
	public List<String> doAddressSearch(String search) {
		return this.addressSearch.search(search)
				.map(Tuple2::_1);
	}

	public Address getAddress(String search) {
		return this.addressSearch.getAddress(search);
	}

	/* SECTION END: ADDRESS SEARCHING */
	/* SECTION START: SHORTEST PATH */

	/**
	 * Derive shortest path as a ShortestPath object given two coordinates
	 * @param from coordinate of the from-destination
	 * @param to coordinate of the to-destination
	 * @return ShortestPath
	 */
	public ShortestPath deriveShortestPath(Coordinate from, Coordinate to, TravelType type) {
		return shortestPathService.getShortestPath(from, to, type);
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
	/* SECTION START: LOAD FROM OSM HANDLING */

	public Option<Path> loadMapFromOsm(Path path) {
		try {
			this.initialize(new OsmDao().read(path));
			return Option.of(path);
		} catch (IOException e) {
			log.error("An error occurred while loading from Osm: ", e);
			return Option.none();
		}
	}

	/* SECTION END: LOAD FROM OSM HANDLING */
	/* SECTION START: FAVORITE POI */
	public List<FavoritePoi> getAllPoi() {
		return this.mapData.getPoi();
	}

	public List<FavoritePoi> addPoi(FavoritePoi poi) {
		this.mapData.setPoi(this.mapData.getPoi().append(poi));
		return getAllPoi();
	}

	public List<FavoritePoi> removePoi(FavoritePoi poi) {
		this.mapData.setPoi(this.mapData.getPoi().remove(poi));
		return getAllPoi();
	}
	/* SECTION END: FAVORITE POI */
}
