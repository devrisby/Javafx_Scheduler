package org.devrisby.c195.controllers;

import javafx.collections.FXCollections;
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
import org.devrisby.c195.models.Customer;
import org.devrisby.c195.models.FirstLvlDivision;
import org.devrisby.c195.views.SceneLoader;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CustomerEditController implements Initializable {
    private final Customer customer;
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
    Button editButton;

    @FXML
    Button backButton;

    @FXML
    Label errorLabel;

    public CustomerEditController(Customer customer) {
        this.customer = customer;
        this.customerRepository = new CustomerRepository(DB.getConnection());
        this.firstLvlDivisionRepository = new FirstLvlDivisionRepository(DB.getConnection());
        this.countryRepository = new CountryRepository(DB.getConnection());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.customerNameTextField.setText(this.customer.getCustomerName());
        this.addressTextField.setText(this.customer.getAddress());
        this.postalCodeTextField.setText(this.customer.getPostalCode());
        this.phoneNumberTextField.setText(this.customer.getPhone());
        initComboBox();

        this.errorLabel.setText("");
        this.backButton.setOnAction(actionEvent -> SceneLoader.changeScene(Scenes.CUSTOMERS, actionEvent));
    }

    private void initComboBox(){
        List<FirstLvlDivision> divisions = this.firstLvlDivisionRepository.findAll();
        List<String> comboBoxChoices = divisions
                .stream()
                .map(d -> d.getDivisionName() + "," + d.getCountry().getCountryName())
                .collect(Collectors.toList());

        this.firstLvlDivisionCountryComboBox.setItems(FXCollections.observableArrayList(comboBoxChoices));
        int initialChoice = comboBoxChoices.indexOf(this.customer.getDivision().getDivisionName() + "," + this.customer.getDivision().getCountry().getCountryName());
        this.firstLvlDivisionCountryComboBox.getSelectionModel().select(initialChoice);
    }
}
