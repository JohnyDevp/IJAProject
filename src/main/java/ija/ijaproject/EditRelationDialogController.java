

package ija.ijaproject;

import ija.ijaproject.cls.ClassDiagram;
import ija.ijaproject.cls.UMLRelation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * controller for dialog for editing parameters of relation
 * @author xholan11
 **/
public class EditRelationDialogController {
    @FXML
    public Button btnSave;

    @FXML
    public Button btnDelete;

    @FXML
    public TextField txtName;
    @FXML
    public ComboBox cmbRelationType;
    @FXML
    public TextField txtCardinalityFrom;
    @FXML
    public TextField txtCardinalityTo;

    /**variable for storing currently changing variable*/
    private RelationGUI relationGUI;
    private Pane canvas;
    private ClassDiagram classDiagram;

    public void onShown(ActionEvent e){


        //set text for all text fields according to the values of relation
        //txtName.setText(relationGUI.getNameOfRelation().getText());
        //txtCardinalityFrom.setText(relationGUI.getCardinalityByFromClass().getText());
        //txtCardinalityTo.setText(relationGUI.getCardinalityByToClass().getText());
    }

    /**initialize dialog*/
    public void init(RelationGUI relationGUI, Pane canvas, ClassDiagram classDiagram){
        this.relationGUI = relationGUI;
        this.canvas = canvas;
        this.classDiagram= classDiagram;
        cmbRelationType.getItems().addAll("ASSOCIATION", "AGGREGATION", "GENERALIZATION", "COMPOSITION");
        cmbRelationType.getSelectionModel().selectFirst();

    }

    /**saving current state for each textbox and cmbbox as relation new attributes*/
    public void btnSave(ActionEvent e) {
        //change relation attributes

        //change name
        this.relationGUI.setNameOfRelation(txtName.getText());
        //cardinalities
        if ((txtCardinalityFrom.getText().trim().matches("[0-9]+[\\.][\\.][0-9]+|[0-9]+[\\.][\\.][\\*]|[\\*]|[0-9]+") &&
                txtCardinalityTo.getText().trim().matches("[0-9]+[\\.][\\.][0-9]+|[0-9]+[\\.][\\.][\\*]|[\\*]|[0-9]+"))
                || (txtCardinalityFrom.getText().equals("") && txtCardinalityTo.getText().equals(""))) {

            relationGUI.setCardinalityByFromClass(txtCardinalityFrom.getText().trim());
            relationGUI.setCardinalityByToClass(txtCardinalityTo.getText().trim());
        }

        //change relation
        UMLRelation.RelationType relType = UMLRelation.RelationType.ASSOCIATION; //default relation

        switch (cmbRelationType.getSelectionModel().getSelectedItem().toString()){
            case "ASSOCIATION": relType = UMLRelation.RelationType.ASSOCIATION; break;
            case "AGGREGATION": relType = UMLRelation.RelationType.AGGREGATION; break;
            case "GENERALIZATION": relType = UMLRelation.RelationType.GENERALIZATION; break;
            case "COMPOSITION": relType = UMLRelation.RelationType.COMPOSITION; break;
        }
        this.relationGUI.setRelationType(relType);
        this.relationGUI.setNewRelLineEndPosition();

        //then it is possible to close the window
        closeStage(e);
    }

    /**deletion from space*/
    public void btnDelete(ActionEvent e){
        //change all colored operations in when it was generalization relation
        relationGUI.setRelationType(UMLRelation.RelationType.ASSOCIATION);

        //remove relation from class diagram
        this.classDiagram.removeRelation(this.relationGUI.getUmlRelation());

        //remove relation references
        this.relationGUI.getRelClassToGUI().getListOfRelations().remove(this.relationGUI);
        this.relationGUI.getRelClassFromGUI().getListOfRelations().remove(this.relationGUI);

        //remove relation from canvas
        this.relationGUI.removeFromCanvas();

        closeStage(e);

    }

    /**
     * method for closing this stage - from */
    private void closeStage(ActionEvent event) {
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
