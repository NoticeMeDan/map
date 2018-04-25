package com.noticemedan.map.view;

import com.noticemedan.map.model.Entities;
import com.noticemedan.map.model.user.FavoritePoiManager;
import com.noticemedan.map.model.utilities.Coordinate;
import com.noticemedan.map.viewmodel.CanvasView;
import com.noticemedan.map.viewmodel.MouseController;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import java.awt.Dimension;
import javax.swing.SwingUtilities;

public class MainViewController {
	FavoritePoiManager favoritePoiManager;

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
	@FXML PoiBoxViewController poiBoxViewController;
	@FXML FavoritePoiPaneController favoritePoiPaneController;
	@FXML SearchPaneController searchPaneController;
	@FXML RoutePaneController routePaneController;

	//Canvas controllers
	private MouseController mouseController;


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
		CanvasView canvas = new CanvasView();
		canvas.setSize(screenSize);
		System.out.println(Entities.writeOut());
		canvas.pan(-Entities.getMinLon(), -Entities.getMaxLat());
		canvas.zoom(screenSize.getWidth() / (Entities.getMaxLon() - Entities.getMinLon()), 0, 0);
		canvas.setZoomLevel(1 / (Entities.getMaxLon() - Entities.getMinLon()));
		this.mouseController = new MouseController(canvas);
		mainView.addEventHandler(KeyEvent.KEY_PRESSED, new KeyboardHandler(canvas));

		SwingUtilities.invokeLater(() -> swingNode.setContent(canvas));
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
								mouseController.getLastMousePositionModelCoords().getX(),
								mouseController.getLastMousePositionModelCoords().getY()
						)
				)
		));
	}
}
