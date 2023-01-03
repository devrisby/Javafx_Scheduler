package org.devrisby.c195.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.devrisby.c195.models.User;
import org.devrisby.c195.services.LocationService;
import org.devrisby.c195.views.SceneLoader;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    private final User user;

    @FXML
    Label titleLabel;

    @FXML
    Label phoenixTimeLabel;
    @FXML
    Label whitePlainsTimeLabel;
    @FXML
    Label montrealTimeLabel;
    @FXML
    Label londonTimeLabel;

    @FXML
    Button customersButton;

    public HomeController(User user) {
        this.user = user;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleLabel.setText("Welcome, " + this.user.getUserName());
        initClocks();
        this.customersButton.setOnAction(actionEvent -> SceneLoader.changeScene(Scenes.CUSTOMERS, actionEvent));
    }

    private void initClocks(){
        // src: https://stackoverflow.com/questions/38566638/javafx-displaying-time-and-refresh-in-every-second
        final Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        event -> {
                            phoenixTimeLabel.setText(LocationService.getLocalTime(ZoneId.of("America/Phoenix")));
                            whitePlainsTimeLabel.setText(LocationService.getLocalTime(ZoneId.of("America/New_York")));
                            montrealTimeLabel.setText(LocationService.getLocalTime(ZoneId.of("America/Montreal")));
                            londonTimeLabel.setText(LocationService.getLocalTime(ZoneId.of("Europe/London")));
                        }
                )
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
