package org.devrisby.c195.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.beans.value.ObservableIntegerValue;
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
import org.devrisby.c195.data.AppointmentRepository;
import org.devrisby.c195.data.DB;
import org.devrisby.c195.models.Appointment;
import org.devrisby.c195.views.SceneLoader;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

    @FXML
    ComboBox<String> appointmentViewComboBox;

    public AppointmentController() {
        this.appointmentRepository = new AppointmentRepository(DB.getConnection());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable(FXCollections.observableArrayList(this.appointmentRepository.findAll()));
        initComboBoxes();
        this.errorLabel.setText("");
        this.addButton.setOnAction(e -> SceneLoader.changeScene(Scenes.APPOINTMENTSADD, e));
        this.logoutButton.setOnAction(event -> SceneLoader.changeScene(Scenes.LOGIN, event));
        this.deleteButton.setOnAction(this::deleteOnAction);
        this.editButton.setOnAction(this::editOnAction);
    }

    private void initTable(ObservableList<Appointment> appointments) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd @ hh:mm a");

        // PropertyValueFactory constructor accepts the string name of the Java Class object field
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationTableColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(LocalDateTime.ofInstant(cellData.getValue().getStart(), ZoneId.systemDefault()).format(formatter)));
        endTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(LocalDateTime.ofInstant(cellData.getValue().getEnd(), ZoneId.systemDefault()).format(formatter)));
        contactNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContact().getContactName()));
        userIdTableColumn.setCellValueFactory(cellData -> (new SimpleIntegerProperty(cellData.getValue().getUser().getUserID())).asObject());
        customerIdTableColumn.setCellValueFactory(cellData -> (new SimpleIntegerProperty(cellData.getValue().getCustomer().getCustomerID()).asObject()));


        appointmentTableView.setItems(appointments);
    }

    private void initComboBoxes() {
        ObservableList<String> views = FXCollections.observableArrayList(List.of("Select View", "Current Month", "Current Week"));
        this.appointmentViewComboBox.setItems(views);
        this.appointmentViewComboBox
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((options, oldValue, newValue) -> {
                    switch(newValue) {
                        case "Select View": {
                            initTable(FXCollections.observableArrayList(this.appointmentRepository.findAll()));
                            break;
                        }

                        case "Current Month": {
                            initTable(FXCollections.observableArrayList(this.appointmentRepository.findByCurrentMonth()));
                            break;
                        }

                        case "Current Week": {
                            initTable(FXCollections.observableArrayList(this.appointmentRepository.findByCurrentWeek()));
                            break;
                        }

                        default: {
                            throw new RuntimeException("Could not populate table with appointments!\n");
                        }
                    }
                });
    }

    private void deleteOnAction(Event event) {
        TableSelectionModel<Appointment> selectedAppointment = this.appointmentTableView.getSelectionModel();
        if(selectedAppointment.getSelectedItem() != null) {
            Appointment appointment = selectedAppointment.getSelectedItem();
            if(confirmDeletionDialog(appointment)) {
                this.appointmentRepository.delete(appointment);
                this.errorLabel.setTextFill(Color.GREEN);
                this.errorLabel.setText("Appointment deleted!");
                initTable(FXCollections.observableArrayList(this.appointmentRepository.findAll()));
                this.appointmentViewComboBox.getSelectionModel().select(0);
            }
        } else {
            this.errorLabel.setText("No row selected. Please select a appointment from the table");
        }
    }

    private void editOnAction(Event event) {
        TableSelectionModel<Appointment> selectedAppointment = this.appointmentTableView.getSelectionModel();

        if(selectedAppointment.getSelectedItem() != null) {
            Appointment appointment = selectedAppointment.getSelectedItem();
            AppointmentEditController appointmentEditController = new AppointmentEditController(appointment);
            SceneLoader.changeScene(Scenes.APPOINTMENTSEDIT, event, appointmentEditController);
        } else {
            this.errorLabel.setText("No row selected. Please select an appointment from the table");
        }
    }

    private boolean confirmDeletionDialog(Appointment appointment){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Appointments");
        alert.setHeaderText("Delete " + appointment.getCustomer().getCustomerName() + "'s appointment?");
        alert.setContentText("Please confirm deletion for " + appointment.getCustomer().getCustomerName() + " appointment at " + appointment.getStart().toString());

        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }
}
