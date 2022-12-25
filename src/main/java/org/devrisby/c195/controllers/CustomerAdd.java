package org.devrisby.c195.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.devrisby.c195.data.CustomerRepository;
import org.devrisby.c195.data.DB;
import org.devrisby.c195.data.FirstLvlDivisionRepository;
import org.devrisby.c195.models.Customer;
import org.devrisby.c195.models.FirstLvlDivision;
import org.devrisby.c195.views.SceneLoader;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CustomerAdd implements Initializable {

    private CustomerRepository customerRepository;
    private FirstLvlDivisionRepository firstLvlDivisionRepository;

    public CustomerAdd(){
        this.customerRepository = new CustomerRepository(DB.getConnection());
        this.firstLvlDivisionRepository = new FirstLvlDivisionRepository(DB.getConnection());
    }

    @FXML
    TextField customerNameTextField;

    @FXML
    TextField addressTextField;

    @FXML
    TextField postalCodeTextField;

    @FXML
    TextField phoneNumberTextField;

    @FXML
    ComboBox<String> firstLvlDivisionCountryComboBox;

    @FXML
    Button addButton;

    @FXML
    Button cancelButton;

    @FXML
    Label errorLabel;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.errorLabel.setText("");
        this.addButton.setOnAction(this::addCustomerAction);
        this.cancelButton.setOnAction(actionEvent -> SceneLoader.changeScene(Scenes.CUSTOMERS, actionEvent));
        initComboBox();
    }

    private void initComboBox(){
        List<FirstLvlDivision> divisions = this.firstLvlDivisionRepository.findAll();
        divisions.forEach(d -> this.firstLvlDivisionCountryComboBox.getItems().add(d.getDivisionName() + "," + d.getCountry().getCountryName()));
    }

    private void addCustomerAction(ActionEvent event) {
        String customerName = this.customerNameTextField.getText();
        String address = this.addressTextField.getText();
        String postalCode = this.postalCodeTextField.getText();
        String phoneNumber = this.phoneNumberTextField.getText();
        String divisionCountry = firstLvlDivisionCountryComboBox.getValue();

        try {
            validateFormValues(List.of(
                    new String[]{"Customer Name", customerName},
                    new String[]{"Address", address},
                    new String[]{"Postal Code", postalCode},
                    new String[]{"Phone Number", phoneNumber},
                    new String[]{"Division, Country", divisionCountry})
            );

            String division = divisionCountry.split(",")[0];

            FirstLvlDivision firstLvlDivision = this.firstLvlDivisionRepository.findByField(division);
            Customer customer = new Customer(customerName, address, postalCode, phoneNumber, firstLvlDivision);
            Customer savedCustomer = this.customerRepository.save(customer);

            if(savedCustomer.getCustomerID() != -1) {
                this.errorLabel.setText("New Customer added!");
                resetTextFields(List.of(this.customerNameTextField, this.addressTextField, this.phoneNumberTextField, this.postalCodeTextField));
            }

        } catch (IllegalArgumentException e) {
            this.errorLabel.setText("Please fill out all fields! Missing: " + e.getMessage());
        }
    }

    private void validateFormValues(List<String[]> values) {
        values.forEach(v -> {
            if(v[1] == null || v[1].isBlank() || v[1].isEmpty()) throw new IllegalArgumentException(v[0]);
        });
    }

    private void resetTextFields(List<TextField> fields) {
        fields.forEach(f -> f.setText(""));
    }
}
