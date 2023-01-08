package org.devrisby.c195.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.util.Duration;
import org.devrisby.c195.Main;
import org.devrisby.c195.models.Appointment;
import org.devrisby.c195.models.User;
import org.devrisby.c195.services.AppointmentService;
import org.devrisby.c195.services.TimeService;
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
    ListView<String> appointmentsListView;

    public HomeController() {
        this.user = Main.SIGNED_IN_USER;
        appointmentService = new AppointmentService();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleLabel.setText("Welcome, " + this.user.getUserName());
        initClocks();
        this.customersButton.setOnAction(actionEvent -> SceneLoader.changeScene(Scenes.CUSTOMERS, actionEvent));
        this.appointmentsButton.setOnAction(actionEvent -> SceneLoader.changeScene(Scenes.APPOINTMENTS, actionEvent));
        this.reportsButton.setOnAction(actionEvent -> SceneLoader.changeScene(Scenes.REPORTS, actionEvent));
        initAppointments();
    }

    private void initClocks(){
        // src: https://stackoverflow.com/questions/38566638/javafx-displaying-time-and-refresh-in-every-second
        final Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        event -> {
                            phoenixTimeLabel.setText(TimeService.getLocalTime(ZoneId.of("America/Phoenix")));
                            whitePlainsTimeLabel.setText(TimeService.getLocalTime(ZoneId.of("America/New_York")));
                            montrealTimeLabel.setText(TimeService.getLocalTime(ZoneId.of("America/Montreal")));
                            londonTimeLabel.setText(TimeService.getLocalTime(ZoneId.of("Europe/London")));
                        }
                )
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void initAppointments() {
        Function<Appointment, String> appointmentMapper = a -> {
            return "Appointment #" + a.getAppointmentID() +
                    " --- " +
                    LocalDateTime.ofInstant(a.getStart(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd @ hh:mm a"));
        };

        List<String> appointmentsInfo = this.appointmentService
                .appointmentsInMinutes(this.user, 15)
                .stream()
                .map(appointmentMapper)
                .collect(Collectors.toList());

        ObservableList<String> appointments = FXCollections.observableList(appointmentsInfo);

        // todo align items in listview, see post: https://stackoverflow.com/a/51574405

        if(appointments.isEmpty()) {
            this.appointmentsListView.setItems(FXCollections.observableList(List.of("There are no upcoming appointments!")));
        } else {
            this.appointmentsListView.setItems(appointments);
        }
    }
}
