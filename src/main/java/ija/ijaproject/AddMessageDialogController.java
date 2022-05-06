package ija.ijaproject;

import ija.ijaproject.cls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * <p>
 * AddMessageDialogController class.
 * </p>
 *
 * @author musta-pollo
 * @version 1.1
 */
public class AddMessageDialogController {
    @FXML
    private ComboBox cmbMessageClasses;

    @FXML
    private ComboBox cmbMessageOperations;

    @FXML
    private ComboBox cmbMessageTypes;

    @FXML
    private Button btnAddMessage;

    @FXML
    private CheckBox checkBoxDeactivateSender;

    @FXML
    private CheckBox checkBoxDeactivateReceiver;

    @FXML
    private CheckBox checkBoxAddReturnMessage;

    /** variable storing all the objects taking part in this diagram */
    private SequenceDiagram sequenceDiagram;

    private Boolean dataValid = false;
    private UMLSeqClass seqClassReceiver;
    private UMLOperation messageOperation;
    private Message.MessageType messageType;

    /**
     * getter
     *
     * @return a {@link ija.ijaproject.cls.Message.MessageType} object
     */
    public Message.MessageType getMessageType() {
        return messageType;
    }

    /**
     * getter
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getDataValid() {
        return dataValid;
    }

    /**
     * getter
     *
     * @return a {@link ija.ijaproject.cls.UMLSeqClass} object
     */
    public UMLSeqClass getSeqClassReceiver() {
        return seqClassReceiver;
    }

    /**
     * getter
     *
     * @return a {@link ija.ijaproject.cls.UMLOperation} object
     */
    public UMLOperation getMessageOperation() {
        return messageOperation;
    }

    /**
     * getter
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getDeactivateSender() {
        return this.checkBoxDeactivateSender.isSelected();
    }

    /**
     * getter
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getDeactivateReceiver() {
        return this.checkBoxDeactivateReceiver.isSelected();
    }

    /**
     * getter
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getAddReturnMessage() {
        return this.checkBoxAddReturnMessage.isSelected();
    }

    /**
     * <p>
     * init.
     * </p>
     *
     * @param sequenceDiagram a {@link ija.ijaproject.cls.SequenceDiagram} object
     */
    public void init(SequenceDiagram sequenceDiagram) {
        // set all the classes which exists in diagram
        this.sequenceDiagram = sequenceDiagram;
        // load them to the combobox
        for (UMLSeqClass umlSeqClass : sequenceDiagram.getListOfObjectsParticipants()) {
            this.cmbMessageClasses.getItems().add(umlSeqClass.getName());
        }

        this.cmbMessageClasses.getSelectionModel().selectFirst();

        // load operations
        // get selected class
        String selectedItem = (String) this.cmbMessageClasses.getSelectionModel().getSelectedItem();

        // find the selected class
        for (UMLSeqClass umlSeqClass : sequenceDiagram.getListOfObjectsParticipants()) {
            if (umlSeqClass.getName().equals(selectedItem)) {
                // loop through class operations
                for (UMLOperation umlOperation : umlSeqClass.getUmlClass().getUmlOperationList()) {
                    // add only public operations
                    if (umlOperation.getModifier().equals('+')) {
                        // create operation text
                        StringBuilder textOfOperation = new StringBuilder(
                                umlOperation.getModifier() + umlOperation.getName() + " (");
                        for (UMLAttribute umlParam : umlOperation.getParametersOfOperationList()) {
                            textOfOperation.append(umlParam.getName()).append(" : ").append(umlParam.getType());
                        }
                        textOfOperation.append(") : ").append(umlOperation.getType());

                        // add it to the combobox
                        this.cmbMessageOperations.getItems().add(textOfOperation.toString());
                    }
                }
            }
        }
        this.cmbMessageOperations.getSelectionModel().selectFirst();

        if (this.cmbMessageOperations.getItems().size() == 0) {
            this.btnAddMessage.setDisable(true);
        } else {
            this.btnAddMessage.setDisable(false);
        }

        // load types of message
        cmbMessageTypes.getItems().addAll(
                Message.MessageType.ASYNC,
                Message.MessageType.SYNC,
                Message.MessageType.CREATE,
                Message.MessageType.DESTROY);

        // set selected first items in comboboxes
        this.cmbMessageTypes.getSelectionModel().selectFirst();

    }

    /**
     * <p>
     * cmbMessageClassesOnAction.
     * </p>
     *
     * @param e a {@link javafx.event.ActionEvent} object
     */
    public void cmbMessageClassesOnAction(ActionEvent e) {
        // clear the combobox with operations
        this.cmbMessageOperations.getItems().clear();

        // get selected class
        String selectedItem = (String) this.cmbMessageClasses.getSelectionModel().getSelectedItem();

        // find the selected class
        for (UMLSeqClass umlSeqClass : sequenceDiagram.getListOfObjectsParticipants()) {
            if (umlSeqClass.getName().equals(selectedItem)) {
                // loop through class operations
                for (UMLOperation umlOperation : umlSeqClass.getUmlClass().getUmlOperationList()) {
                    // add only public operations
                    if (umlOperation.getModifier().equals('+')) {
                        // create operation text
                        StringBuilder textOfOperation = new StringBuilder(
                                umlOperation.getModifier() + umlOperation.getName() + " (");
                        for (UMLAttribute umlParam : umlOperation.getParametersOfOperationList()) {
                            textOfOperation.append(umlParam.getName()).append(" : ").append(umlParam.getType());
                        }
                        textOfOperation.append(") : ").append(umlOperation.getType());

                        // add it to the combobox
                        this.cmbMessageOperations.getItems().add(textOfOperation.toString());
                    }
                }
            }
        }

        this.cmbMessageOperations.getSelectionModel().selectFirst();
        if (this.cmbMessageOperations.getItems().size() == 0) {
            this.btnAddMessage.setDisable(true);
        } else {
            this.btnAddMessage.setDisable(false);
        }
    }

    /**
     * <p>
     * btnAddMessage.
     * </p>
     *
     * @param e a {@link javafx.event.ActionEvent} object
     */
    public void btnAddMessage(ActionEvent e) {
        // get selected class and operation
        String selectedClass = (String) this.cmbMessageClasses.getSelectionModel().getSelectedItem();
        String selectedOperation = (String) this.cmbMessageOperations.getSelectionModel().getSelectedItem();

        // set the currently chosen type
        this.messageType = (Message.MessageType) this.cmbMessageTypes.getSelectionModel().getSelectedItem();

        // find the selected class
        for (UMLSeqClass umlSeqClass : sequenceDiagram.getListOfObjectsParticipants()) {
            if (umlSeqClass.getName().equals(selectedClass)) {
                // loop through class operations
                for (UMLOperation umlOperation : umlSeqClass.getUmlClass().getUmlOperationList()) {
                    // compare only public operations
                    if (umlOperation.getModifier().equals('+')) {
                        // create operation text
                        StringBuilder textOfOperation = new StringBuilder(
                                umlOperation.getModifier() + umlOperation.getName() + " (");
                        for (UMLAttribute umlParam : umlOperation.getParametersOfOperationList()) {
                            textOfOperation.append(umlParam.getName()).append(" : ").append(umlParam.getType());
                        }
                        textOfOperation.append(") : ").append(umlOperation.getType());

                        // compare with the selected operation
                        if (selectedOperation.equals(textOfOperation.toString())) {
                            this.messageOperation = umlOperation;
                            break;
                        }
                    }
                }
                // set selected class
                this.seqClassReceiver = umlSeqClass;
                break;
            }
        }
        this.dataValid = true;

        // close dialog
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

}
