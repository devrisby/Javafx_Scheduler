package org.devrisby.c195.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.devrisby.c195.models.Appointment;
import org.devrisby.c195.models.Contact;
import org.devrisby.c195.models.Customer;
import org.devrisby.c195.models.User;
import org.devrisby.c195.services.*;
import org.devrisby.c195.views.SceneLoader;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class AppointmentAddController implements Initializable {

    private final AppointmentService appointmentService;
    private final CustomerService customerService;
    private final ContactService contactService;
    private final UserService userService;

    @FXML
    Label errorLabel;
    @FXML
    Button addButton;
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

    public AppointmentAddController() {
        this.appointmentService = new AppointmentService();
        this.customerService = new CustomerService();
        this.contactService = new ContactService();
        this.userService = new UserService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initComboBox();
        setStatusMessage("", false);
        this.backButton.setOnAction(actionEvent -> SceneLoader.changeScene(Scenes.APPOINTMENTS, actionEvent));
        this.addButton.setOnAction(this::addAppointmentAction);
    }

    private void initComboBox() {
        // https://stackoverflow.com/a/38367739
        List<Contact> contacts = this.contactService.findAll();
        this.contactComboBox.setItems(FXCollections.observableArrayList(contacts));

        List<User> users = this.userService.findAll();
        this.userComboBox.setItems(FXCollections.observableArrayList(users));

        List<Customer> customers = this.customerService.findAll();
        this.customerComboBox.setItems(FXCollections.observableArrayList(customers));

        this.startAMPMComboBox.getItems().addAll(List.of("---", "AM", "PM"));
        this.endAMPMComboBox.getItems().addAll(List.of("---", "AM", "PM"));
    }

    private void addAppointmentAction(ActionEvent event) {
        this.errorLabel.setText("");

        try {
            validateTextFields();
            validateNonTextFields();
            validateTimes();

            Appointment savedAppointment = this.appointmentService.save(createAppointmentFromInputs());
            setStatusMessage("New Appointment added!", false);

        } catch (IllegalArgumentException | DateTimeParseException e ) {
            setStatusMessage(e.getMessage(), true);
        }
    }

    private void setStatusMessage(String message, boolean isError){
        this.errorLabel.setText(message);
        this.errorLabel.setTextFill(isError ? Color.RED : Color.GREEN);
    }

    private Appointment createAppointmentFromInputs() {
        return new Appointment(
                this.titleTextField.getText(),
                this.descriptionTextField.getText(),
                this.locationTextField.getText(),
                this.typeTextField.getText(),
                extractDateTime(this.startDatePicker, this.startAMPMComboBox, this.startTimeTextField),
                extractDateTime(this.endDatePicker, this.endAMPMComboBox, this.endTimeTextField),
                this.customerComboBox.getValue(),
                this.contactComboBox.getValue(),
                this.userComboBox.getValue()
        );
    }

    private void validateTextFields() {
        Map<String, String> textFieldValues = Map.ofEntries(
                new AbstractMap.SimpleEntry<>("Title", this.titleTextField.getText()),
                new AbstractMap.SimpleEntry<>("Description", this.descriptionTextField.getText()),
                new AbstractMap.SimpleEntry<>("Location", this.locationTextField.getText()),
                new AbstractMap.SimpleEntry<>("Type", this.typeTextField.getText()),
                new AbstractMap.SimpleEntry<>("Start Time", this.startTimeTextField.getText()),
                new AbstractMap.SimpleEntry<>("End Time", this.endTimeTextField.getText())
        );

        // Check for any missing values in the textfields
        textFieldValues.forEach((key, value) -> {
            if(value == null || value.isBlank() || value.isEmpty()){
                throw new IllegalArgumentException("Please fill out all fields! Missing: " + key);
            }
        });
    }

    private void validateNonTextFields() {
        Map<String, Optional<Object>> otherFieldValues = Map.ofEntries(
                new AbstractMap.SimpleEntry<>("Contact", Optional.ofNullable(this.contactComboBox.getValue())),
                new AbstractMap.SimpleEntry<>("Customer", Optional.ofNullable(this.customerComboBox.getValue())),
                new AbstractMap.SimpleEntry<>("User", Optional.ofNullable(this.userComboBox.getValue())),
                new AbstractMap.SimpleEntry<>("Start Date", Optional.ofNullable(this.startDatePicker.getValue())),
                new AbstractMap.SimpleEntry<>("End Date", Optional.ofNullable(this.endDatePicker.getValue()))
        );

        // Check for any missing values in the other input fields (combo boxes, datepickers, etc)
        otherFieldValues.forEach((key, value) -> {
            if(value.isEmpty()) {
                throw new IllegalArgumentException("Please fill out all fields! Missing: " + key);
            }
        });
    }

    private void validateTimes(){
        Instant start = extractDateTime(this.startDatePicker, this.startAMPMComboBox, this.startTimeTextField);
        Instant end =  extractDateTime(this.endDatePicker, this.endAMPMComboBox, this.endTimeTextField);

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

    private Instant extractDateTime(DatePicker datePicker, ComboBox<String> amPmComboBox, TextField timeTextField) {
        LocalDate date = datePicker.getValue();
        LocalTime time = null;

        if(amPmComboBox.getValue() == null || amPmComboBox.getValue().equals("---") ) {
            time = LocalTime.parse(timeTextField.getText(), DateTimeFormatter.ofPattern("H:mm"));
        } else {
            try {
                time = LocalTime.parse(
                        timeTextField.getText() + " " + amPmComboBox.getValue().toUpperCase(),
                        DateTimeFormatter.ofPattern("h:mm a")
                );

            } catch (DateTimeParseException e) {
                if(e.getMessage().contains("ClockHourOfAmPm"))
                    throw new IllegalArgumentException("Time must be in 12-hour format if using AM/PM!");
                else
                    throw e;
            }
        }

        LocalDateTime dateTime = LocalDateTime.of(date, time);

        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}
