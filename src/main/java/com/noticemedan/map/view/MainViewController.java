package com.noticemedan.map.view;

import com.noticemedan.map.model.Entities;
import com.noticemedan.map.model.user.FavoritePoiManager;
import com.noticemedan.map.model.utilities.Coordinate;
import com.noticemedan.map.model.utilities.TextFormatter;
import com.noticemedan.map.viewmodel.CanvasView;
import com.noticemedan.map.viewmodel.MouseController;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import lombok.Getter;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import javax.swing.SwingUtilities;

public class MainViewController {
	@Getter
	private static CanvasView canvasView;
	FavoritePoiManager favoritePoiManager;

	@FXML
	AnchorPane mainView;

	//searchFieldImitator
	@FXML Button routeButton;
	@FXML Button favoriteButton;
	@FXML Pane searchFieldImitator;

	//scalaBar
	@FXML Text scalaBarDistanceText;

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

	//MainView controllers
	private MouseController mouseController;
	private KeyboardController keyboardController;

	public void updateScalaBar() {
		Point2D scalaBarFirstPoint = Coordinate.viewportPoint2canvasPoint(new Point2D.Double(0,0), canvasView.getTransform());
		Point2D scalaBarSecondPoint = Coordinate.viewportPoint2canvasPoint(new Point2D.Double(130,0), canvasView.getTransform());
		Coordinate scalaBarFirstCoordinate = new Coordinate(scalaBarFirstPoint.getX(), Coordinate.canvasLat2Lat(scalaBarFirstPoint.getY()));
		Coordinate scalaBarSecondCoordinate = new Coordinate(scalaBarSecondPoint.getX(), Coordinate.canvasLat2Lat(scalaBarSecondPoint.getY()));
		scalaBarDistanceText.setText(String.valueOf(TextFormatter.formatDistance(Coordinate.haversineDistance(scalaBarFirstCoordinate, scalaBarSecondCoordinate, 6378.137),2)));
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
		canvasView = new CanvasView();
		canvasView.setSize(screenSize);
		System.out.println(Entities.writeOut());
		canvasView.pan(-Entities.getMinLon(), -Entities.getMaxLat());
		canvasView.zoom(screenSize.getWidth() / (Entities.getMaxLon() - Entities.getMinLon()), 0, 0);
		canvasView.setZoomLevel(1 / (Entities.getMaxLon() - Entities.getMinLon()));
		this.mouseController = new MouseController(canvasView, this);
		mainView.addEventHandler(KeyEvent.KEY_PRESSED, new KeyboardController(canvasView));
		SwingUtilities.invokeLater(() -> swingNode.setContent(canvasView));
		osmPaneContainer.getChildren().addAll(swingNode);
	}

	private void eventListeners() {
		searchFieldImitator.setOnMouseClicked(event -> searchPaneController.openSearchPane());
		favoriteButton.setOnAction(event -> favoritePoiPaneController.openFavoritePane());
		routeButton.setOnAction(event -> routePaneController.openRoutePane());
		swingNode.addEventHandler(MouseEvent.ANY, new ClickDragHandler(
				event -> poiBoxViewController.closePoiBox(),
				event -> poiBoxViewController.openPoiBox(
						new Coordinate(
								mouseController.getLastMousePositionCanvasCoords().getX(),
								Coordinate.canvasLat2Lat(mouseController.getLastMousePositionCanvasCoords().getY())
						)
				)
		));
	}
}
