package org.devrisby.c195.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.devrisby.c195.data.DB;
import org.devrisby.c195.data.UserRepository;
import org.devrisby.c195.models.LoginActivity;
import org.devrisby.c195.models.User;
import org.devrisby.c195.services.LocationService;
import org.devrisby.c195.services.ReportServices;
import org.devrisby.c195.services.UserService;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.sql.Timestamp;
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

        this.usernameTextField.setOnKeyPressed(this::onEnterPressAction);
        this.passwordPasswordField.setOnKeyPressed(this::onEnterPressAction);
    }

    private void onEnterPressAction(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            onLoginAction(event);
        }
    }

    private void onLoginAction(Event actionEvent) {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();
        Optional<User> user = userService.loginUser(username, password);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        LoginActivity loginReport = new LoginActivity();

        loginReport.setUserName(username.isBlank() ? "N/A": username);
        loginReport.setTimeStamp(timestamp);

        if(username.isBlank() || password.isBlank() || user.isEmpty()) {
            this.errorLabel.setText(LocationService.getResourceBundle().getString("errorLabelFail"));
            loginReport.setLoginSuccess(false);
            ReportServices.updateLoginActivity(loginReport);
        } else {
            this.errorLabel.setTextFill(Color.GREEN);
            this.errorLabel.setText(LocationService.getResourceBundle().getString("errorLabelSuccess"));
            loginReport.setLoginSuccess(true);
            ReportServices.updateLoginActivity(loginReport);
            Home home = new Home(user.get());
            new Timeline(new KeyFrame(Duration.seconds(1.5), keyframeActionEvent -> changeScene(Scenes.HOME, actionEvent, home))).play();
        }
    }
}
