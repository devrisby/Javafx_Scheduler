package org.devrisby.c195.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.devrisby.c195.data.CountryRepository;
import org.devrisby.c195.data.CustomerRepository;
import org.devrisby.c195.data.DB;
import org.devrisby.c195.data.FirstLvlDivisionRepository;
import org.devrisby.c195.models.Country;
import org.devrisby.c195.models.Customer;
import org.devrisby.c195.models.FirstLvlDivision;
import org.devrisby.c195.views.SceneLoader;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerAddController implements Initializable {

    // Todo: display customer id and make it uneditable

    private CustomerRepository customerRepository;
    private FirstLvlDivisionRepository firstLvlDivisionRepository;
    private CountryRepository countryRepository;

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
    Button backButton;

    @FXML
    Label errorLabel;

    public CustomerAddController(){
        this.customerRepository = new CustomerRepository(DB.getConnection());
        this.firstLvlDivisionRepository = new FirstLvlDivisionRepository(DB.getConnection());
        this.countryRepository = new CountryRepository(DB.getConnection());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.errorLabel.setText("");
        this.addButton.setOnAction(this::addCustomerAction);
        this.backButton.setOnAction(actionEvent -> SceneLoader.changeScene(Scenes.CUSTOMERS, actionEvent));
        initComboBox();
    }

    private void initComboBox(){
        // Todo: Change so selecting country will bring up the appropriate first level divisions
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

            validateAddressFormValue(address);

            String division = divisionCountry.split(",")[0];

            FirstLvlDivision firstLvlDivision = this.firstLvlDivisionRepository.findByField(division);
            Customer customer = new Customer(customerName, address, postalCode, phoneNumber, firstLvlDivision);
            Customer savedCustomer = this.customerRepository.save(customer);

            if(savedCustomer.getCustomerID() != -1) {
                this.errorLabel.setText("New Customer added!");
                resetTextFields(List.of(this.customerNameTextField, this.addressTextField, this.phoneNumberTextField, this.postalCodeTextField));
            }

        } catch (IllegalArgumentException e) {
            this.errorLabel.setText(e.getMessage());
        }
    }

    private void validateFormValues(List<String[]> values) {
        values.forEach(v -> {
            if(v[1] == null || v[1].isBlank() || v[1].isEmpty()){
                throw new IllegalArgumentException("Please fill out all fields! Missing: " + v[0]);
            }
        });
    }

    // Invalidate address if it includes first-level division or country data
    private void validateAddressFormValue(String addressFormValue) {
        List<FirstLvlDivision> firstLvlDivisions = this.firstLvlDivisionRepository.findAll();
        List<Country> countries = this.countryRepository.findAll();

        firstLvlDivisions.forEach(division -> {
            if(addressFormValue.contains(division.getDivisionName())){
                throw new IllegalArgumentException("Address cannot contain first level division or country data!");
            }
        });

        countries.forEach(country -> {
            if(addressFormValue.contains(country.getCountryName())){
                throw new IllegalArgumentException("Address cannot contain division or country data!");
            }
        });
    }

    private void resetTextFields(List<TextField> fields) {
        fields.forEach(f -> f.setText(""));
    }
}
