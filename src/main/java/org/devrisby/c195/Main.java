package org.devrisby.c195;

import javafx.application.Application;
import javafx.stage.Stage;
import org.devrisby.c195.data.AppDataSource;
import org.devrisby.c195.modules.user.User;
import org.devrisby.c195.views.Scenes;

import static org.devrisby.c195.views.SceneLoader.showScene;

public class Main extends Application {
    public static User SIGNED_IN_USER = null;
    public static void main(String[] args) {
        AppDataSource.startConnection();
        launch(args);
    }

    public void start(Stage primaryStage) {
        showScene(primaryStage, Scenes.LOGIN);
    }
}