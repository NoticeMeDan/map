package com.noticemedan.mappr;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.viewmodel.MainViewController;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class App extends Application {
	private final Injector injector = Guice.createInjector(new MapModule());
	private FXMLLoader loader;
	private AnchorPane mainView;
	private MainViewController mainViewController;

	public static void main(String[] args) {
		launch(args);
	}

    @Override
    public void start(Stage primaryStage) {
		System.out.println("Does start call?");
		notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));
		mainViewController.setStage(primaryStage);
		mainViewController.configureStage();
    }

    @Override
	public void init() throws Exception {
		notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_LOAD));
		System.out.println("Does init call?");
		loader = new FXMLLoader();
		loader.setLocation(App.class.getResource("/fxml/MainView.fxml"));
		loader.setControllerFactory(injector::getInstance);
		mainView = loader.load();
		mainViewController = loader.getController();
	}

    // Use Dependency Injection to inject DomainFacade into FX Controllers
    private class MapModule extends AbstractModule {
		@Override
		protected void configure() {
			bind(DomainFacade.class).in(Singleton.class);
		}
	}
}
