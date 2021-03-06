package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.App;
import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.model.map.Boundaries;
import com.noticemedan.mappr.model.pathfinding.TravelType;
import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.view.util.TextFormatter;
import com.noticemedan.mappr.view.CanvasView;
import com.noticemedan.mappr.viewmodel.event.ClickDragHandler;
import com.noticemedan.mappr.viewmodel.event.KeyboardHandler;
import com.noticemedan.mappr.viewmodel.event.MouseHandler;
import com.noticemedan.mappr.viewmodel.poi.FavoritePoiPaneController;
import com.noticemedan.mappr.viewmodel.poi.PoiBoxViewController;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class MainViewController {
	@Getter
	private static CanvasView canvas;

	@FXML
	AnchorPane mainView;
	@Setter
	Stage stage;
	private int width = 1100;
	private int height = 800;
	private boolean sidePaneOpen = false;
	private int sidePaneWidth = 0;

	//searchFieldImitator
	@FXML Button routeButton;
	@FXML Button favoriteButton;
	@FXML Pane searchFieldImitator;

	//scalaBar
	@FXML Text scalaBarDistanceText;

	//currentHoveredRoad
	@FXML Text currentHoveredRoadNamePane;

	// OSM
	@FXML Pane osmPaneContainer;
	private SwingNode swingNode;

	//Components
	@FXML Pane routePane;
	@FXML Pane favoritePoiPane;
	@FXML Pane poiBoxView;
	@FXML Pane searchPane;
	@FXML Pane mapPane;
	@FXML MenuBar menuBar;
	@FXML private AnchorPane loadingMessagePane;


	//Component controllers
	@FXML private PoiBoxViewController poiBoxViewController;
	@FXML private FavoritePoiPaneController favoritePoiPaneController;
	@FXML private SearchPaneController searchPaneController;
	@FXML private RoutePaneController routePaneController;
	@Getter
	@FXML private MapPaneController mapPaneController;
	@FXML private MenuBarController menuBarController;

	//Canvas controllers
	@Getter
	private CanvasController canvasController;
	private MouseHandler mouseHandler;

	// Model Facade
	private DomainFacade domain;
	private Boundaries boundaries;

	@Inject
	public MainViewController(DomainFacade domainFacade) {
		this.domain = domainFacade;
		this.canvasController = new CanvasController(this.domain);
	}

	public void initialize() {
		insertOSMPane();
		favoritePoiPaneController.setMainViewController(this);
		routePaneController.setMainViewController(this);
		searchPaneController.setMainViewController(this);
		menuBarController.setMainViewController(this);
		mapPaneController.setMainViewController(this);
		poiBoxViewController.setMainViewController(this);

		eventListeners();
	}

	public void configureStage() {
		Image icon = new Image(App.class.getResourceAsStream("/media/icon.png"));
		stage.setTitle("Mappr");
		stage.getIcons().add(icon);
		stage.setMinWidth(800);
		stage.setMinHeight(700);
		stage.setWidth(width);
		stage.setHeight(height);
		stage.setScene(new Scene(mainView));
		stageListeners();
		stage.show();
	}

	private void stageListeners() { // This enables canvas resizing
		ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
			width = (int) stage.getWidth() - sidePaneWidth;
			height = (int) stage.getHeight();
			resizeNode(width, height);
		};
		stage.widthProperty().addListener(stageSizeListener);
		stage.heightProperty().addListener(stageSizeListener);
	}

	private void resizeNode(int width, int height) {
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		swingNode.setContent(canvas);
	}

	private void insertOSMPane() {
		swingNode = new SwingNode();
		canvas = this.canvasController.getCanvasView();
		this.centerViewport();
		canvas.setPreferredSize(new Dimension(width, height));
		swingNode.setContent(canvas);
		osmPaneContainer.getChildren().addAll(swingNode);
	}

	void centerViewport() {
		this.boundaries = this.domain.getBoundaries();
		canvas.setTransform(new AffineTransform());
		canvas.pan(-boundaries.getMinLon(), -boundaries.getMaxLat());
		canvas.zoom(width / (boundaries.getMaxLon() - boundaries.getMinLon()), 0, 0);
		canvas.setZoomLevel(1 / (boundaries.getMaxLon() - boundaries.getMinLon()));
	}

	private void eventListeners() {
		this.mouseHandler = new MouseHandler(this);
		mainView.addEventHandler(KeyEvent.KEY_PRESSED, new KeyboardHandler(this));

		searchFieldImitator.setOnMouseClicked(event -> {
			searchPaneController.openSearchPane();
			pushCanvas();
		});

		favoriteButton.setOnAction(event -> {
			favoritePoiPaneController.openFavoritePane();
			pushCanvas();
		});

		routeButton.setOnAction(event -> {
			routePaneController.openRoutePane();
			pushCanvas();
		});

		swingNode.addEventHandler(MouseEvent.ANY, new ClickDragHandler(
				event -> poiBoxViewController.closePoiBox(),
				event -> poiBoxViewController.openPoiBox(
						new Coordinate(
								mouseHandler.getLastMousePositionCanvasCoords().getX(),
								Coordinate.canvasLatToLat(mouseHandler.getLastMousePositionCanvasCoords().getY())
						)
				)
		));
	}

	public void updateScalaBar() {
		Point2D scalaBarFirstPoint = Coordinate.viewportPointToCanvasPoint(new Point2D.Float(0,0), canvas.getTransform());
		Point2D scalaBarSecondPoint = Coordinate.viewportPointToCanvasPoint(new Point2D.Float(130,0), canvas.getTransform());
		Coordinate scalaBarFirstCoordinate = new Coordinate(scalaBarFirstPoint.getX(), Coordinate.canvasLatToLat(scalaBarFirstPoint.getY()));
		Coordinate scalaBarSecondCoordinate = new Coordinate(scalaBarSecondPoint.getX(), Coordinate.canvasLatToLat(scalaBarSecondPoint.getY()));
		scalaBarDistanceText.setText(String.valueOf(TextFormatter.formatDistance(Coordinate.haversineDistance(scalaBarFirstCoordinate, scalaBarSecondCoordinate, 6378137),2)));
	}

	public void updateCurrentHoveredRoad(Coordinate queryPoint) {
		String roadName = domain.doNearestNeighborInCurrentRangeSearch(queryPoint, TravelType.ALL).getName();
		if(roadName == null) roadName = "Ukendt vejnavn";
		currentHoveredRoadNamePane.setText(roadName);
	}

	public void pushCanvas() {
		if(!sidePaneOpen) {
			AnchorPane.setLeftAnchor(osmPaneContainer, 250d);
			AnchorPane.setLeftAnchor(loadingMessagePane, 290d);
			sidePaneWidth = 250;
			sidePaneOpen = !sidePaneOpen;
			// Annoying hack to make canvas actually resize. Tried to call nodeResize directly here
			// but for some reason it does not work.
			stage.setWidth(stage.getWidth()+1);
			stage.setWidth(stage.getWidth()-1);
		} else {
			AnchorPane.setLeftAnchor(osmPaneContainer,0d);
			AnchorPane.setLeftAnchor(loadingMessagePane,15d);
			sidePaneWidth = 0;
			sidePaneOpen = !sidePaneOpen;
			stage.setWidth(stage.getWidth()+1);
			stage.setWidth(stage.getWidth()-1);
		}
	}

	void toggleLoadingMessage() {
		if (this.loadingMessagePane.isVisible()) {
			this.loadingMessagePane.setVisible(false);
		} else {
			this.loadingMessagePane.setVisible(true);
		}
	}
}
