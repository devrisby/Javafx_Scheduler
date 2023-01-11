package org.devrisby.c195.modules.customer.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.devrisby.c195.controllers.FxUtils;
import org.devrisby.c195.modules.country.Country;
import org.devrisby.c195.modules.country.CountryService;
import org.devrisby.c195.modules.customer.Customer;
import org.devrisby.c195.modules.customer.CustomerService;
import org.devrisby.c195.modules.division.Division;
import org.devrisby.c195.modules.division.DivisionService;
import org.devrisby.c195.views.SceneLoader;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/** FXML Controller class for Customer Forms */
public abstract class CustomerFormBase implements Initializable {

    protected final CustomerService customerService;
    protected final DivisionService divisionService;
    protected final CountryService countryService;

    @FXML
    TextField customerNameTextField;
    @FXML
    TextField addressTextField;
    @FXML
    TextField postalCodeTextField;
    @FXML
    TextField phoneNumberTextField;
    @FXML
    ComboBox<Country> countryComboBox;
    @FXML
    ComboBox<Division> firstLvlDivisionComboBox;
    @FXML
    Label errorLabel;
    @FXML
    Button backButton;

    public CustomerFormBase() {
        this.customerService = new CustomerService();
        this.divisionService = new DivisionService();
        this.countryService = new CountryService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formButtonAction();
        initComboBox();
        FxUtils.setStatusMessage(errorLabel, "", false);
        this.backButton.setOnAction(actionEvent -> SceneLoader.changeScene(Scenes.CUSTOMERS, actionEvent));
    }

    protected abstract void formButtonAction();

    protected Customer createCustomerFromInputs() {
        return new Customer(
                this.customerNameTextField.getText(),
                this.addressTextField.getText(),
                this.postalCodeTextField.getText(),
                this.phoneNumberTextField.getText(),
                this.firstLvlDivisionComboBox.getSelectionModel().getSelectedItem()
        );
    }

    protected Customer createCustomerFromInputs(int customerId) {
        return new Customer(
                customerId,
                this.customerNameTextField.getText(),
                this.addressTextField.getText(),
                this.postalCodeTextField.getText(),
                this.phoneNumberTextField.getText(),
                this.firstLvlDivisionComboBox.getSelectionModel().getSelectedItem()
        );
    }

    protected void initComboBox(){
        List<Country> countries = this.countryService.findAll();
        this.countryComboBox.setItems(FXCollections.observableArrayList(countries));
        this.countryComboBox
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((options, oldValue, newValue) -> {
                    if(newValue != null) {
                        this.firstLvlDivisionComboBox.valueProperty().set(null);
                        List<Division> divisions = this.divisionService.findByCountryId(newValue.getCountryID());
                        this.firstLvlDivisionComboBox.setItems(FXCollections.observableArrayList(divisions));
                        this.firstLvlDivisionComboBox.setPromptText("");
                    } else {
                        this.firstLvlDivisionComboBox.setItems(null);
                    }
                });

        this.firstLvlDivisionComboBox.setPromptText("Select a country first");
    }

    protected void validateFormValues() {
        List.of(
                new String[]{"Customer Name", this.customerNameTextField.getText()},
                new String[]{"Address", this.addressTextField.getText()},
                new String[]{"Postal Code", this.postalCodeTextField.getText()},
                new String[]{"Phone Number", this.phoneNumberTextField.getText()}
        ).forEach(v -> {
            if(v[1] == null || v[1].isBlank() || v[1].isEmpty()){
                throw new IllegalArgumentException("Please fill out all fields! Missing: " + v[0]);
            }
        });
    }

    protected void validateAddressComboBoxes() {
        Country selectedCountry = this.countryComboBox.getSelectionModel().getSelectedItem();

        if(selectedCountry == null) {
            throw new IllegalArgumentException("Missing country: Please select a country!");
        } else {
            Division selectedDivision = this.firstLvlDivisionComboBox.getSelectionModel().getSelectedItem();
            if(selectedDivision == null) {
                throw new IllegalArgumentException("Missing division: Please select a division!");
            }
        }
    }

    protected void validateAddressText() {
        String addressFormValue = this.addressTextField.getText();

        List<Division> divisions = this.divisionService.findAll();
        List<Country> countries = this.countryService.findAll();

        divisions.forEach(division -> {
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

    protected void resetTextFields(){
        FxUtils.resetTextFields(List.of(
                this.customerNameTextField,
                this.addressTextField,
                this.phoneNumberTextField,
                this.postalCodeTextField
        ));
    }
}
