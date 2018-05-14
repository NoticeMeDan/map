package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.model.map.Boundaries;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.pathfinding.PathEdge;
import com.noticemedan.mappr.model.pathfinding.PathNode;
import com.noticemedan.mappr.model.pathfinding.TravelType;
import com.noticemedan.mappr.model.user.FavoritePoi;
import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.model.util.Rect;
import com.noticemedan.mappr.view.CanvasView;
import io.vavr.collection.List;
import io.vavr.collection.Vector;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URISyntaxException;
import java.nio.file.Paths;

@Slf4j @Data
public class CanvasController {
	CanvasView canvasView;
	DomainFacade domain;

	private BufferedImage start;
	private BufferedImage goal;
	private BufferedImage pointer;
	private BufferedImage pointOfinterest;
	private Boundaries boundaries;
	private List<FavoritePoi> favoritePoints;


	public CanvasController(DomainFacade domainFacade) {
		this.domain = domainFacade;
		this.boundaries = this.domain.getBoundaries();
		this.favoritePoints = this.domain.getAllPoi();
		try {
			this.pointer = domain.getImageFromFS(Paths.get(CanvasController.class.getResource("/graphics/pointer.png").toURI())).get();
			this.pointOfinterest = domain.getImageFromFS(Paths.get(CanvasController.class.getResource("/graphics/point-of-interest.png").toURI())).get();
			this.start = domain.getImageFromFS(Paths.get(CanvasController.class.getResource("/graphics/start.png").toURI())).get();
			this.goal = domain.getImageFromFS(Paths.get(CanvasController.class.getResource("/graphics/goal.png").toURI())).get();
		} catch (URISyntaxException e) {
			log.error("An error occurred", e);
		}

		this.canvasView = new CanvasView(this);
	}

	public Vector<Element> getCoastLines() {
		return this.domain.getCoastLines();
	}

	public Vector<Element> doRangeSearch(Rect viewArea, double zoomLevel){
		return this.domain.doRangeSearch(viewArea, zoomLevel);
	}

	public Vector<PathEdge> deriveAllDijkstraEdges() {
		return this.domain.deriveAllDijkstraEdges();
	}

	public Vector<PathNode> deriveAllDijkstraNodes() {
		return this.domain.deriveAllDijkstraNodes();
	}

	public Vector<Shape> deriveRandomShortestPathShapes() {
		return this.domain.deriveRandomShortestPathShapes();
	}

	public Element doNearestNeighborSearch(Coordinate queryPoint, double zoomLevel) {
		return this.domain.doNearestNeighborSearch(queryPoint, zoomLevel);
	}

	public Element doNearestNeighborInCurrentRangeSearch(Coordinate queryPoint) {
		return this.domain.doNearestNeighborInCurrentRangeSearch(queryPoint,  TravelType.ALL);
	}

	public void updateFavoritePoints() {
		this.favoritePoints = domain.getAllPoi();
	}

}
