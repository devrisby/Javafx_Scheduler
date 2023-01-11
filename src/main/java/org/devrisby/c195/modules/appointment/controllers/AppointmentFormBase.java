package org.devrisby.c195.modules.appointment.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.devrisby.c195.controllers.FxUtils;
import org.devrisby.c195.modules.appointment.Appointment;
import org.devrisby.c195.modules.appointment.AppointmentService;
import org.devrisby.c195.modules.contact.Contact;
import org.devrisby.c195.modules.contact.ContactService;
import org.devrisby.c195.modules.customer.Customer;
import org.devrisby.c195.modules.customer.CustomerService;
import org.devrisby.c195.modules.user.User;
import org.devrisby.c195.modules.user.UserService;
import org.devrisby.c195.utils.TimeUtils;
import org.devrisby.c195.views.SceneLoader;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/** Abstract FXML Controller class for add and update appointment screens */
public abstract class AppointmentFormBase implements Initializable {

    @FXML
    Label errorLabel;
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

    protected final AppointmentService appointmentService;
    protected final CustomerService customerService;
    protected final ContactService contactService;
    protected final UserService userService;
    protected final TimeUtils timeUtils;

    /** Constructor for AppointmentFormBase. Initializes Appointment, Customer, Contact, User service classes  */
    public AppointmentFormBase() {
        this.appointmentService = new AppointmentService();
        this.customerService = new CustomerService();
        this.contactService = new ContactService();
        this.userService = new UserService();
        this.timeUtils = new TimeUtils();
    }

    /** Initializes Appointment Form Controllers */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initComboBox();
        formButtonAction();
        FxUtils.setStatusMessage(errorLabel, "", false);
        this.backButton.setOnAction(actionEvent -> SceneLoader.changeScene(Scenes.APPOINTMENTS, actionEvent));
    }

    protected abstract void formButtonAction();

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

    protected Appointment createAppointmentFromInputs() {
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

    protected Appointment createAppointmentFromInputs(int id) {
        return new Appointment(
                id,
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

    protected void validateTextFields() {
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

    protected void validateNonTextFields() {
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

    protected void validateTimes(){
        Instant start = extractDateTime(this.startDatePicker, this.startAMPMComboBox, this.startTimeTextField);
        Instant end =  extractDateTime(this.endDatePicker, this.endAMPMComboBox, this.endTimeTextField);

        timeUtils.validateStartEndTimes(start, end);
        timeUtils.validateForBusinessHours(start, end);
    }

    protected Instant extractDateTime(DatePicker datePicker, ComboBox<String> amPmComboBox, TextField timeTextField) {
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
