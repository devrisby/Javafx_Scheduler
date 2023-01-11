package org.devrisby.c195.modules.appointment.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.devrisby.c195.controllers.FxUtils;
import org.devrisby.c195.modules.appointment.Appointment;
import org.devrisby.c195.modules.appointment.AppointmentService;
import org.devrisby.c195.views.SceneLoader;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/** FXML Controller for the Appointment Screen */
public class AppointmentController implements Initializable {

    private final AppointmentService appointmentService;

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
    Button backButton;
    @FXML
    Button editButton;
    @FXML
    ComboBox<String> appointmentViewComboBox;

    /** Constructor for Appointment Controller. Initializes AppointmentService instance */
    public AppointmentController() {
        this.appointmentService = new AppointmentService();
    }

    /** Initializes Appointment Controller. Populates table with appointment data and sets button actions */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initComboBoxes();
        this.editButton.setOnAction(this::editOnAction);
        this.deleteButton.setOnAction(this::deleteOnAction);
        FxUtils.setStatusMessage(errorLabel, "", false);
        initTable(FXCollections.observableArrayList(this.appointmentService.findAll()));
        this.addButton.setOnAction(e -> SceneLoader.changeScene(Scenes.APPOINTMENTSADD, e));
        this.backButton.setOnAction(event -> SceneLoader.changeScene(Scenes.HOME, event));
    }

    /** Initializes TableView with Appointments.
     * LAMBDA FUNCTION: I used lambdas to set the table cell values for the columns */
    public void initTable(ObservableList<Appointment> appointments) {
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
                            initTable(FXCollections.observableArrayList(this.appointmentService.findAll()));
                            break;
                        }
                        case "Current Month": {
                            initTable(FXCollections.observableArrayList(this.appointmentService.findByCurrentMonth()));
                            break;
                        }
                        case "Current Week": {
                            initTable(FXCollections.observableArrayList(this.appointmentService.findByCurrentWeek()));
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

            String title = "Appointments";
            String headerText = "Delete " + appointment.getCustomer().getCustomerName() + "'s appointment?";
            String contentText = "Please confirm deletion for " + appointment.getCustomer().getCustomerName() +
                                 " appointment at " + appointment.getStart().toString();

            if(FxUtils.confirmActionDialog(title, headerText, contentText)) {
                this.appointmentService.delete(appointment);
                FxUtils.setStatusMessage(errorLabel, "Appointment ID: " + appointment.getAppointmentID() +
                                            ", of Type: " + appointment.getType() + ", deleted!", false);
                initTable(FXCollections.observableArrayList(this.appointmentService.findAll()));
                this.appointmentViewComboBox.getSelectionModel().select(0);
            }
        } else {
            FxUtils.setStatusMessage(errorLabel, "No row selected. Please select a appointment from the table", true);
        }
    }

    private void editOnAction(Event event) {
        TableSelectionModel<Appointment> selectedAppointment = this.appointmentTableView.getSelectionModel();

        if(selectedAppointment.getSelectedItem() != null) {
            Appointment appointment = selectedAppointment.getSelectedItem();
            SceneLoader.changeScene(Scenes.APPOINTMENTSEDIT, event, new AppointmentFormEditController(appointment));
        } else {
            FxUtils.setStatusMessage(errorLabel, "No row selected. Please select a appointment from the table", true);
        }
    }
}
