package ija.ijaproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

/**
 * controller for welcome view - view with info about controlling this app
 *
 * @author xzimol04, xholan11
 * @version 1.1
 */
public class WelcomeViewController {
    @FXML
    public Button btnOk;

    @FXML
    public TextFlow txtFlow;

    /**
     * 
     * initialize.
     * 
     */
    public void initialize() {
        String txt = String.join("\n",
                "APP USAGE\n",
                "In the main window, you can either load a new class diagram from a JSON file (example in /data folder) or create a new one.\n",
                "You can add a new class by clicking on ADD CLASS button. Create relations by clicking on ADD RELATION button and then on the edge of both classes.\n",
                "For edit mark object or relation by clicking and then press appropriate button.",
                "In the sequence diagram, you can add classes created in the class diagram and create messages between them by right-clicking on the timeline below a class.\n",
                "Close all diagrams by clicking on the CLOSE button in the class diagram.\n",
                "Save all by clicking on the SAVE button in the class diagram.\n",
                "APP MADE BY:  Jan Zimola (xzimol04); Jan Holáň (xholan11)\n");
        Text t = new Text(txt);
        txtFlow.getChildren().add(t);
    }

    /**
     * 
     * btnOk.
     * 
     *
     * @param e a {@link javafx.event.ActionEvent} object
     */
    public void btnOk(ActionEvent e) {
        closeStage(e);
    }

    /**
     * method for closing this stage
     */
    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
