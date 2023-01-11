package org.devrisby.c195.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.List;

/** Utility class for JavaFX Controller*/
public class FxUtils {

    /** Sets status message.
     * Set color of message by indicating if it is an error (RED) or not (GREEN)
     * */
    public static void setStatusMessage(Label label, String message, boolean isError){
        label.setText(message);
        label.setTextFill(isError ? Color.RED : Color.GREEN);
    }

    /** Creates Confirmation Dialog box.
     * Use to confirm an action before executing that action
     * */
    public static boolean confirmActionDialog(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    /** Resets all text fields to blank. */
    public static void resetTextFields(List<TextField> fields) {
        fields.forEach(f -> f.setText(""));
    }
}
