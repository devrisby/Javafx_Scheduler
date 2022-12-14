package org.devrisby.c195.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.util.ResourceBundle;

import static org.devrisby.c195.views.SceneLoader.changeStage;

public class Login implements Initializable {

    @FXML
    Button cancelButton;

    @FXML
    Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.cancelButton.setOnAction(actionEvent -> Platform.exit());
        this.loginButton.setOnAction(actionEvent -> changeStage(Scenes.HOME, actionEvent));
    }
}
