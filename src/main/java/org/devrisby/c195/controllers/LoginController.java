package org.devrisby.c195.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import org.devrisby.c195.Main;
import org.devrisby.c195.data.DB;
import org.devrisby.c195.data.UserRepository;
import org.devrisby.c195.models.LoginActivity;
import org.devrisby.c195.models.User;
import org.devrisby.c195.services.TimeService;
import org.devrisby.c195.services.ReportServices;
import org.devrisby.c195.services.UserService;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.devrisby.c195.views.SceneLoader.changeScene;

public class LoginController implements Initializable {

    private final UserService userService;

    public LoginController() {
        this.userService = new UserService();
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
        this.loginTitleLabel.setText(TimeService.getResourceBundle().getString("loginTitleLabel"));
        this.usernameLabel.setText(TimeService.getResourceBundle().getString("usernameLabel"));
        this.usernameTextField.setPromptText(TimeService.getResourceBundle().getString("usernameTextFieldPrompt"));
        this.passwordLabel.setText(TimeService.getResourceBundle().getString("passwordLabel"));
        this.passwordPasswordField.setPromptText(TimeService.getResourceBundle().getString("passwordTextFieldPrompt"));
        this.cancelButton.setText(TimeService.getResourceBundle().getString("cancelButton"));
        this.cancelButton.setOnAction(actionEvent -> Platform.exit());
        this.loginButton.setText(TimeService.getResourceBundle().getString("loginButton"));
        this.loginButton.setOnAction(this::onLoginAction);
        this.errorLabel.setText("");
        this.locationLabel.setText(ZoneId.systemDefault().toString());

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
            this.errorLabel.setText(TimeService.getResourceBundle().getString("errorLabelFail"));
            loginReport.setLoginSuccess(false);
            ReportServices.updateLoginActivity(loginReport);
            Main.SIGNED_IN_USER = null;
        } else {
            Main.SIGNED_IN_USER = user.get();
            this.errorLabel.setTextFill(Color.GREEN);
            this.errorLabel.setText(TimeService.getResourceBundle().getString("errorLabelSuccess"));
            loginReport.setLoginSuccess(true);
            ReportServices.updateLoginActivity(loginReport);
            new Timeline(new KeyFrame(Duration.seconds(1.5), keyframeActionEvent -> changeScene(Scenes.HOME, actionEvent))).play();
        }
    }
}
