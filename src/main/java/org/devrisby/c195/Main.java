package org.devrisby.c195;

import javafx.application.Application;
import javafx.stage.Stage;
import org.devrisby.c195.data.DB;
import org.devrisby.c195.views.Scenes;

import static org.devrisby.c195.views.SceneLoader.showScene;

public class Main extends Application {
    public static void main(String[] args) {
        DB.startConnection();
        System.out.println("Hello world!");
        launch(args);
    }

    public void start(Stage primaryStage) {
        showScene(primaryStage, Scenes.LOGIN);
    }
}