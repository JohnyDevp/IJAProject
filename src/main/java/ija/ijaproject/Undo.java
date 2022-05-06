package ija.ijaproject;

import ija.ijaproject.cls.ClassDiagram;
import ija.ijaproject.cls.UMLAttribute;
import ija.ijaproject.cls.UMLClass;
import ija.ijaproject.cls.UMLOperation;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * 
 * Represent Undo class used to undo operations
 * 
 *
 * @author xzimol04, xholan11
 * @version 1.1
 */
public class Undo {
    public enum UndoOperation {
        RENAMEOBJECT, ADDATTRIBUTE, ADDOPERATION, REMOVEATTRIBUTE, REMOVEOPERATION
    };

    // type of operation to be undone
    private UndoOperation operationType;

    // gui object where the undo operation will take place
    // gui methods for adding also handling adding to inner representation of object
    // (either umlclass or umlinterface)
    private GUIClassInterfaceTemplate guiObject;

    private ClassDiagram classDiagram;

    private Pane canvas;

    // variables storing the old values (which will be reset to current objects if
    // undo)
    private String prevObjectName = null;
    private UMLOperation umlOperation = null;
    private UMLAttribute umlAttribute = null;

    /**
     * getter of undo operation type
     *
     * @return a {@link ija.ijaproject.Undo.UndoOperation} object
     */
    public UndoOperation getOperationType() {
        return operationType;
    }

    /**
     * getter of object
     *
     * @return a {@link ija.ijaproject.GUIClassInterfaceTemplate} object
     */
    public GUIClassInterfaceTemplate getGuiObject() {
        return guiObject;
    }

    /**
     * constructor - renaming object
     *
     * @param operationType  a {@link ija.ijaproject.Undo.UndoOperation} object
     * @param canvas         a {@link javafx.scene.layout.Pane} object
     * @param classDiagram   a {@link ija.ijaproject.cls.ClassDiagram} object
     * @param guiObject      a {@link ija.ijaproject.GUIClassInterfaceTemplate}
     *                       object
     * @param prevObjectName a {@link java.lang.String} object
     */
    public Undo(UndoOperation operationType, Pane canvas, ClassDiagram classDiagram,
            GUIClassInterfaceTemplate guiObject, String prevObjectName) {
        this.operationType = operationType;
        this.guiObject = guiObject;
        this.prevObjectName = prevObjectName;
        this.classDiagram = classDiagram;
        this.canvas = canvas;
    }

    /**
     * constructor - adding or removing attribute
     *
     * @param operationType a {@link ija.ijaproject.Undo.UndoOperation} object
     * @param canvas        a {@link javafx.scene.layout.Pane} object
     * @param classDiagram  a {@link ija.ijaproject.cls.ClassDiagram} object
     * @param guiObject     a {@link ija.ijaproject.GUIClassInterfaceTemplate}
     *                      object
     * @param umlAttribute  a {@link ija.ijaproject.cls.UMLAttribute} object
     */
    public Undo(UndoOperation operationType, Pane canvas, ClassDiagram classDiagram,
            GUIClassInterfaceTemplate guiObject, UMLAttribute umlAttribute) {
        this.operationType = operationType;
        this.guiObject = guiObject;
        this.umlAttribute = umlAttribute;
        this.classDiagram = classDiagram;
        this.canvas = canvas;
    }

    /**
     * constructor - adding or removing operation
     *
     * @param operationType a {@link ija.ijaproject.Undo.UndoOperation} object
     * @param canvas        a {@link javafx.scene.layout.Pane} object
     * @param classDiagram  a {@link ija.ijaproject.cls.ClassDiagram} object
     * @param guiObject     a {@link ija.ijaproject.GUIClassInterfaceTemplate}
     *                      object
     * @param umlOperation  a {@link ija.ijaproject.cls.UMLOperation} object
     */
    public Undo(UndoOperation operationType, Pane canvas, ClassDiagram classDiagram,
            GUIClassInterfaceTemplate guiObject, UMLOperation umlOperation) {
        this.operationType = operationType;
        this.guiObject = guiObject;
        this.umlOperation = umlOperation;
        this.classDiagram = classDiagram;
        this.canvas = canvas;
    }

    /**
     * function for realize the undo operation
     * here is decided which operation is going to take action
     *
     * @return whether the operation can be undone or not
     */
    public boolean doUndo() {
        // check whether the object gui still exists in diagram
        if (this.classDiagram.findObject(this.guiObject.getUmlObject()) == null) {
            return false;
        }

        switch (this.operationType) {
            case RENAMEOBJECT:
                return doUndoRenameObject();
            case ADDATTRIBUTE:
                return doUndoAddRemovedAttribute();
            case REMOVEATTRIBUTE:
                return doUndoRemoveAddedAttribute();
            case ADDOPERATION:
                return doUndoAddRemovedOperation();
            case REMOVEOPERATION:
                return doUndoRemoveAddedOperation();
        }

        return false; // when operation is unknown
    }

    /**
     * method for renaming object
     * 
     * @return if operation could have been done
     */
    private boolean doUndoRenameObject() {
        // check if there isnt ant other class with desired name
        UMLClass tmp = this.classDiagram.createClass(prevObjectName);
        if (tmp != null) {
            // if not then delete the tmp class (testing the name) and set the name
            this.classDiagram.deleteClass(tmp);
            // set the new name for inner representation
            this.guiObject.getUmlObject().setName(prevObjectName);
            // set the name of label representing object name
            this.guiObject.getClassNameLabel().setText(prevObjectName);

            // resize the object gui
            this.guiObject.resizeObjectGUI();

            return true;
        }
        return false;
    }

    /**
     * method for adding attribute, that has been removed
     * 
     * @return if operation could have been done
     */
    private boolean doUndoAddRemovedAttribute() {
        // take the attribute gui and add it on canvas iff could be created
        Text newAttr = ((ClassObjectGUI) guiObject).addAttribute(this.umlAttribute);

        if (newAttr != null)
            this.canvas.getChildren().add(newAttr);
        else
            return false;

        return true;
    }

    /**
     * method for removing attribute, that has been added
     * 
     * @return if operation could have been done
     */
    private boolean doUndoRemoveAddedAttribute() {
        Text attributeText = ((ClassObjectGUI) guiObject).getMapOfAttributes().get(this.umlAttribute);
        if (attributeText == null)
            return false; // if the attribute doesnt exist then return fail
        // remove from canvas
        this.canvas.getChildren().remove(attributeText);
        // remove attribute's representations
        ((ClassObjectGUI) guiObject).removeAttribute(this.umlAttribute);
        return true;
    }

    /**
     * method for adding operation, that has been removed
     * 
     * @return if operation could have been done
     */
    private boolean doUndoAddRemovedOperation() {
        // try to add operation to class and according to success add it to cmbbox
        Text txtOpr = this.guiObject.addOperation(this.umlOperation);
        if (txtOpr == null)
            return false; // fail - already exists
        else {
            // add it to canvas
            this.canvas.getChildren().add(txtOpr);
        }
        return true;
    }

    /**
     * method for removing operation, that has been previously addded
     * 
     * @return if operation could have been done
     */
    private boolean doUndoRemoveAddedOperation() {
        Text operationText = guiObject.getMapOfOperations().get(this.umlOperation);
        if (operationText == null)
            return false; // if the operation doesnt exist then return fail

        // remove from canvas
        this.canvas.getChildren().remove(operationText);
        // remove attribute's representations
        guiObject.removeOperation(umlOperation);
        return true;
    }

}
