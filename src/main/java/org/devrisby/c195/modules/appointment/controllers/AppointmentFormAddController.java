package org.devrisby.c195.modules.appointment.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.devrisby.c195.controllers.FxUtils;

import java.time.format.DateTimeParseException;

/** FXML controller class for adding appointments form screen */
public class AppointmentFormAddController extends AppointmentFormBase {

    @FXML
    Button addButton;

    /** Saves user created appointment to database.
     * Extracts all input values, validates inputs, creates new appointment object, and saves it do database */
    @Override
    protected void formButtonAction() {
        this.addButton.setOnAction(event -> {
            FxUtils.setStatusMessage(errorLabel, "", false);

            try {
                super.validateTextFields();
                super.validateNonTextFields();
                super.validateTimes();

                super.appointmentService.save(super.createAppointmentFromInputs());
                FxUtils.setStatusMessage(errorLabel,"New Appointment added!", false);

            } catch (IllegalArgumentException | DateTimeParseException e ) {
                FxUtils.setStatusMessage(errorLabel, e.getMessage(), true);
            }
        });
    }
}
