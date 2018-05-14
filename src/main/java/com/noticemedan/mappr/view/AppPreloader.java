package com.noticemedan.mappr.view;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AppPreloader extends Preloader {
	private ProgressBar bar;
	private Stage stage;
	private Image backgroundImage;

	private Scene createPreloaderScene() {
		bar = new ProgressBar();
		backgroundImage = new Image("/media/loadingscreen.png");
		Background background = new Background(new BackgroundImage(backgroundImage,
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)));

		BorderPane p = new BorderPane(bar);
		p.setBackground(background);
		bar.setStyle("-fx-padding: 340px 0 0 -120px");
		Scene scene = new Scene(p, 800, 500);
		scene.setFill(Color.TRANSPARENT);
		return scene;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage = primaryStage;
		System.out.println(Thread.currentThread());

		this.stage.initStyle(StageStyle.TRANSPARENT);
		stage.setScene(createPreloaderScene());
		stage.show();
	}

	@Override
	public void handleProgressNotification(ProgressNotification pn) {
		bar.setProgress(pn.getProgress());
	}

	@Override
	public void handleStateChangeNotification(StateChangeNotification evt) {
		if (evt.getType() == StateChangeNotification.Type.BEFORE_INIT) {

		}

		if (evt.getType() == StateChangeNotification.Type.BEFORE_LOAD) {

		}

		if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
			stage.hide();
		}
	}
}
