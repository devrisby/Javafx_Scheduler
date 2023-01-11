package org.devrisby.c195.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.util.Duration;
import org.devrisby.c195.Main;
import org.devrisby.c195.modules.appointment.Appointment;
import org.devrisby.c195.modules.user.User;
import org.devrisby.c195.modules.appointment.AppointmentService;
import org.devrisby.c195.utils.TimeUtils;
import org.devrisby.c195.views.SceneLoader;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

/** FXML controller for the User Home Screen*/
public class HomeController implements Initializable {
    private final User user;
    private final AppointmentService appointmentService;

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
    @FXML
    Button appointmentsButton;
    @FXML
    Button reportsButton;
    @FXML
    Button logoutButton;

    @FXML
    ListView<String> appointmentsListView;

    /** Constructor for HomeController. */
    public HomeController() {
        this.user = Main.SIGNED_IN_USER;
        appointmentService = new AppointmentService();
    }

    /** FXML Initializer for HomeController.
     * LAMBDA FUNCTION: I used a lambda to set the button action (change screen) for the various buttons in this controller
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initClocks();
        initAppointments();
        this.logoutButton.setOnAction(this::logout);
        titleLabel.setText("Welcome, " + this.user.getUserName());
        this.reportsButton.setOnAction(actionEvent -> SceneLoader.changeScene(Scenes.REPORTS, actionEvent));
        this.customersButton.setOnAction(actionEvent -> SceneLoader.changeScene(Scenes.CUSTOMERS, actionEvent));
        this.appointmentsButton.setOnAction(actionEvent -> SceneLoader.changeScene(Scenes.APPOINTMENTS, actionEvent));
    }

    private void initClocks(){
        // src: https://stackoverflow.com/questions/38566638/javafx-displaying-time-and-refresh-in-every-second
        final Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.5),
                        event -> {
                            phoenixTimeLabel.setText(TimeUtils.getLocalTime(ZoneId.of("America/Phoenix")));
                            whitePlainsTimeLabel.setText(TimeUtils.getLocalTime(ZoneId.of("America/New_York")));
                            montrealTimeLabel.setText(TimeUtils.getLocalTime(ZoneId.of("America/Montreal")));
                            londonTimeLabel.setText(TimeUtils.getLocalTime(ZoneId.of("Europe/London")));
                        }
                )
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void initAppointments() {
        Function<Appointment, String> appointmentMapper = a -> "Appointment #" +
                a.getAppointmentID() +
                " --- " +
                LocalDateTime.ofInstant(a.getStart(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd @ hh:mm a"));

        List<String> appointmentsInfo = this.appointmentService
                .appointmentsInMinutes(this.user, 15)
                .stream()
                .map(appointmentMapper)
                .collect(Collectors.toList());

        ObservableList<String> appointments = FXCollections.observableList(appointmentsInfo);

        if(appointments.isEmpty()) {
            this.appointmentsListView.setItems(FXCollections.observableList(List.of("There are no upcoming appointments!")));
        } else {
            this.appointmentsListView.setItems(appointments);
        }
    }

    private void logout(ActionEvent event) {
        Main.SIGNED_IN_USER = null;
        SceneLoader.changeScene(Scenes.LOGIN, event);
    }
}
