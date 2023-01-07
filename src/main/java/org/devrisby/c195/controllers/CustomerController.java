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
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.devrisby.c195.data.CustomerRepository;
import org.devrisby.c195.data.DB;
import org.devrisby.c195.models.Appointment;
import org.devrisby.c195.models.Customer;
import org.devrisby.c195.services.CustomerService;
import org.devrisby.c195.views.SceneLoader;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    private CustomerRepository customerRepository;
    private CustomerService customerService;

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

    public CustomerController(){
        this.customerRepository = new CustomerRepository(DB.getConnection());
        this.customerService = new CustomerService();
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
    // TODO: get deletion confirmation first before deleting
    private void deleteOnAction(Event event){
        TableSelectionModel<Customer> selectedCustomer = this.customerTableView.getSelectionModel();
        if(selectedCustomer.getSelectedItem() != null) {
            Customer customer = selectedCustomer.getSelectedItem();
            if(confirmDeletionDialog(customer)) {
                this.customerService.delete(customer);
                this.errorLabel.setTextFill(Color.GREEN);
                this.errorLabel.setText("Customer " + customer.getCustomerName() + ", has been deleted!");
                initTable();
            }
        } else {
            this.errorLabel.setText("No row selected. Please select a customer from the table");
        }
    }

    private void editOnAction(Event event) {
        TableSelectionModel<Customer> selectedCustomer = this.customerTableView.getSelectionModel();

        if(selectedCustomer.getSelectedItem() != null) {
            Customer customer = selectedCustomer.getSelectedItem();
            CustomerEditController customerEditController = new CustomerEditController(customer);
            SceneLoader.changeScene(Scenes.CUSTOMEREDIT, event, customerEditController);
        } else {
            this.errorLabel.setText("No row selected. Please select a customer from the table");
        }
    }

    private boolean confirmDeletionDialog(Customer customer){
        String name = customer.getCustomerName();
        int numOfAppointments = this.customerService.findAppointments(customer).size();
        String appointmentsSingularOrPlural = "appointment" + (numOfAppointments == 1 ? "":"s");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Appointments");
        alert.setHeaderText("Delete " + customer);
        alert.setContentText("Please confirm deletion for " + name + "'s " + numOfAppointments + " " + appointmentsSingularOrPlural);

        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }
}
