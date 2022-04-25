package ija.ijaproject;

import ija.ijaproject.cls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * controller for gui for sequence diagram
 * @author xzimol04
 * */
public class SequenceDiagramController {

    /**variable storing all the objects taking part in this diagram*/
    private List<SequenceObjectGUI> sequenceObjectGUIList = new ArrayList<>();

    /**variable storing all the relations taking part in this diagram*/
    private List<SequenceRelationGUI> sequenceRelationGUIList = new ArrayList<>();

    /**variable storing the intern representation of this sequence diagram*/
    private SequenceDiagram sequenceDiagram = new SequenceDiagram("");

    /**variable storing class diagram*/
    private ClassDiagram classDiagram = null;

    /**
     * variable storing tab where this diagram is stored
     * */
    private Tab tab;

    /**
     * variable for tabPane reference from main controller
     * */
    private TabPane tabPane;

    @FXML
    public Pane canvas;
    @FXML
    public Button btnAddClass;
    @FXML
    public Button btnAddMessage;
    @FXML
    public Button btnDeleteClass;
    @FXML
    public Button btnDeleteMessage;

    /*setters/getters============================================================================================================================================*/

    /**
     * setter
     * @param tabPane reference of the tab where the diagram has been drawed
     * */
    public final void setTabPane(TabPane tabPane){
        this.tabPane = tabPane;
    }

    /**
     * setter
     * @param tab reference of the tab where the diagram has been drawed
     * */
    public final void setTab(Tab tab){
        this.tab = tab;
    }

    /**
     * getter
     * @return reference of the tab where diagram has been drawed
     * */
    public final Tab getTab() {return this.tab; }

    /**
     * getter
     * @return reference of the tabPane
     * */
    public final TabPane getTabPane() {return this.tabPane; }

    /*============================================================================================================================================*/


    /**
     * override method handling action when button close pressed
     * ensuring properly closed file and closing tab (from another function)
     * */
    public void btnClose(){
        //TODO prompt for exiting if not saved
        //TODO properly close file

        //remove tab of this diagram from tabPane
        getTabPane().getTabs().remove(getTab());

    }

    /**
     * initial method
     * it is called from ClassDiagramController
     * */
    public void init(SequenceDiagram sequenceDiagram, ClassDiagram classDiagram){
        this.sequenceDiagram = sequenceDiagram;
        this.classDiagram = classDiagram;
    }

    /**
     * method handling adding class
     * */
    @FXML
    public void btnAddClass(ActionEvent event){
        //create dialog for choose which object to add
        //set choices list
        List<String> classesList = new ArrayList<>();
        //loop through objects and add to the list only classes
        for (UMLClassInterfaceTemplate umlObject : this.classDiagram.getUmlObjectsList()){
            if (umlObject.getClass() == UMLClass.class){
                classesList.add(umlObject.getName()); //if class then add to the class list
            }
        }

        //if the class list is empty - warning and return
        if (classesList.size() == 0) {
            Errors.showAlertDialog("Firstly create classes which can be added!", Alert.AlertType.WARNING);
            return;
        }

        //create dialog itself
        ChoiceDialog<String> dialog = new ChoiceDialog<>(classesList.get(0).toString(),classesList);
        dialog.setTitle("Choose class");
        Optional<String> result = dialog.showAndWait();

        UMLClass chosenUmlClass = null;

        //set the class name
        if (result.isPresent()){
             String chosenClass = result.get().toString();
             //loop through names of classes and set the umlClass when found
             for (UMLClassInterfaceTemplate umlObject : this.classDiagram.getUmlObjectsList()){
                 if (umlObject.getName().equals(chosenClass)){
                     chosenUmlClass = (UMLClass) umlObject;
                 }
             }
        }

        //validation test
        if (chosenUmlClass == null){
            return;
        }

        //create sequence uml class
        //-1 coord says that it is not set yet
        UMLSeqClass umlSeqClass = new UMLSeqClass(chosenUmlClass, 0.0);

        //create object gui
        SequenceObjectGUI sequenceObjectGUI = new SequenceObjectGUI(umlSeqClass, this.sequenceDiagram, this.canvas);
        this.sequenceObjectGUIList.add(sequenceObjectGUI);
        //create its gui
        sequenceObjectGUI.createGUI();
        //add on canvas
        addClassOnCanvasAndSetActions(sequenceObjectGUI);

    }

    public void btnAddMessage(){

    }

    public void btnDeleteClass(){

    }

    public void btnDeleteMessage(){

    }

    /*=================================================================================================================*/
    SequenceObjectGUI selectedObject = null; //variable storing currently selected object
    private Double mouseX = 0.0;

    /**
     * method for adding newly added class on canvas and
     * */
    private void addClassOnCanvasAndSetActions(SequenceObjectGUI sequenceObjectGUI){
        //add object on gui
        canvas.getChildren().addAll(
                sequenceObjectGUI.getObjBackground(),
                sequenceObjectGUI.getObjectTimeLine(),
                sequenceObjectGUI.getObjNameText()
        );

        //set actions
        sequenceObjectGUI.getObjBackground().setOnMouseClicked(mouseEvent -> {
            //change color of the previously selected object
            if (this.selectedObject != null){
                this.selectedObject.getObjBackground().setFill(Color.LIGHTGRAY);
                if (sequenceObjectGUI == this.selectedObject){
                    this.selectedObject = null;
                    return;
                }
            } else {
                //set new selected object
                this.selectedObject = sequenceObjectGUI;
                sequenceObjectGUI.getObjBackground().setFill(Color.ORANGE);
            }

        });

        sequenceObjectGUI.getObjBackground().setOnMousePressed(mouseEvent -> {
            this.mouseX = mouseEvent.getX();
        });

        sequenceObjectGUI.getObjBackground().setOnMouseDragged(mouseEvent -> {
            Double diffX = mouseEvent.getX() - this.mouseX; //compute the movement difference
            //reset previous mouse X coord
            this.mouseX = mouseEvent.getX();

            //test for not taking class object outside the canvas
            if (sequenceObjectGUI.getObjBackground().getX() + diffX <= 0) { return; }

            //move all parts of the class

            //background
            sequenceObjectGUI.getObjBackground().setX(sequenceObjectGUI.getObjBackground().getX() + diffX);
            //label
            sequenceObjectGUI.getObjNameText().setX(sequenceObjectGUI.getObjNameText().getX() + diffX);
            //line
            sequenceObjectGUI.getObjectTimeLine().setStartX(sequenceObjectGUI.getObjectTimeLine().getStartX() + diffX);
            sequenceObjectGUI.getObjectTimeLine().setEndX(sequenceObjectGUI.getObjectTimeLine().getEndX() + diffX);

            //move all messages
            for (SequenceRelationGUI sequenceRelationGUI : sequenceObjectGUI.getSequenceRelationGUIList()){
                //first choose type of message

            }


        });

        sequenceObjectGUI.getObjectTimeLine().setOnMouseClicked(mouseEvent -> {
            //todo - raise dailog => for creation of new message

        });
    }
}
