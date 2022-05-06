package ija.ijaproject;

import ija.ijaproject.cls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * controller for gui for sequence diagram
 *
 * @author xzimol04, xholan11
 * @version 1.1
 */
public class SequenceDiagramController {

    /** variable storing all the objects taking part in this diagram */
    private List<SequenceObjectGUI> sequenceObjectGUIList = new ArrayList<>();

    /** variable storing all the relations taking part in this diagram */
    private List<SequenceMessageGUI> sequenceMessageGUIList = new ArrayList<>();

    /** variable storing the intern representation of this sequence diagram */
    private SequenceDiagram sequenceDiagram = new SequenceDiagram("");

    /** variable storing class diagram */
    private ClassDiagram classDiagram = null;

    /**
     * variable storing tab where this diagram is stored
     */
    private Tab tab;

    /**
     * variable for tabPane reference from main controller
     */
    private TabPane tabPane;

    @FXML
    public Pane canvas;
    @FXML
    public Button btnAddClass;
    @FXML
    public Button btnDeleteClass;
    @FXML
    public Button btnDeleteMessage;

    /*
     * setters/getters==============================================================
     * =============================================================================
     * =
     */

    /**
     * setter
     *
     * @param tabPane reference of the tab where the diagram has been drawed
     */
    public final void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    /**
     * setter
     *
     * @param tab reference of the tab where the diagram has been drawed
     */
    public final void setTab(Tab tab) {
        this.tab = tab;
    }

    /**
     * getter
     *
     * @return reference of the tab where diagram has been drawed
     */
    public final Tab getTab() {
        return this.tab;
    }

    /**
     * getter
     *
     * @return reference of the tabPane
     */
    public final TabPane getTabPane() {
        return this.tabPane;
    }

    /*
     * =============================================================================
     * ===============================================================
     */

    /**
     * override method handling action when button close pressed
     * ensuring properly closed file and closing tab (from another function)
     */
    public void btnClose() {
        // TODO prompt for exiting if not saved
        // TODO properly close file

        // remove tab of this diagram from tabPane
        getTabPane().getTabs().remove(getTab());

    }

    /**
     * initial method
     * it is called from ClassDiagramController
     *
     * @param sequenceDiagram a {@link ija.ijaproject.cls.SequenceDiagram} object
     * @param classDiagram    a {@link ija.ijaproject.cls.ClassDiagram} object
     */
    public void init(SequenceDiagram sequenceDiagram, ClassDiagram classDiagram) {
        this.sequenceDiagram = sequenceDiagram;
        this.classDiagram = classDiagram;
    }

    /**
     * method handling adding class
     *
     * @param event a {@link javafx.event.ActionEvent} object
     */
    @FXML
    public void btnAddClass(ActionEvent event) {
        // create dialog for choose which object to add
        // set choices list
        ArrayList<String> classesList = new ArrayList<>();
        // loop through objects and add to the list only classes
        for (UMLClassInterfaceTemplate umlObject : this.classDiagram.getUmlObjectsList()) {
            if (umlObject.getClass() == UMLClass.class) {

                boolean exists = false;
                // Remove all classes that are already instanciated
                for (UMLSeqClass seqClass : this.sequenceDiagram.listOfObjectsParticipants) {
                    if (seqClass.umlClass.name == umlObject.name)
                        exists = true;
                }
                if (!exists)
                    classesList.add(umlObject.getName()); // if class then add to the class list
            }
        }

        // if the class list is empty - warning and return
        if (classesList.size() == 0) {
            Errors.showAlertDialog("Firstly create classes which can be added!", Alert.AlertType.WARNING);
            return;
        }

        // create dialog for choosing class desired to add
        ChoiceDialog<String> dialog = new ChoiceDialog<>(classesList.get(0).toString(), classesList);
        dialog.setTitle("Choose class");
        Optional<String> result = dialog.showAndWait();

        UMLClass chosenUmlClass = null;

        // found related umlClass to the chosen name
        if (result.isPresent()) {
            String chosenClass = result.get().toString();
            // loop through names of classes and set the umlClass when found
            for (UMLClassInterfaceTemplate umlObject : this.classDiagram.getUmlObjectsList()) {
                if (umlObject.getName().equals(chosenClass)) {
                    chosenUmlClass = (UMLClass) umlObject;
                }
            }
        }

        // validation test - if umlClass do exist (just so)
        if (chosenUmlClass == null) {
            return;
        }

        // create sequence uml class
        // -1 coord says that it is not set yet
        UMLSeqClass umlSeqClass = new UMLSeqClass(chosenUmlClass, 0.0);

        // add it to the diagram
        this.sequenceDiagram.addObject(umlSeqClass);

        // create object gui
        SequenceObjectGUI sequenceObjectGUI = new SequenceObjectGUI(umlSeqClass, this.sequenceDiagram, this.canvas);
        this.sequenceObjectGUIList.add(sequenceObjectGUI);
        // create its gui
        sequenceObjectGUI.createGUI();
        // add on canvas
        addClassOnCanvasAndSetActions(sequenceObjectGUI);

    }

