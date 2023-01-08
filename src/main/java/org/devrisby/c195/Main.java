package org.devrisby.c195;

import javafx.application.Application;
import javafx.stage.Stage;
import org.devrisby.c195.data.DB;
import org.devrisby.c195.models.User;
import org.devrisby.c195.views.Scenes;

import java.util.Locale;

import static org.devrisby.c195.views.SceneLoader.showScene;

public class Main extends Application {
    public static User SIGNED_IN_USER = null;
    public static void main(String[] args) {
        DB.startConnection();
        Locale.setDefault(new Locale("fr"));
//        System.out.println("DEBUG --- " + LocalTime.parse("15:00 PM", DateTimeFormatter.ofPattern("hh:mm a")).toString());;
        launch(args);
    }

    public void start(Stage primaryStage) {
        showScene(primaryStage, Scenes.LOGIN);
    }
}