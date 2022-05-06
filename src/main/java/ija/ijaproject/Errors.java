package ija.ijaproject;

import javafx.scene.control.Alert;

/**
 * <p>
 * Errors class.
 * </p>
 *
 * @author musta-pollo
 * @version 1.1
 */
public class Errors {
    /**
     * <p>
     * showAlertDialog.
     * </p>
     *
     * @param alertText a {@link java.lang.String} object
     * @param alertType a {@link javafx.scene.control.Alert.AlertType} object
     */
    public static void showAlertDialog(String alertText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(alertText);
        alert.showAndWait();
    }
}
