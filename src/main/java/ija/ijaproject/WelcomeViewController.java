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
 * @author xzimol04
 * */
public class WelcomeViewController {
    @FXML
    public Button btnOk;

    @FXML
    public TextFlow txtFlow;

    public void initialize(){
        Text t = new Text("" +
                "APP USAGE\n" +
                "--- In main window you can either load new class diagram from json file (example in /data folder) or" +
                "create new one. \n" +
                "--- You can add new interface or class by clicking on ADD INTERFACE/CLASS button.\n" +
                "--- Adding new attributes and operations to currently selected class (red sign) by clicking on" +
                "EDIT CLASS/INTERFACE button, which will raise up new dialog where you can remove or add these attributes " +
                "or operations\n" +
                "--- Close all diagrams by clicking on CLOSE button\n" +
                "\n\n\n" +
                "APP MADE BY: Jan ZIMOLA (xzimol04); Jan HOLAN (xholan11)");
        txtFlow.getChildren().add(t);
    }

    public void btnOk(ActionEvent e){
        closeStage(e);
    }

    /**
     * method for closing this stage */
    private void closeStage(ActionEvent event) {
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
