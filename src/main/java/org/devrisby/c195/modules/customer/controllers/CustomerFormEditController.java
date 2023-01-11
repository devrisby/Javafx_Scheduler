package org.devrisby.c195.modules.customer.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.devrisby.c195.controllers.FxUtils;
import org.devrisby.c195.modules.customer.Customer;

import java.net.URL;
import java.util.ResourceBundle;

/** FXML controller class for Customer edit form */
public class CustomerFormEditController extends CustomerFormBase {
    private final Customer customer;

    @FXML
    Button editButton;

    public CustomerFormEditController(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        populateInputControls();
    }

    private void populateInputControls(){
        super.customerNameTextField.setText(this.customer.getCustomerName());
        super.addressTextField.setText(this.customer.getAddress());
        super.postalCodeTextField.setText(this.customer.getPostalCode());
        super.phoneNumberTextField.setText(this.customer.getPhone());
        super.countryComboBox.getSelectionModel().select(this.customer.getDivision().getCountry());
        super.firstLvlDivisionComboBox.getSelectionModel().select(this.customer.getDivision());
    }

    protected void formButtonAction() {
        this.editButton.setOnAction(event -> {
            FxUtils.setStatusMessage(errorLabel, "", false);

            try {
                super.validateFormValues();
                super.validateAddressComboBoxes();
                super.validateAddressText();

                super.customerService.save(super.createCustomerFromInputs(this.customer.getCustomerID()));

                FxUtils.setStatusMessage(errorLabel, "Customer updated!", false);
                super.resetTextFields();
            } catch (IllegalArgumentException e) {
                FxUtils.setStatusMessage(errorLabel, e.getMessage(), true);
            }
        });
    }
}
