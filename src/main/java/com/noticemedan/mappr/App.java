package com.noticemedan.mappr;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.noticemedan.mappr.model.DomainFacade;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
	private final Injector injector = Guice.createInjector(new MapModule());
	public static void main(String[] args) {
		launch(args);
	}

    @Override
    public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
		loader.setControllerFactory(injector::getInstance);
		Parent root = loader.load();
		Image icon = new Image(App.class.getResourceAsStream("/media/icon.png"));

		primaryStage.setTitle("Mappr");
		primaryStage.getIcons().add(icon);
		primaryStage.setWidth(1100);
		primaryStage.setHeight(650);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
    }

    // Use Dependency Injection to inject DomainFacade into FX Controllers
    private class MapModule extends AbstractModule {
		@Override
		protected void configure() {
			bind(DomainFacade.class).in(Singleton.class);
		}
	}
}
