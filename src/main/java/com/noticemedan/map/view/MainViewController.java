package com.noticemedan.map.view;

import com.noticemedan.map.App;
import com.noticemedan.map.model.Entities;
import com.noticemedan.map.model.user.FavoritePoiManager;
import com.noticemedan.map.model.utilities.Coordinate;
import com.noticemedan.map.model.utilities.TextFormatter;
import com.noticemedan.map.viewmodel.CanvasView;
import com.noticemedan.map.viewmodel.MouseController;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.geom.Point2D;


public class MainViewController {
	@Getter
	private static CanvasView canvas;
	FavoritePoiManager favoritePoiManager;

	@FXML AnchorPane mainView;
	private int latestSceneWidth;
	private int latestSceneHeight;
	@Setter
	Stage stage;
	int width = 1100;
	int height = 800;

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
	private MouseController mouseController;

	@FXML
	public void initialize() {
		favoritePoiManager = new FavoritePoiManager();
		poiBoxViewController.setFavoritePois(favoritePoiManager.getObservableFavoritePOIs());
		favoritePoiPaneController.setFavoritePois(favoritePoiManager.getObservableFavoritePOIs());
		insertOSMPane();
		favoritePoiPaneController.setCanvas(canvas);
		eventListeners();
	}

	public void setUpStage() {
		Image icon = new Image(App.class.getResourceAsStream("/media/icon.png"));

		stage.setTitle("Mappr");
		stage.getIcons().add(icon);
		stage.setMinWidth(800);
		stage.setMinHeight(700);
		stage.setWidth(1100);
		stage.setHeight(800);
		stage.setScene(new Scene(mainView));

		ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
			width = (int) stage.getWidth();
			height = (int) stage.getHeight();
			resizeNode(width, height);
		};

		stage.widthProperty().addListener(stageSizeListener);
		stage.heightProperty().addListener(stageSizeListener);

		stage.show();
	}

	private void resizeNode(int width, int height) {
		canvas.setPreferredSize(new Dimension(width, height));
		swingNode.setContent(canvas);
	}

	private void insertOSMPane() {
		swingNode = new SwingNode();
		canvas = new CanvasView();
		canvas.pan(-Entities.getMinLon(), -Entities.getMaxLat());
		canvas.zoom(1100 / (Entities.getMaxLon() - Entities.getMinLon()), 0, 0);
		canvas.setZoomLevel(1 / (Entities.getMaxLon() - Entities.getMinLon()));
		canvas.setPreferredSize(new Dimension(1100, 800));
		swingNode.setContent(canvas);
		osmPaneContainer.getChildren().addAll(swingNode);
	}

	private void eventListeners() {
		this.mouseController = new MouseController(canvas, this);
		mainView.addEventHandler(KeyEvent.KEY_PRESSED, new KeyboardController(canvas));
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

	/*private void windowResizeListeners() {
		osmPaneContainer.widthProperty().addListener(
				(observableValue, oldSceneWidth, newSceneWidth) -> {
					latestSceneWidth = newSceneWidth.intValue();
					canvas.resizeCanvasToSceneSize(newSceneWidth.intValue(), latestSceneHeight);
				});
		osmPaneContainer.heightProperty().addListener(
				(observableValue, oldSceneHeight, newSceneheight) -> {
					latestSceneHeight = newSceneheight.intValue();
					canvas.resizeCanvasToSceneSize(latestSceneWidth, newSceneheight.intValue());
				});
	}*/

	public void updateScalaBar() {
		Point2D scalaBarFirstPoint = Coordinate.viewportPoint2canvasPoint(new Point2D.Double(0,0), canvas.getTransform());
		Point2D scalaBarSecondPoint = Coordinate.viewportPoint2canvasPoint(new Point2D.Double(130,0), canvas.getTransform());
		Coordinate scalaBarFirstCoordinate = new Coordinate(scalaBarFirstPoint.getX(), Coordinate.canvasLat2Lat(scalaBarFirstPoint.getY()));
		Coordinate scalaBarSecondCoordinate = new Coordinate(scalaBarSecondPoint.getX(), Coordinate.canvasLat2Lat(scalaBarSecondPoint.getY()));
		scalaBarDistanceText.setText(String.valueOf(TextFormatter.formatDistance(Coordinate.haversineDistance(scalaBarFirstCoordinate, scalaBarSecondCoordinate, 6378.137),2)));
	}
}
