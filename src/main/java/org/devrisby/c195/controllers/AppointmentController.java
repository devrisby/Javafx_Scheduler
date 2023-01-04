package org.devrisby.c195.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.devrisby.c195.data.AppointmentRepository;
import org.devrisby.c195.data.DB;
import org.devrisby.c195.models.Appointment;
import org.devrisby.c195.views.SceneLoader;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {

    private AppointmentRepository appointmentRepository;

    @FXML
    Label titleLabel;

    @FXML
    Label errorLabel;

    @FXML
    TableView<Appointment> appointmentTableView;
    @FXML
    TableColumn<Appointment, Integer> idTableColumn;
    @FXML
    TableColumn<Appointment, String> titleTableColumn;
    @FXML
    TableColumn<Appointment, String> descriptionTableColumn;
    @FXML
    TableColumn<Appointment, String> locationTableColumn;
    @FXML
    TableColumn<Appointment, String> contactNameTableColumn;
    @FXML
    TableColumn<Appointment, String> typeTableColumn;
    @FXML
    TableColumn<Appointment, String> startTableColumn;

    @FXML
    TableColumn<Appointment, String> endTableColumn;

    @FXML
    TableColumn<Appointment, Integer> userIdTableColumn;

    @FXML
    TableColumn<Appointment, Integer> customerIdTableColumn;

    @FXML
    Button addButton;

    @FXML
    Button deleteButton;

    @FXML
    Button logoutButton;

    @FXML
    Button editButton;

    public AppointmentController() {
        this.appointmentRepository = new AppointmentRepository(DB.getConnection());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        this.errorLabel.setText("");
        this.logoutButton.setOnAction(event -> SceneLoader.changeScene(Scenes.LOGIN, event));
    }

    private void initTable() {
        // PropertyValueFactory constructor accepts the string name of the Java Class object field
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationTableColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStart().toString()));
        endTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd().toString()));
        contactNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContact().getContactName()));
        userIdTableColumn.setCellValueFactory(cellData -> (new SimpleIntegerProperty(cellData.getValue().getUser().getUserID())).asObject());
        customerIdTableColumn.setCellValueFactory(cellData -> (new SimpleIntegerProperty(cellData.getValue().getCustomer().getCustomerID()).asObject()));


        // Retrieve appointments from database
        ObservableList<Appointment> appointments = FXCollections.observableArrayList(this.appointmentRepository.findAll());

        // Initialize tableview with appointments
        appointmentTableView.setItems(appointments);
    }
}
