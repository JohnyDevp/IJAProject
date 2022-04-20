package ija.ijaproject;

import javafx.scene.control.Alert;

public class Errors {
    public static void showAlertDialog(String alertText, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setContentText(alertText);
        alert.showAndWait();
    }
}
