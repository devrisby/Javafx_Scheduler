package org.devrisby.c195.views;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum Scenes {
    LOGIN("login.fxml", "Login"),
    HOME("home.fxml", "Home"),
    CUSTOMERS("customers.fxml", "Customers"),
    CUSTOMERADD("customerAdd.fxml", "Add New Customer"),
    CUSTOMEREDIT("customerEdit.fxml", "Edit Customer");

    private final String fileName;
    private final String sceneName;

    Scenes(String fileName, String sceneName) {
        this.fileName = fileName;
        this.sceneName = sceneName;
    }

    public URL getFXMLFileURL() {
        // Gets path of resource directory
        Path resourceDirPath = Paths.get("..", "..", "..", "..");

        // Get path to FXML file
        Path fxmlFilePath = Paths.get(resourceDirPath.toString(), this.fileName);

        return getClass().getResource(fxmlFilePath.toString());
    }

    public String getSceneName() {
        return this.sceneName;
    }
}
