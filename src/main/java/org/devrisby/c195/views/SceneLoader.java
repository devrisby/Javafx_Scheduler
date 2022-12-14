package org.devrisby.c195.views;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoader {

    public static void changeStage(Scenes scene, ActionEvent actionEvent) {
        Stage currentStage = (Stage) ( (Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = loadStageRoot(scene);
        Stage newStage = initStage(currentStage, scene.getSceneName(), root);
        newStage.show();
    }

    public static void showStage(Stage stage, Scenes scene) {
        Parent root = loadStageRoot(scene);
        Stage newStage = initStage(stage, scene.getSceneName(), root);
        newStage.show();
    }

    private static Parent loadStageRoot(Scenes scene) {
        try {
            return FXMLLoader.load(scene.getFXMLFileURL());
        } catch (IOException err) {
            System.out.println("Error loading FXML file!\n");
            err.printStackTrace();
            Platform.exit();
            return null;
        }
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