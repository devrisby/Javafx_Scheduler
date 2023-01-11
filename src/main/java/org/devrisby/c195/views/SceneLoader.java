package org.devrisby.c195.views;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.devrisby.c195.utils.LanguageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Class for managing changing JavaFX Scenes and screens */
public class SceneLoader {

    /** Changes current scene to new scene.
     * @param scene - Associated with FXML file
     * @param actionEvent - used to provide current JavaFX stage instance */
    public static void changeScene(Scenes scene, Event actionEvent) {
        Stage currentStage = (Stage) ( (Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = loadStageRoot(scene);
        Stage newStage = initStage(currentStage, scene.getSceneName(), root);
        newStage.show();
    }

    /** Changes current scene to new scene.
     * Used to transfer some data from previous screen to the next screen upon loading
     * @param scene - Associated with FXML file
     * @param actionEvent - used to provide current JavaFX stage instance
     * @param controller  - FXML controller of the new scene */
    public static void changeScene(Scenes scene, Event actionEvent, Initializable controller) {
        Stage currentStage = (Stage) ( (Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = loadStageRoot(scene, controller);
        Stage newStage = initStage(currentStage, scene.getSceneName(), root);
        newStage.show();
    }

    /** Initializes screen with new Stage */
    public static void showScene(Stage stage, Scenes scene) {
        Parent root = loadStageRoot(scene);
        Stage newStage = initStage(stage, scene.getSceneName(), root);
        newStage.show();
    }

    private static Parent loadStageRoot(Scenes scene) {
        try {
            return FXMLLoader.load(scene.getFXMLFileURL(), LanguageUtils.getResourceBundle());
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
            FXMLLoader loader = new FXMLLoader(scene.getFXMLFileURL(), LanguageUtils.getResourceBundle());
            loader.setController(controller);
            parent = loader.load();

        } catch (IOException err) {
            System.out.println("Error loading FXML file!\n" + err.getMessage());
            err.printStackTrace();
            Platform.exit();
        }

        return parent;
    }

    private static Stage initStage(Stage stage, String windowTitle, Parent root) {
        Path resourceDirPath = Paths.get("..", "..", "..", "..");

        stage.setTitle(windowTitle);
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(SceneLoader.class.getResource(Paths.get(resourceDirPath.toString(), "logo.png").toString()).toString()));
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