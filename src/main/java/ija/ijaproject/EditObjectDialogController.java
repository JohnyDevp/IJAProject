

package ija.ijaproject;

import ija.ijaproject.cls.ClassDiagram;
import ija.ijaproject.cls.UMLAttribute;
import ija.ijaproject.cls.UMLClass;
import ija.ijaproject.cls.UMLOperation;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Controller for editting either class or interface in class diagram => editting attributes and operations
 * @author xholan11
 * */
public class EditObjectDialogController {

    @FXML
    public TextField txtObjectName;

    @FXML
    public ComboBox cmbAttributes;

    @FXML
    public Button btnAddAttribute;

    @FXML
    public ComboBox cmbOperations;

    @FXML
    public Button btnAddOperation;

    @FXML
    public ComboBox cmbModifier;

    @FXML
    public TextField txtOperationName;

    @FXML
    public TextField txtOperationType;

    private GUIClassInterfaceTemplate guiObject;
    //for the possibility of renaming object => has to be checked
    private ClassDiagram clsDiag;
    private Pane canvas;
    private List<UMLAttribute> attributesOfOperation = new ArrayList<>();

    /**
     * method for initialization of this controller */
    public void init(GUIClassInterfaceTemplate guiObject, ClassDiagram clsDiag, Pane canvas){
        this.guiObject = guiObject;
        this.canvas = canvas;
        this.clsDiag = clsDiag;

        //disable attribute things iff interface take part
        if (guiObject.getClass() == InterfaceObjectGUI.class){
            this.btnAddAttribute.setDisable(true);
            this.cmbAttributes.setDisable(true);
        }

        //load both combo boxes
        loadCmbAttributes();
        loadCmbOperations();

        //load modifier for operation
        cmbModifier.getItems().addAll("-","+",'#',"~");
        cmbModifier.getSelectionModel().selectFirst();

    }

    /**
     * load combobox with attributes
     * */
    private void loadCmbAttributes(){
        //load combobox with attributes
        if (guiObject.getClass() == ClassObjectGUI.class){
            cmbAttributes.getItems().clear();
            //loop through text array and add texts (which have to be unique => code is set to satisfy that) to cmbbox
            for(Text txtAttr : ((ClassObjectGUI) guiObject).getListOfAttributes()){
                cmbAttributes.getItems().add(txtAttr.getText());
            }
        }
    }

    /**
     * load combobox with operations
     * */
    private void loadCmbOperations(){
        cmbOperations.getItems().clear();
        //load combobox with operations => this is for both interface and class
        for (Text txtOper : guiObject.getListOfOperations()){
            cmbOperations.getItems().add(txtOper.getText());
        }
    }

    /**adding attribute => for class*/
    public void btnAddAttribute(ActionEvent actionEvent) throws IOException {
        //show creating dialog
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/editObjectDialogAddAttribute_view.fxml"));
        Parent parent = fxmlLoader.load();
        AddAttributeDialogController dlgController = fxmlLoader.<AddAttributeDialogController>getController();

        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();

        //if the dialog was closed
        if(dlgController.getUmlAttribute() == null) return;
        else {
            //take the newly created attribute and add it on canvas iff could be created
            Text newAttr = ((ClassObjectGUI)guiObject).addAttribute(dlgController.getUmlAttribute());
            if (newAttr != null) this.canvas.getChildren().add(newAttr);
        }

        //reload combobox
        loadCmbAttributes();
        cmbAttributes.setItems(cmbAttributes.getItems());

    }

    /**button for deleting attribute from class object => chosen in combobox*/
    public void btnDeleteAttribute(ActionEvent actionEvent) {
        //loop the map of attributes and when find then remove it
        for (Map.Entry<UMLAttribute,Text> mapAttr : ((ClassObjectGUI)guiObject).getMapOfAttributes().entrySet()){
            if(cmbAttributes.getValue() == mapAttr.getValue().getText()){
                //remove from canvas
                this.canvas.getChildren().remove(mapAttr.getValue());
                //remove attribute's representations
                ((ClassObjectGUI)guiObject).removeAttribute(mapAttr.getKey());
            }
        }
        //reload combobox
        loadCmbAttributes();
        cmbAttributes.setItems(cmbAttributes.getItems());

    }

    /**button for renaming the object*/
    public void btnRename(ActionEvent actionEvent) {
        //check if the name isnt empty
        if (!txtObjectName.getText().equals("")){
            //check if there isnt ant other class with desired name
            UMLClass tmp = this.clsDiag.createClass(txtObjectName.getText());
            if (tmp != null){
                //if not then delete the tmp class (testing the name) and set the name
                this.clsDiag.deleteClass(tmp);
                //set the new name for inner representation
                this.guiObject.getObject().setName(txtObjectName.getText());
                //set the name of label representing object name
                this.guiObject.getClassNameLabel().setText(txtObjectName.getText());

            }
        }
    }

    public void btnDeleteOperation(ActionEvent actionEvent) {
    }

    public void btnAddOperation(ActionEvent actionEvent) {
        //if one of required field is null then return
        if (txtOperationName.getText().equals("") || txtOperationType.getText().equals("")) return;

        //create new operation
        UMLOperation newOperation = new UMLOperation(txtOperationName.getText(), txtOperationType.getText(), cmbModifier.getValue().toString().toCharArray()[0]);
        //add all previously created attributes to it
        for (UMLAttribute uop: this.attributesOfOperation){
            newOperation.addOperationParameter(uop);
        }

        //try to add it to class and according to success add it to cmbbox
        Text txtOpr = this.guiObject.addOperation(newOperation);
        if(txtOpr == null) return; //fail => already exists
        else {
            //add it to canvas
            this.canvas.getChildren().add(txtOpr);
            //reload combobox
            loadCmbOperations();
            cmbAttributes.setItems(cmbAttributes.getItems());
        }
    }

    /**add attribute for operation*/
    public void btnAddOperationAttribute(ActionEvent actionEvent) throws IOException {
        //show creating dialog
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/editObjectDialogAddAttribute_view.fxml"));
        Parent parent = fxmlLoader.load();
        AddAttributeDialogController dlgController = fxmlLoader.<AddAttributeDialogController>getController();

        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();

        //if the dialog was closed
        if(dlgController.getUmlAttribute() == null) return;
        else {
            //add attribute to the list of attributes for operation
            this.attributesOfOperation.add(dlgController.getUmlAttribute());
        }
    }
}