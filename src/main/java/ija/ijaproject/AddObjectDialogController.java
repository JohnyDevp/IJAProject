
package ija.ijaproject;

import ija.ijaproject.cls.ClassDiagram;
import ija.ijaproject.cls.UMLClass;
import ija.ijaproject.cls.UMLClassInterfaceTemplate;
import ija.ijaproject.cls.UMLInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Dialog for adding new interface or class to diagram
 *
 * @author xholan11
 * @version 1.1
 */
public class AddObjectDialogController {
    @FXML
    public Button btnOk;

    @FXML
    public TextField txtField;

    @FXML
    public CheckBox checkBox;

    private ClassDiagram mainClassDiagram;
    private UMLClassInterfaceTemplate createdObject;

    /**
     * <p>
     * Constructor for AddObjectDialogController.
     * </p>
     */
    public AddObjectDialogController() {
        this.mainClassDiagram = null;
        this.createdObject = null;
    }

    /**
     * method handling clicking on OK button - adding new object to class diagram
     *
     * @param e a {@link javafx.event.ActionEvent} object
     */
    public void btnOkClick(ActionEvent e) {
        boolean isInterface = checkBox.isSelected();
        String name = txtField.getText();

        // name cannot be empty
        if (name == "") {
            System.out.println("Name has to be set");
            return;
        }

        // create an interface
        if (isInterface) {
            UMLInterface umlInterface = mainClassDiagram.createInterface(name);
            if (umlInterface == null) {
                System.out.println("Object of this name already exists");
                return;
            } else {
                System.out.println("Object added");
                this.createdObject = umlInterface;
                closeStage(e);
            }
        } else { // create a class
            UMLClass umlClass = mainClassDiagram.createClass(name);
            if (umlClass == null) {
                System.out.println("Object of this name already exists");
            } else {
                System.out.println("Object added");
                this.createdObject = umlClass;
                closeStage(e);
            }
        }
    }

    /**
     * setter of class diagram - to be able to add there new object
     *
     * @param mainClassDiagram a {@link ija.ijaproject.cls.ClassDiagram} object
     */
    public void setMainClassDiagram(ClassDiagram mainClassDiagram) {
        this.mainClassDiagram = mainClassDiagram;
    }

    /**
     * method for get newly created object
     *
     * @return a {@link ija.ijaproject.cls.UMLClassInterfaceTemplate} object
     */
    public UMLClassInterfaceTemplate getCreatedObject() {
        return this.createdObject;
    }

    /**
     * method for closing this stage - from
     */
    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
