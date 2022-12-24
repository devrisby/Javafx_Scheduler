package org.devrisby.c195.views;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.devrisby.c195.services.LocationService;

import java.io.IOException;
import java.util.Objects;

public class SceneLoader {

    public static void changeScene(Scenes scene, ActionEvent actionEvent) {
        Stage currentStage = (Stage) ( (Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = loadStageRoot(scene);
        Stage newStage = initStage(currentStage, scene.getSceneName(), root);
        newStage.show();
    }

    public static void changeScene(Scenes scene, ActionEvent actionEvent, Initializable controller) {
        Stage currentStage = (Stage) ( (Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = loadStageRoot(scene, controller);
        Stage newStage = initStage(currentStage, scene.getSceneName(), root);
        newStage.show();
    }

    public static void showScene(Stage stage, Scenes scene) {
        Parent root = loadStageRoot(scene);
        Stage newStage = initStage(stage, scene.getSceneName(), root);
        newStage.show();
    }

    private static Parent loadStageRoot(Scenes scene) {
        try {
            System.out.println("DEBUG --- Home filepath: " + scene.getFXMLFileURL());
            return FXMLLoader.load(scene.getFXMLFileURL(), LocationService.getResourceBundle());
        } catch (IOException err) {
            System.out.println("Error loading FXML file!\n" + err.getMessage());
            err.printStackTrace();
            Platform.exit();
            return null;
        }
    }

    private static Parent loadStageRoot(Scenes scene, Initializable controller) {
        Parent parent = null;

        try {
            System.out.println("DEBUG --- Home filepath: " + scene.getFXMLFileURL());
            FXMLLoader loader = new FXMLLoader(scene.getFXMLFileURL(), LocationService.getResourceBundle());
            loader.setController(controller);
            parent = loader.load();

            System.out.println("DEBUG --- parent is null: " + Objects.isNull(parent));
        } catch (IOException err) {
            System.out.println("Error loading FXML file!\n" + err.getMessage());
            err.printStackTrace();
            Platform.exit();
        }

        return parent;
    }

    private static Stage initStage(Stage stage, String windowTitle, Parent root) {
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(root));
        return stage;
    }
}


/*
This helper class is based on the javafx starter code in Main.java:

        public void start(Stage primaryStage) throws Exception {
                Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
                primaryStage.setTitle("hello world");
                primaryStage.setScene(new Scene(root, 300, 275));
                primaryStage.show();
        }
 */