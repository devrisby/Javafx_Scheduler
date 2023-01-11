package org.devrisby.c195.modules.appointment.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.devrisby.c195.controllers.FxUtils;
import org.devrisby.c195.modules.appointment.Appointment;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;

/** FXML controller for edit appointment form screen */
public class AppointmentFormEditController extends AppointmentFormBase {

    private final Appointment appointment;

    @FXML
    Button editButton;

    /** Constructor for AppointmentFormEditController. Initializes Appointment instance for editing */
    public AppointmentFormEditController(Appointment appointment) {
        super();
        this.appointment = appointment;
    }

    /** Initializes AppointmentFormEdit Controller. Populate inputs with data from Appointment instance  */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        this.populateInputControls();
    }

    private void populateInputControls() {
        super.appointmentIdTextField.setText(Integer.toString(appointment.getAppointmentID()));
        super.titleTextField.setText(appointment.getTitle());
        super.descriptionTextField.setText(appointment.getDescription());
        super.locationTextField.setText(appointment.getLocation());
        super.typeTextField.setText(appointment.getType());
        super.startDatePicker.setValue(LocalDate.ofInstant(appointment.getStart(), ZoneId.systemDefault()));
        super.startTimeTextField.setText(LocalTime.ofInstant(appointment.getStart(), ZoneId.systemDefault()).toString());
        super.endDatePicker.setValue(LocalDate.ofInstant(appointment.getEnd(), ZoneId.systemDefault()));
        super.endTimeTextField.setText(LocalTime.ofInstant(appointment.getEnd(), ZoneId.systemDefault()).toString());
        super.customerComboBox.getSelectionModel().select(appointment.getCustomer());
        super.contactComboBox.getSelectionModel().select(appointment.getContact());
        super.userComboBox.getSelectionModel().select(appointment.getUser());
    }

    @Override
    protected void formButtonAction() {
        this.editButton.setOnAction(event -> {
            FxUtils.setStatusMessage(errorLabel, "", false);

            try {
                super.validateTextFields();
                super.validateNonTextFields();
                super.validateTimes();

                super.appointmentService.update(super.createAppointmentFromInputs(this.appointment.getAppointmentID()));
                FxUtils.setStatusMessage(errorLabel, "New Appointment added!", false);

            } catch (IllegalArgumentException | DateTimeParseException e ) {
                FxUtils.setStatusMessage(errorLabel, e.getMessage(), true);
            }
        });
    }
}
