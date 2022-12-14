package org.devrisby.c195;

import javafx.application.Application;
import javafx.stage.Stage;
import org.devrisby.c195.views.Scenes;

import static org.devrisby.c195.views.SceneLoader.showStage;

public class Main extends Application {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        launch(args);
    }

    public void start(Stage primaryStage) {
        showStage(primaryStage, Scenes.LOGIN);
    }
}