    /**
     * object deleting actions - button click handling
     *
     * @param e a {@link javafx.event.ActionEvent} object
     */
    public void btnDeleteClass(ActionEvent e) {
        if (this.selectedObject == null) {
            return;
        }

        // notify all its related messages
        for (SequenceMessageGUI sequenceMessageGUI : this.selectedObject.getSendingMessageGUIList()) {
            sequenceMessageGUI.notifyObjectsAboutDeletion("receiver");
            removeMessage(sequenceMessageGUI);
        }

        for (SequenceMessageGUI sequenceMessageGUI : this.selectedObject.getReceivingMessageGUIList()) {
            sequenceMessageGUI.notifyObjectsAboutDeletion("sender");
            removeMessage(sequenceMessageGUI);
        }

        // remove class gui
        this.selectedObject.removeSequenceObjectGui();

        // remove intern class representation - uml seq class
        this.sequenceDiagram.removeObject(this.selectedObject.getUmlSeqClass());

        // remove class from the list of gui objects
        this.sequenceObjectGUIList.remove(this.selectedObject);

        // set currently selected object to null
        this.selectedObject = null;
    }

    /**
     * message deleting actions - button click handling
     *
     * @param e a {@link javafx.event.ActionEvent} object
     */
    public void btnDeleteMessage(ActionEvent e) {
        if (this.selectedMessage == null)
            return;
        else {
            // notify related objects about message deletion
            this.selectedMessage.notifyObjectsAboutDeletion("both");

            removeMessage(this.selectedMessage);
        }
    }

    /*
     * =============================================================================
     * ====================================
     */

    SequenceObjectGUI selectedObject = null; // variable storing currently selected object
    private SequenceMessageGUI selectedMessage = null; // variable for storing currently selected message
    private Double mouseX = 0.0;

