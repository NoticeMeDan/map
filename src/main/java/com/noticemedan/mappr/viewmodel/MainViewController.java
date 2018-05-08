package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.model.Entities;
import com.noticemedan.mappr.model.user.FavoritePoiManager;
import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.viewmodel.event.ClickDragHandler;
import com.noticemedan.mappr.viewmodel.event.KeyboardHandler;
import com.noticemedan.mappr.viewmodel.event.MouseHandler;
import com.noticemedan.mappr.viewmodel.poi.FavoritePoiPaneController;
import com.noticemedan.mappr.viewmodel.poi.PoiBoxViewController;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Getter;

import java.awt.Dimension;
import javax.inject.Inject;
import javax.swing.SwingUtilities;

public class MainViewController {
	@Getter
	private static CanvasView canvasView;
	private FavoritePoiManager favoritePoiManager;

	@FXML
	AnchorPane mainView;

	//searchFieldImitator
	@FXML Button routeButton;
	@FXML Button favoriteButton;
	@FXML Pane searchFieldImitator;

	// OSM
	@FXML Pane osmPaneContainer;
	private SwingNode swingNode;

	//Components
	@FXML Pane routePane;
	@FXML Pane favoritePoiPane;
	@FXML Pane poiBoxView;
	@FXML Pane searchPane;

	//Component controllers
	@FXML private PoiBoxViewController poiBoxViewController;
	@FXML private FavoritePoiPaneController favoritePoiPaneController;
	@FXML private SearchPaneController searchPaneController;
	@FXML private RoutePaneController routePaneController;

	//Canvas controllers
	private MouseHandler mouseHandler;

	// Model Facade
	private DomainFacade domain;

	@Inject
	public MainViewController(DomainFacade domainFacade) {
		this.domain = domainFacade;
	}

	public void initialize() {
		favoritePoiManager = new FavoritePoiManager();
		poiBoxViewController.setFavoritePois(favoritePoiManager.getObservableFavoritePOIs());
		favoritePoiPaneController.setFavoritePois(favoritePoiManager.getObservableFavoritePOIs());

		insertOSMPane();
		eventListeners();
	}

	private void insertOSMPane() {
		Dimension screenSize = new Dimension(1100, 650);
		swingNode = new SwingNode();
		canvasView = new CanvasView(this.domain);
		canvasView.setSize(screenSize);
		System.out.println(Entities.writeOut());
		canvasView.pan(-Entities.getMinLon(), -Entities.getMaxLat());
		canvasView.zoom(screenSize.getWidth() / (Entities.getMaxLon() - Entities.getMinLon()), 0, 0);
		canvasView.setZoomLevel(1 / (Entities.getMaxLon() - Entities.getMinLon()));
		this.mouseHandler = new MouseHandler(canvasView);
		mainView.addEventHandler(KeyEvent.KEY_PRESSED, new KeyboardHandler(canvasView));

		SwingUtilities.invokeLater(() -> swingNode.setContent(canvasView));
		osmPaneContainer.getChildren().addAll(swingNode);
	}

	private void eventListeners() {
		searchFieldImitator.setOnMouseClicked(event -> searchPaneController.openSearchPane());
		favoriteButton.setOnAction(event -> favoritePoiPaneController.openFavoritePane());
		routeButton.setOnAction(event -> routePaneController.openRoutePane());

		//TODO: @Emil Point2D or Coordinate? Save proper coordinates (real lat lon)
		swingNode.addEventHandler(MouseEvent.ANY, new ClickDragHandler(
				event -> poiBoxViewController.closePoiBox(),
				event -> poiBoxViewController.openPoiBox(
						new Coordinate(
								mouseHandler.getLastMousePositionModelCoords().getX(),
								mouseHandler.getLastMousePositionModelCoords().getY()
						)
				)
		));
	}
}
