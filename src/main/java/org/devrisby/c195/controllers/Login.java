package org.devrisby.c195.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.devrisby.c195.data.DB;
import org.devrisby.c195.data.UserRepository;
import org.devrisby.c195.models.User;
import org.devrisby.c195.services.LocationService;
import org.devrisby.c195.services.UserService;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.devrisby.c195.views.SceneLoader.changeScene;

public class Login implements Initializable {

    private final UserService userService;

    public Login() {
        this.userService = new UserService(new UserRepository(DB.getConnection()));
    }

    @FXML
    Label loginTitleLabel;
    @FXML
    Button cancelButton;

    @FXML
    Button loginButton;

    @FXML
    Label usernameLabel;

    @FXML
    Label passwordLabel;

    @FXML
    TextField usernameTextField;

    @FXML
    PasswordField passwordPasswordField;

    @FXML
    Label errorLabel;

    @FXML
    Label locationLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.loginTitleLabel.setText(LocationService.getResourceBundle().getString("loginTitleLabel"));
        this.usernameLabel.setText(LocationService.getResourceBundle().getString("usernameLabel"));
        this.usernameTextField.setPromptText(LocationService.getResourceBundle().getString("usernameTextFieldPrompt"));
        this.passwordLabel.setText(LocationService.getResourceBundle().getString("passwordLabel"));
        this.passwordPasswordField.setPromptText(LocationService.getResourceBundle().getString("passwordTextFieldPrompt"));
        this.cancelButton.setText(LocationService.getResourceBundle().getString("cancelButton"));
        this.cancelButton.setOnAction(actionEvent -> Platform.exit());
        this.loginButton.setText(LocationService.getResourceBundle().getString("loginButton"));
        this.loginButton.setOnAction(this::onLoginAction);
        this.errorLabel.setText("");
        this.locationLabel.setText(LocationService.getCurrentCountry());
    }

    private void onLoginAction(ActionEvent actionEvent) {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();
        Optional<User> user = userService.loginUser(username, password);

        if(username.isBlank() || password.isBlank() || user.isEmpty()) {
            this.errorLabel.setText(LocationService.getResourceBundle().getString("errorLabelFail"));
        } else {
            this.errorLabel.setTextFill(Color.GREEN);
            this.errorLabel.setText(LocationService.getResourceBundle().getString("errorLabelSuccess"));
            Home home = new Home(user.get());
            new Timeline(new KeyFrame(Duration.seconds(1.5), keyframeActionEvent -> changeScene(Scenes.HOME, actionEvent, home))).play();
        }
    }
}
