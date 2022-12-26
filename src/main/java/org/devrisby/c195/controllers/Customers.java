package org.devrisby.c195.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.devrisby.c195.data.CustomerRepository;
import org.devrisby.c195.data.DB;
import org.devrisby.c195.models.Customer;
import org.devrisby.c195.views.SceneLoader;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.util.ResourceBundle;

public class Customers implements Initializable {

    private CustomerRepository customerRepository;

    @FXML
    Label titleLabel;

    @FXML
    Label errorLabel;

    @FXML
    TableView<Customer> customerTableView;
    @FXML
    TableColumn<Customer, Integer> idTableColumn;
    @FXML
    TableColumn<Customer, String> nameTableColumn;
    @FXML
    TableColumn<Customer, String> addressTableColumn;
    @FXML
    TableColumn<Customer, String> postalCodeTableColumn;
    @FXML
    TableColumn<Customer, String> phoneNumberTableColumn;
    @FXML
    TableColumn<Customer, String> divisionTableColumn;
    @FXML
    TableColumn<Customer, String> countryTableColumn;

    @FXML
    Button addButton;

    @FXML
    Button deleteButton;

    @FXML
    Button logoutButton;

    @FXML
    Button editButton;

    public Customers(){
        this.customerRepository = new CustomerRepository(DB.getConnection());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        this.errorLabel.setText("");
        this.addButton.setOnAction(e -> SceneLoader.changeScene(Scenes.CUSTOMERADD, e));
        this.deleteButton.setOnAction(this::deleteOnAction);
        this.logoutButton.setOnAction(event -> SceneLoader.changeScene(Scenes.LOGIN, event));
        this.editButton.setOnAction(this::editOnAction);
    }

    private void initTable(){
        idTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("customerID"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        addressTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        postalCodeTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
        phoneNumberTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        divisionTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> p) {
                return new SimpleStringProperty(p.getValue().getDivision().getDivisionName());
            }
        });
        countryTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> p) {
                return new SimpleStringProperty(p.getValue().getDivision().getCountry().getCountryName());
            }
        });

        ObservableList<Customer> customers = FXCollections.observableArrayList(this.customerRepository.findAll());
        customerTableView.setItems(customers);
    }

    // TODO: delete appointments first
    private void deleteOnAction(Event event){
        TableSelectionModel<Customer> selectedCustomer = this.customerTableView.getSelectionModel();
        if(selectedCustomer.getSelectedItem() != null) {
            Customer customer = selectedCustomer.getSelectedItem();
            this.customerRepository.deleteById(customer.getCustomerID());
            this.errorLabel.setText("Customer " + customer.getCustomerName() + ", has been deleted!");
            initTable();
        } else {
            this.errorLabel.setText("No row selected. Please select a customer from the table");
        }
    }

    private void editOnAction(Event event) {
        TableSelectionModel<Customer> selectedCustomer = this.customerTableView.getSelectionModel();

        if(selectedCustomer.getSelectedItem() != null) {
            Customer customer = selectedCustomer.getSelectedItem();
            CustomerEdit customerEdit = new CustomerEdit(customer);
            SceneLoader.changeScene(Scenes.CUSTOMEREDIT, event, customerEdit);
        } else {
            this.errorLabel.setText("No row selected. Please select a customer from the table");
        }
    }
}
