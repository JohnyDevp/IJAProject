
package ija.ijaproject;

import ija.ijaproject.cls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Dialog for adding new attribute - for adding attribute as attribute and attribute as param for operation
 * @author xholan11
 * */

public class AddAttributeDialogController {

    @FXML
    private ComboBox cmbModifier;

    @FXML
    private TextField txtAttrName;

    @FXML
    private TextField txtAttrType;

    private UMLAttribute umlAttribute = null;

    public boolean isCmbModifierEnabled = false;

    public void init(boolean isCmbModifierEnabled){
        if (isCmbModifierEnabled){
            cmbModifier.getItems().addAll("#","-","+","~");
            cmbModifier.getSelectionModel().selectFirst();
        }
        this.isCmbModifierEnabled = isCmbModifierEnabled;
    }

    @FXML
    public void btnAddAttribute(ActionEvent actionEvent) {
        String name = txtAttrName.getText().trim();
        String type = txtAttrType.getText().trim();

        //name and type cannot be empty
        if (name == "" || type=="") {
            System.out.println("Name and type has to be set");
            return;
        }
        if (isCmbModifierEnabled){
            this.umlAttribute = new UMLAttribute(cmbModifier.getValue().toString().toCharArray()[0], name, type);
        }else {
            this.umlAttribute = new UMLAttribute(name, type);
        }
        closeStage(actionEvent);
    }

    /**
     * method for get newly created attribute
     * */
    public UMLAttribute getUmlAttribute(){
        return this.umlAttribute;
    }

    /**
     * method for closing this stage - from */
    private void closeStage(ActionEvent event) {
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
}

