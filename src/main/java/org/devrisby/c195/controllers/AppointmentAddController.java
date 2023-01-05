package org.devrisby.c195.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.devrisby.c195.data.*;
import org.devrisby.c195.models.Contact;
import org.devrisby.c195.models.Customer;
import org.devrisby.c195.models.User;
import org.devrisby.c195.views.SceneLoader;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AppointmentAddController implements Initializable {

    private AppointmentRepository appointmentRepository;
    private CustomerRepository customerRepository;
    private ContactRepository contactRepository;
    private UserRepository userRepository;

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
    ComboBox<String> contactComboBox;
    @FXML
    ComboBox<String> userComboBox;
    @FXML
    ComboBox<String> customerComboBox;
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
        this.appointmentRepository = new AppointmentRepository(DB.getConnection());
        this.customerRepository = new CustomerRepository(DB.getConnection());
        this.contactRepository = new ContactRepository(DB.getConnection());
        this.userRepository = new UserRepository(DB.getConnection());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.errorLabel.setText("");
        this.backButton.setOnAction(actionEvent -> SceneLoader.changeScene(Scenes.APPOINTMENTS, actionEvent));
        initComboBox();
    }

    private void initComboBox() {
        // Todo: have combobox be set to models instead of string, using: https://stackoverflow.com/a/38367739
        List<Contact> contacts = this.contactRepository.findAll();
        contacts.forEach(c -> this.contactComboBox.getItems().add("#" + c.getContactID() + " " + c.getContactName()));

        List<User> users = this.userRepository.findAll();
        users.forEach(u -> this.userComboBox.getItems().add("#" + u.getUserID() + " " + u.getUserName()));

        List<Customer> customers = this.customerRepository.findAll();
        customers.forEach(c -> this.customerComboBox.getItems().add("#" + c.getCustomerID() + " " + c.getCustomerName()));

        this.startAMPMComboBox.setItems(FXCollections.observableList(List.of("--", "AM", "PM")));
        this.endAMPMComboBox.setItems(FXCollections.observableList(List.of("--", "AM", "PM")));
    }
}
