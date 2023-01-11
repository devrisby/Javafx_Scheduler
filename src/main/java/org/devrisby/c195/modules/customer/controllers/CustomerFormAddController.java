package org.devrisby.c195.modules.customer.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.devrisby.c195.controllers.FxUtils;

/** FXML controller for Customer form add screen */
public class CustomerFormAddController extends CustomerFormBase {

    @FXML
    Button addButton;

    protected void formButtonAction() {
        this.addButton.setOnAction(event -> {
            FxUtils.setStatusMessage(errorLabel, "", false);

            try {
                super.validateFormValues();
                super.validateAddressComboBoxes();
                super.validateAddressText();

                super.customerService.save(super.createCustomerFromInputs());

                FxUtils.setStatusMessage(errorLabel, "New Customer Added!", false);
                super.resetTextFields();
            } catch (IllegalArgumentException e) {
                FxUtils.setStatusMessage(errorLabel, e.getMessage(), true);
            }
        });
    }
}
