package org.devrisby.c195.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.devrisby.c195.data.*;
import org.devrisby.c195.models.Appointment;
import org.devrisby.c195.models.Contact;
import org.devrisby.c195.models.Customer;
import org.devrisby.c195.models.User;
import org.devrisby.c195.services.TimeService;
import org.devrisby.c195.views.SceneLoader;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class AppointmentEditController implements Initializable {

    private Appointment appointment;

    private AppointmentRepository appointmentRepository;
    private CustomerRepository customerRepository;
    private ContactRepository contactRepository;
    private UserRepository userRepository;

    @FXML
    Label errorLabel;
    @FXML
    Button editButton;
    @FXML
    Button backButton;
    @FXML
    TextField appointmentIdTextField;
    @FXML
    TextField titleTextField;
    @FXML
    TextField descriptionTextField;
    @FXML
    TextField locationTextField;
    @FXML
    TextField typeTextField;
    @FXML
    ComboBox<Contact> contactComboBox;
    @FXML
    ComboBox<User> userComboBox;
    @FXML
    ComboBox<Customer> customerComboBox;
    @FXML
    DatePicker startDatePicker;
    @FXML
    TextField startTimeTextField;
    @FXML
    ComboBox<String> startAMPMComboBox;
    @FXML
    DatePicker endDatePicker;
    @FXML
    TextField endTimeTextField;
    @FXML
    ComboBox<String> endAMPMComboBox;

    public AppointmentEditController(Appointment appointment) {
        this.appointment = appointment;
        this.appointmentRepository = new AppointmentRepository(DB.getConnection());
        this.customerRepository = new CustomerRepository(DB.getConnection());
        this.contactRepository = new ContactRepository(DB.getConnection());
        this.userRepository = new UserRepository(DB.getConnection());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.errorLabel.setText("");
        this.editButton.setOnAction(this::editAppointmentAction);
        this.backButton.setOnAction(actionEvent -> SceneLoader.changeScene(Scenes.APPOINTMENTS, actionEvent));
        initComboBox();

        this.appointmentIdTextField.setText(Integer.toString(appointment.getAppointmentID()));
        this.titleTextField.setText(appointment.getTitle());
        this.descriptionTextField.setText(appointment.getDescription());
        this.locationTextField.setText(appointment.getLocation());
        this.typeTextField.setText(appointment.getType());
        this.startDatePicker.setValue(LocalDate.ofInstant(appointment.getStart(), ZoneId.systemDefault()));
        this.startTimeTextField.setText(LocalTime.ofInstant(appointment.getStart(), ZoneId.systemDefault()).toString());
        this.endDatePicker.setValue(LocalDate.ofInstant(appointment.getEnd(), ZoneId.systemDefault()));
        this.endTimeTextField.setText(LocalTime.ofInstant(appointment.getEnd(), ZoneId.systemDefault()).toString());
        this.customerComboBox.getSelectionModel().select(appointment.getCustomer());
        this.contactComboBox.getSelectionModel().select(appointment.getContact());
        this.userComboBox.getSelectionModel().select(appointment.getUser());
    }

    private void initComboBox() {
        // https://stackoverflow.com/a/38367739
        List<Contact> contacts = this.contactRepository.findAll();
        this.contactComboBox.setItems(FXCollections.observableArrayList(contacts));


        List<User> users = this.userRepository.findAll();
        this.userComboBox.setItems(FXCollections.observableArrayList(users));

        List<Customer> customers = this.customerRepository.findAll();
        this.customerComboBox.setItems(FXCollections.observableArrayList(customers));

        this.startAMPMComboBox.getItems().addAll(List.of("---", "AM", "PM"));
        this.endAMPMComboBox.getItems().addAll(List.of("---", "AM", "PM"));
    }

    private void editAppointmentAction(ActionEvent event) {
        try {
            this.errorLabel.setText("");
            validateInputFields();

            Instant startTime = extractStartDateTime();
            Instant endTime = extractEndDateTime();

            validateTimes(startTime, endTime);

            appointment.setTitle(this.titleTextField.getText());
            appointment.setDescription(this.descriptionTextField.getText());
            appointment.setLocation(this.locationTextField.getText());
            appointment.setType(this.typeTextField.getText());
            appointment.setStart(startTime);
            appointment.setEnd(endTime);
            appointment.setCustomer(this.customerComboBox.getValue());
            appointment.setContact(this.contactComboBox.getValue());
            appointment.setUser(this.userComboBox.getValue());


            checkForAppointmentConflicts(appointment);

            this.appointmentRepository.save(appointment);
            this.errorLabel.setTextFill(Color.GREEN);
            this.errorLabel.setText("New Appointment added!");

        } catch (IllegalArgumentException e ) {
            this.errorLabel.setText(e.getMessage());
        } catch (DateTimeParseException de ) {
            this.errorLabel.setText("Invalid time entered!");
        }
    }

    private void validateInputFields() {
        Map<String, String> textFieldValues = Map.ofEntries(
                new AbstractMap.SimpleEntry<>("Title", this.titleTextField.getText()),
                new AbstractMap.SimpleEntry<>("Description", this.descriptionTextField.getText()),
                new AbstractMap.SimpleEntry<>("Location", this.locationTextField.getText()),
                new AbstractMap.SimpleEntry<>("Type", this.typeTextField.getText()),
                new AbstractMap.SimpleEntry<>("Start Time", this.startTimeTextField.getText()),
                new AbstractMap.SimpleEntry<>("End Time", this.endTimeTextField.getText())
        );

        textFieldValues.forEach((key, value) -> {
            if(value == null || value.isBlank() || value.isEmpty()){
                throw new IllegalArgumentException("Please fill out all fields! Missing: " + key);
            }
        });

        Map<String, Optional<Object>> otherFieldValues = Map.ofEntries(
                new AbstractMap.SimpleEntry<>("Contact", Optional.ofNullable(this.contactComboBox.getValue())),
                new AbstractMap.SimpleEntry<>("Customer", Optional.ofNullable(this.customerComboBox.getValue())),
                new AbstractMap.SimpleEntry<>("User", Optional.ofNullable(this.userComboBox.getValue())),
                new AbstractMap.SimpleEntry<>("Start Date", Optional.ofNullable(this.startDatePicker.getValue())),
                new AbstractMap.SimpleEntry<>("End Date", Optional.ofNullable(this.endDatePicker.getValue()))
        );

        otherFieldValues.forEach((key, value) -> {
            if(value.isEmpty()) {
                throw new IllegalArgumentException("Please fill out all fields! Missing: " + key);
            }
        });
    }

    private void validateTimes(Instant start, Instant end){
        LocalDateTime startDateTime = LocalDateTime.ofInstant(start, ZoneId.systemDefault());
        LocalDateTime endDateTime = LocalDateTime.ofInstant(end, ZoneId.systemDefault());

        if(startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("Start time cannot occur after end time!");
        } else if (startDateTime.isEqual(endDateTime)) {
            throw new IllegalArgumentException("Start and end times cannot be the same!");
        }

        LocalTime startTime = startDateTime.atZone(ZoneId.systemDefault()).toLocalTime();
        LocalTime endTime = endDateTime.atZone(ZoneId.systemDefault()).toLocalTime();

        if(!TimeService.isWithinBusinessHours(startTime) || !TimeService.isWithinBusinessHours(endTime)) {
            throw new IllegalArgumentException("Times can't be outside of the business hours (8:00 AM - 10:00 PM EST");
        }
    }

    // TODO: Refactor into AppointmentService class
    private void checkForAppointmentConflicts(Appointment appointment) {
        List<Appointment> existingAppointments =
                this.appointmentRepository.findAllByCustomerId(appointment.getCustomer().getCustomerID());

        existingAppointments
                .stream()
                .filter(a -> a.getAppointmentID() != appointment.getAppointmentID())
                .forEach(a -> {
                    boolean isEqualTo = appointment.getStart().compareTo(a.getStart()) == 0 ||
                            appointment.getStart().compareTo(a.getEnd()) == 0 ||
                            appointment.getEnd().compareTo(a.getStart()) == 0 ||
                            appointment.getEnd().compareTo(a.getEnd()) == 0;

                    boolean isWithin = appointment.getStart().isAfter(a.getStart()) &&
                            appointment.getEnd().isBefore(a.getEnd());

                    if(isEqualTo || isWithin ) {
                        throw new IllegalArgumentException("Appointment times conflict with an existing appointment!");
                    }
                });
    }

    private Instant extractStartDateTime() {
        LocalDate startDate = this.startDatePicker.getValue();
        LocalTime startTime = null;

        if(this.startAMPMComboBox.getValue() == null || this.startAMPMComboBox.getValue().equals("---") ) {
            startTime = LocalTime.parse(this.startTimeTextField.getText(), DateTimeFormatter.ofPattern("H:mm"));
        } else {
            try {
                startTime = LocalTime.parse(this.startTimeTextField.getText() + " " + this.startAMPMComboBox.getValue().toUpperCase(), DateTimeFormatter.ofPattern("h:mm a"));
            } catch (DateTimeParseException e) {
                if(e.getMessage().contains("ClockHourOfAmPm"))
                    throw new IllegalArgumentException("Time must be in 12-hour format if using AM/PM!");
                else
                    throw e;
            }
        }

        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);

        return startDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    private Instant extractEndDateTime() {
        LocalDate endDate = this.endDatePicker.getValue();
        LocalTime endTime = null;

        if(this.endAMPMComboBox.getValue() == null || this.endAMPMComboBox.getValue().equals("---") ) {
            endTime = LocalTime.parse(this.endTimeTextField.getText(), DateTimeFormatter.ofPattern("H:mm"));
        } else {
            try {
                endTime = LocalTime.parse(this.endTimeTextField.getText() + " " + this.endAMPMComboBox.getValue().toUpperCase(), DateTimeFormatter.ofPattern("h:mm a"));
            } catch (DateTimeParseException e) {
                if(e.getMessage().contains("ClockHourOfAmPm"))
                    throw new IllegalArgumentException("Time must be in 12-hour format if using AM/PM!");
                else
                    throw e;
            }
        }

        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        return endDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}
