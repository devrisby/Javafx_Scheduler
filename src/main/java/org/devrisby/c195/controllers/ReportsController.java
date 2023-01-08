package org.devrisby.c195.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import org.devrisby.c195.data.AppointmentRepository;
import org.devrisby.c195.data.ContactRepository;
import org.devrisby.c195.data.CountryRepository;
import org.devrisby.c195.data.DB;
import org.devrisby.c195.models.*;
import org.devrisby.c195.services.AppointmentService;
import org.devrisby.c195.services.CustomerService;
import org.devrisby.c195.views.SceneLoader;
import org.devrisby.c195.views.Scenes;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ReportsController implements Initializable {

    private AppointmentService appointmentService;
    private AppointmentRepository appointmentRepository;
    private CountryRepository countryRepository;
    private CustomerService customerService;

    private ContactRepository contactRepository;

    @FXML
    TextField appointmentsTextField;

    @FXML
    ComboBox<String> typeComboBox;

    @FXML
    ComboBox<String> monthComboBox;

    @FXML
    Button calculateAppointmentsButton;

    @FXML
    TextField customerTextField;

    @FXML
    ComboBox<Country> countryComboBox;

    @FXML
    Button calculateCustomersButton;

    @FXML
    TreeTableView<Appointment> scheduleTreeTableView;

    @FXML
    TreeTableColumn<Appointment, String> contactNameTreeTableColumn;
    @FXML
    TreeTableColumn<Appointment, String> appointmentIdTreeTableColumn;
    @FXML
    TreeTableColumn<Appointment, String> customerIdTreeTableColumn;
    @FXML
    TreeTableColumn<Appointment, String> titleTreeTableColumn;
    @FXML
    TreeTableColumn<Appointment, String> descriptionTreeTableColumn;
    @FXML
    TreeTableColumn<Appointment, String> typeTreeTableColumn;
    @FXML
    TreeTableColumn<Appointment, String> startTreeTableColumn;
    @FXML
    TreeTableColumn<Appointment, String> endTreeTableColumn;

    @FXML
    Button backButton;

    public ReportsController() {
        this.appointmentService = new AppointmentService();
        this.appointmentRepository = new AppointmentRepository(DB.getConnection());
        this.countryRepository = new CountryRepository(DB.getConnection());
        this.customerService = new CustomerService();
        this.contactRepository = new ContactRepository(DB.getConnection());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.backButton.setOnAction(event -> SceneLoader.changeScene(Scenes.HOME, event));
        initAppointmentReportControls();
        initCustomerReportControls();
        initScheduleReport();
    }

    private void initAppointmentReportControls(){
        List<Appointment> appointments = this.appointmentRepository.findAll();

        List<String> appointmentTypes = appointments
                .stream()
                .map(a -> a.getType())
                .distinct()
                .collect(Collectors.toList());

        List<String> appointmentMonths = appointments
                .stream()
                .map(a -> LocalDate.ofInstant(a.getStart(), ZoneId.systemDefault()).getMonth().name())
                .distinct()
                .collect(Collectors.toList());


        this.monthComboBox.setItems(FXCollections.observableArrayList(appointmentMonths));
        this.typeComboBox.setItems(FXCollections.observableArrayList(appointmentTypes));
        this.calculateAppointmentsButton.setOnAction(this::onCalculateAppointmentsAction);
    }

    private void onCalculateAppointmentsAction(ActionEvent event) {
        String month = this.monthComboBox.getSelectionModel().getSelectedItem();
        String type = this.typeComboBox.getSelectionModel().getSelectedItem();

        if(month == null || type == null) {
            this.appointmentsTextField.setText("0");
        } else {
            String numOfAppointments = Integer.toString(this.appointmentService.numOfAppointmentsByMonthType(month, type));
            this.appointmentsTextField.setText(numOfAppointments);
        }
    }

    private void initCustomerReportControls() {
        List<Appointment> appointments = this.appointmentRepository.findAll();
        List<Country> countries = this.countryRepository.findAll();
        this.countryComboBox.setItems(FXCollections.observableArrayList(countries));
        this.calculateCustomersButton.setOnAction(this::onCalculateCustomersActions);
    }

    private void onCalculateCustomersActions(ActionEvent event) {
        Country country = this.countryComboBox.getSelectionModel().getSelectedItem();

        if(country == null){
            this.customerTextField.setText("0");
        } else {
            this.customerTextField.setText(Integer.toString(this.customerService.countCustomersByCountry(country)));
        }
    }

    private void initScheduleReport() {

        List<Appointment> appointments = this.appointmentRepository.findAll();
        List<Contact> contacts = this.contactRepository.findAll();

        List<TreeItem<Appointment>> roots = contacts
                .stream()
                .map(c -> new TreeItem<>(new Appointment(
                        "", "", "", "",
                        null, null, null, c, null
                )))
                .collect(Collectors.toList());

        Map<Integer, List<List<Appointment>>> appointmentsByContacts = contacts
                .stream()
                .collect(Collectors.groupingBy(
                        Contact::getContactID,
                        Collectors.mapping(c -> appointments
                                .stream()
                                .filter(a -> a.getContact().getContactID() == c.getContactID())
                                .collect(Collectors.toList()), Collectors.toList()
                )));

        appointmentsByContacts
                .forEach((key, value) -> {
                    List<TreeItem<Appointment>> treeItems = value.get(0).stream().map(TreeItem::new).collect(Collectors.toList());

                    roots.stream()
                            .filter(r -> r.getValue().getContact().getContactID() == key)
                            .findFirst()
                            .get()
                            .getChildren()
                            .addAll(treeItems);
                });


        TreeItem<Appointment> root = new TreeItem<>(new Appointment(
                "title", "description", "location", "type",
                Instant.now(), Instant.now(),
                new Customer("Name", "address", "postal", "phone", null),
                new Contact("Contact Name", "email"),
                new User("username", "password")
        ));

        initScheduleReportColumns();
        roots.forEach(r -> root.getChildren().add(r));
        this.scheduleTreeTableView.setRoot(root);
        this.scheduleTreeTableView.setShowRoot(false);
    }

    private void initScheduleReportColumns() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd @ hh:mm a");

        this.titleTreeTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("title"));
        this.descriptionTreeTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        this.typeTreeTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("type"));
        this.contactNameTreeTableColumn.setCellValueFactory(cellData -> {
          if(cellData.getValue().getValue().getAppointmentID() != -1) {
              return new SimpleStringProperty("");
          } else {
              return new SimpleStringProperty(cellData.getValue().getValue().getContact().getContactName());
          }
        });

        this.startTreeTableColumn.setCellValueFactory(cellData -> {
            if(cellData.getValue().getValue().getAppointmentID() == -1 ) {
                return new SimpleStringProperty("");
            } else {
                return new SimpleStringProperty(LocalDateTime.ofInstant(cellData.getValue().getValue().getStart(), ZoneId.systemDefault()).format(formatter));
            }
        });

        this.endTreeTableColumn.setCellValueFactory(cellData -> {
            if(cellData.getValue().getValue().getAppointmentID() == -1) {
                return new SimpleStringProperty("");
            } else {
                return new SimpleStringProperty(LocalDateTime.ofInstant(cellData.getValue().getValue().getEnd(), ZoneId.systemDefault()).format(formatter));
            }
        });

        this.appointmentIdTreeTableColumn.setCellValueFactory(cellData -> {
            if(cellData.getValue().getValue().getAppointmentID() == -1) {
                return new SimpleStringProperty("");
            } else {
                return  new SimpleStringProperty(Integer.toString(cellData.getValue().getValue().getAppointmentID()));
            }
        });


        this.customerIdTreeTableColumn.setCellValueFactory(cellData -> {
            if(cellData.getValue().getValue().getAppointmentID() == -1) {
                return new SimpleStringProperty("");
            } else {
                return new SimpleStringProperty(Integer.toString(cellData.getValue().getValue().getCustomer().getCustomerID()));
            }
        });
    }
}
