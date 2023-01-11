package org.devrisby.c195.modules.customer.controllers;

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
import org.devrisby.c195.controllers.FxUtils;
import org.devrisby.c195.modules.customer.Customer;
import org.devrisby.c195.modules.appointment.AppointmentService;
import org.devrisby.c195.modules.customer.CustomerService;
import org.devrisby.c195.views.SceneLoader;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.util.ResourceBundle;

/**  FXML Controller for Customer GUI Screen */
public class CustomerController implements Initializable {

    private final CustomerService customerService;
    private final AppointmentService appointmentService;

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
    Button backButton;
    @FXML
    Button editButton;

    public CustomerController(){
        this.customerService = new CustomerService();
        this.appointmentService = new AppointmentService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        FxUtils.setStatusMessage(errorLabel, "", false);
        this.addButton.setOnAction(e -> SceneLoader.changeScene(Scenes.CUSTOMERADD, e));
        this.deleteButton.setOnAction(this::deleteOnAction);
        this.backButton.setOnAction(event -> SceneLoader.changeScene(Scenes.HOME, event));
        this.editButton.setOnAction(this::editOnAction);
    }

    private void initTable(){
        this.idTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("customerID"));
        this.nameTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        this.addressTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        this.postalCodeTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
        this.phoneNumberTableColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        this.divisionTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> p) {
                return new SimpleStringProperty(p.getValue().getDivision().getDivisionName());
            }
        });
        this.countryTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> p) {
                return new SimpleStringProperty(p.getValue().getDivision().getCountry().getCountryName());
            }
        });

        ObservableList<Customer> customers = FXCollections.observableArrayList(this.customerService.findAll());
        this.customerTableView.setItems(customers);
    }

    private void deleteOnAction(Event event){
        TableSelectionModel<Customer> selectedCustomer = this.customerTableView.getSelectionModel();
        if(selectedCustomer.getSelectedItem() != null) {
            Customer customer = selectedCustomer.getSelectedItem();

            String title = "Appointments";
            String headerText = "Delete " + customer;
            int numOfAppointments = this.appointmentService.customerAppointments(customer).size();
            String appointmentsSingularOrPlural = "appointment" + (numOfAppointments == 1 ? "":"s");
            String contentText = numOfAppointments == 0 ?
                    customer.getCustomerName() + " has 0 appointments" :
                    "Deleting customer will delete " + customer.getCustomerName() + "'s " + numOfAppointments + " " + appointmentsSingularOrPlural;

            if(FxUtils.confirmActionDialog(title, headerText, contentText)) {
                this.customerService.delete(customer);
                FxUtils.setStatusMessage(errorLabel, "Customer " + customer.getCustomerName() + ", has been deleted!", false);
                initTable();
            }
        } else {
            FxUtils.setStatusMessage(errorLabel, "No row selected. Please select a customer from the table", true);
        }
    }

    private void editOnAction(Event event) {
        TableSelectionModel<Customer> selectedCustomer = this.customerTableView.getSelectionModel();

        if(selectedCustomer.getSelectedItem() != null) {
            Customer customer = selectedCustomer.getSelectedItem();
            CustomerFormEditController customerFormEditController = new CustomerFormEditController(customer);
            SceneLoader.changeScene(Scenes.CUSTOMEREDIT, event, customerFormEditController);
        } else {
            FxUtils.setStatusMessage(errorLabel, "No row selected. Please select a customer from the table", true);
        }
    }
}