    /**
     * method for adding newly added class on canvas and
     */
    private void addClassOnCanvasAndSetActions(SequenceObjectGUI sequenceObjectGUI) {
        // add object on gui
        canvas.getChildren().addAll(
                sequenceObjectGUI.getObjBackground(),
                sequenceObjectGUI.getObjectTimeLine(),
                sequenceObjectGUI.getObjNameText());

        // set actions
        sequenceObjectGUI.getObjBackground().setOnMouseClicked(mouseEvent -> {
            // change color of the previously selected object
            if (this.selectedObject != null) {
                // set as unselected the previously selected object
                this.selectedObject.getObjBackground().setFill(Color.LIGHTGRAY);
                // if the previously selected object was this object, then return (-> select
                // "nothing")
                if (sequenceObjectGUI == this.selectedObject) {
                    this.selectedObject = null;
                    return;
                } else {
                    // otherwise set as newly selected object this object
                    this.selectedObject = sequenceObjectGUI;
                    sequenceObjectGUI.getObjBackground().setFill(Color.ORANGE);
                }
            } else {
                // nothing to unselect
                // set new selected object for this object
                this.selectedObject = sequenceObjectGUI;
                sequenceObjectGUI.getObjBackground().setFill(Color.ORANGE);
            }

        });

        sequenceObjectGUI.getObjBackground().setOnMousePressed(mouseEvent -> {
            // set the current mouse position for the next possibility of making difference
            this.mouseX = mouseEvent.getX();
        });

        sequenceObjectGUI.getObjBackground().setOnMouseDragged(mouseEvent -> {
            Double diffX = mouseEvent.getX() - this.mouseX; // compute the movement difference
            // reset previous mouse X coord
            this.mouseX = mouseEvent.getX();

            // notify the object about moving
            sequenceObjectGUI.moveObject(diffX);
        });

        sequenceObjectGUI.getObjectTimeLine().setOnMouseClicked(mouseEvent -> {
            // if the click wasn't right-click then return
            if (mouseEvent.getButton() != MouseButton.SECONDARY) {
                return;
            }

            // show creating dialog
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/addMessageDialog_view.fxml"));
            Parent parent = null;
            try {
                parent = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AddMessageDialogController dlgController = fxmlLoader.<AddMessageDialogController>getController();
            dlgController.init(this.sequenceDiagram);

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            // if not valid data has been chosen - return
            if (!dlgController.getDataValid()) {
                return;
            }

            // create message
            Message newMsg = this.sequenceDiagram.createMessage(mouseEvent.getY(), sequenceObjectGUI.getUmlSeqClass(),
                    dlgController.getSeqClassReceiver(), dlgController.getMessageOperation(),
                    dlgController.getMessageType());
            // set if newMsg activate or deactivate the related objects
            newMsg.setReceiverDeactivation(dlgController.getDeactivateReceiver());
            newMsg.setSenderDeactivation(dlgController.getDeactivateSender());

            // find gui of receiver
            SequenceObjectGUI guiReceiverObject = null;
            for (SequenceObjectGUI guiObj : this.sequenceObjectGUIList) {
                if (guiObj.getUmlSeqClass() == dlgController.getSeqClassReceiver()) {
                    guiReceiverObject = guiObj;
                }
            }

            // create gui for this message
            // and set messageGui reference to its related objects
            if (guiReceiverObject != null) {
                SequenceMessageGUI newMessageGui = new SequenceMessageGUI(this.sequenceDiagram, newMsg,
                        sequenceObjectGUI, guiReceiverObject, this.canvas);
                // add message to the list of messages
                this.sequenceMessageGUIList.add(newMessageGui);
                // add to related objects
                sequenceObjectGUI.addSendingMessageGui(newMessageGui);
                guiReceiverObject.addReceivingMessageGui(newMessageGui);
                // set new message actions
                setMessageActions(newMessageGui);

                // add return message if selected
                if (dlgController.getAddReturnMessage()) {
                    // create message
                    Message returnMsg = this.sequenceDiagram.createReturnMessage(mouseEvent.getY(),
                            dlgController.getSeqClassReceiver(), sequenceObjectGUI.getUmlSeqClass(), "return");
                    returnMsg.setReceiverDeactivation(false);
                    returnMsg.setSenderDeactivation(true);
                    SequenceMessageGUI returnMessageGui = new SequenceMessageGUI(this.sequenceDiagram, returnMsg,
                            guiReceiverObject, sequenceObjectGUI, this.canvas);

                    // add to related objects
                    sequenceObjectGUI.addReceivingMessageGui(returnMessageGui);
                    guiReceiverObject.addSendingMessageGui(returnMessageGui);
                    // set new message actions
                    setMessageActions(returnMessageGui);
                }
            }

        });
    }

    /**
     * private method for setting actions for message
     */
    private void setMessageActions(SequenceMessageGUI sequenceMessageGUI) {
        sequenceMessageGUI.getMessageLine().setOnMouseClicked(mouseEvent -> {
            // change color of the previously selected object
            if (this.selectedMessage != null) {
                this.selectedMessage.getMessageLine().setStroke(Color.BLACK);
                if (sequenceMessageGUI == this.selectedMessage) {
                    this.selectedMessage = null;
                    return;
                }
            } else {
                // set new selected object
                this.selectedMessage = sequenceMessageGUI;
                sequenceMessageGUI.getMessageLine().setStroke(Color.ORANGE);
            }
        });
    }

    /**
     * private function for removing message - shared part used when deleting object
     * and deleting message itself
     * 
     * @param sequenceMessageGUI
     */
    private void removeMessage(SequenceMessageGUI sequenceMessageGUI) {

        // remove all graphical parts of the message
        sequenceMessageGUI.removeMessageGui();

        // remove message internal representation
        this.sequenceDiagram.deleteMessage(sequenceMessageGUI.getUmlMessage());

        // remove it from the list
        this.sequenceMessageGUIList.remove(sequenceMessageGUI);

        // if this message is currently selected then set currently selected message to
        // null
        if (this.selectedMessage == sequenceMessageGUI) {
            this.selectedMessage = null;
        }
    }

    /**
     * this function updating all elements in this diagram
     */
    public void updateDiagram() {
        // update objects - classes
        for (SequenceObjectGUI sequenceObjectGUI : sequenceObjectGUIList) {
            UMLClassInterfaceTemplate umlClassInterfaceTemplate = classDiagram
                    .findObject(sequenceObjectGUI.getUmlSeqClass().getUmlClass().getName());
            // if it is class
            if (umlClassInterfaceTemplate != null && umlClassInterfaceTemplate.getClass() != UMLInterface.class) {
                // class found - but there has to be another check, if the name fit -> will be
                // implicitly reset in setExistsInClassDiagram function

                // check if the class doesnt exist so far (now exists)
                if (sequenceObjectGUI.getExistsInClassDiagram()) { // if it has existed already
                    sequenceObjectGUI.setExistsInClassDiagram(true, null);
                } else {
                    // create new umlseqclass and add it to the class diagram and update this seq
                    // class object
                    UMLSeqClass umlSeqClass = new UMLSeqClass((UMLClass) umlClassInterfaceTemplate,
                            sequenceObjectGUI.getUmlSeqClass().getXcoord());
                    sequenceObjectGUI.setExistsInClassDiagram(true, umlSeqClass);
                }
            } /*
               * else if (umlClassInterfaceTemplate == null &&
               * sequenceObjectGUI.getUmlSeqClass().getName().equals("*")) {
               * //this is the possibility, when the name doesnt exist when loading file
               * UMLClassInterfaceTemplate umlClassInterfaceTemplate =
               * classDiagram.findObject(sequenceObjectGUI.getUmlSeqClass().getUmlClass().
               * getName());
               * }
               */
            else {
                // class name not found
                sequenceObjectGUI.setExistsInClassDiagram(false, null);
            }
        }

        // update messages
        /*
         * for (SequenceMessageGUI sequenceMessageGUI : sequenceMessageGUIList){
         * //firstly found whether exists the class containing this message
         * UMLClass msgReceiverOld =
         * sequenceMessageGUI.getUmlMessage().getClassReceiver().getUmlClass();
         * UMLClassInterfaceTemplate msgReceiverCurrent =
         * classDiagram.findObject(msgReceiverOld.getName());
         * 
         * if (msgReceiverCurrent != null && msgReceiverCurrent.getClass() ==
         * UMLClass.class){
         * //check whether the operation itself exists in the class
         * 
         * } else {
         * //class doesnt exist
         * sequenceMessageGUI.setExistsInClassDiagram(false);
         * }
         * 
         * }
         */

    }
}
