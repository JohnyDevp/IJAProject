package ija.ijaproject;

import ija.ijaproject.cls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class AddAttributeDialogController {

    @FXML
    public TextField txtAttrName;

    @FXML
    public TextField txtAttrType;

    private UMLAttribute umlAttribute = null;

    @FXML
    public void btnAddAttribute(ActionEvent actionEvent) {
        String name = txtAttrName.getText().trim();
        String type = txtAttrType.getText().trim();

        //name and type cannot be empty
        if (name == "" || type=="") {
            System.out.println("Name and type has to be set");
            return;
        }

        this.umlAttribute = new UMLAttribute(name, type);
        closeStage(actionEvent);
    }

    /**
     * method for get newly created attribute
     * */
    public UMLAttribute getUmlAttribute(){
        return this.umlAttribute;
    }

    /**
     * method for closing this stage => from */
    private void closeStage(ActionEvent event) {
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
}

