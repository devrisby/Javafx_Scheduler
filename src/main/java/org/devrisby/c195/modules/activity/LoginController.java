package org.devrisby.c195.modules.activity;

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
import javafx.util.Duration;
import org.devrisby.c195.Main;
import org.devrisby.c195.controllers.FxUtils;
import org.devrisby.c195.modules.user.User;
import org.devrisby.c195.utils.LanguageUtils;
import org.devrisby.c195.modules.report.ReportService;
import org.devrisby.c195.modules.user.UserService;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.devrisby.c195.views.SceneLoader.changeScene;

/** FXML Controller for Login Screen */
public class LoginController implements Initializable {

    private final UserService userService;
    private final LoginActivity loginActivity;

    public LoginController() {
        this.userService = new UserService();
        this.loginActivity = new LoginActivity();
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

    /** Initialize LoginController. Translates UI text and sets button actions */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateUIText();
        this.loginButton.setOnAction(this::onLoginAction);
        FxUtils.setStatusMessage(errorLabel, "", false);
        this.cancelButton.setOnAction(actionEvent -> Platform.exit());
        this.usernameTextField.setOnKeyPressed(this::onEnterPressAction);
        this.passwordPasswordField.setOnKeyPressed(this::onEnterPressAction);
    }

    private void populateUIText() {
        this.loginTitleLabel.setText(LanguageUtils.getResourceBundle().getString("loginTitleLabel"));
        this.usernameLabel.setText(LanguageUtils.getResourceBundle().getString("usernameLabel"));
        this.passwordLabel.setText(LanguageUtils.getResourceBundle().getString("passwordLabel"));
        this.locationLabel.setText(ZoneId.systemDefault().toString());
        this.loginButton.setText(LanguageUtils.getResourceBundle().getString("loginButton"));
        this.cancelButton.setText(LanguageUtils.getResourceBundle().getString("cancelButton"));
        this.passwordPasswordField.setPromptText(LanguageUtils.getResourceBundle().getString("passwordTextFieldPrompt"));
        this.usernameTextField.setPromptText(LanguageUtils.getResourceBundle().getString("usernameTextFieldPrompt"));
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
        ZonedDateTime timestamp = ZonedDateTime.now();

        loginActivity.setUserName(username.isBlank() ? "N/A": username);
        loginActivity.setTimeStamp(timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        if(username.isBlank() || password.isBlank() || user.isEmpty()) {
            failedLoginAttempt();
        } else {
            successfulLoginAttempt(user.get(), actionEvent);
        }
    }

    private void failedLoginAttempt() {
        Main.SIGNED_IN_USER = null;
        loginActivity.setLoginSuccess(false);
        ReportService.updateLoginActivity(loginActivity);
        FxUtils.setStatusMessage(errorLabel, LanguageUtils.getResourceBundle().getString("errorLabelFail"), true);
    }

    private void successfulLoginAttempt(User user, Event actionEvent) {
        Main.SIGNED_IN_USER = user;
        loginActivity.setLoginSuccess(true);
        ReportService.updateLoginActivity(loginActivity);
        FxUtils.setStatusMessage(errorLabel, LanguageUtils.getResourceBundle().getString("errorLabelSuccess"), false);
        new Timeline(new KeyFrame(Duration.seconds(1.5), keyframeActionEvent -> changeScene(Scenes.HOME, actionEvent))).play();
    }
}
