package org.devrisby.c195.views;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Enum class for FXML file names and file paths*/
public enum Scenes {
    LOGIN("/login.fxml", "Login"),
    HOME("/home.fxml", "Home"),
    CUSTOMERS("/customers.fxml", "Customers"),
    CUSTOMERADD("/customerAdd.fxml", "Add New Customer"),
    CUSTOMEREDIT("/customerEdit.fxml", "Edit Customer"),
    APPOINTMENTS("/appointments.fxml", "Appointments"),
    APPOINTMENTSADD("/appointmentAdd.fxml", "Add new Appointment"),
    APPOINTMENTSEDIT("/appointmentEdit.fxml", "Edit appointment"),
    REPORTS("/reports.fxml", "Reports");

    private final String fileName;
    private final String sceneName;

    Scenes(String fileName, String sceneName) {
        this.fileName = fileName;
        this.sceneName = sceneName;
    }

    public URL getFXMLFileURL() {
        return getClass().getResource(this.fileName);
    }

    public String getSceneName() {
        return this.sceneName;
    }
}
