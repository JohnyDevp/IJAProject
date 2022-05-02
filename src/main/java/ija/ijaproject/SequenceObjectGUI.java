package ija.ijaproject;

import ija.ijaproject.cls.Message;
import ija.ijaproject.cls.SequenceDiagram;
import ija.ijaproject.cls.UMLSeqClass;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SequenceObjectGUI {
    //implicitly set Y for each object (not message)
    private static final int YCOORD_BEGIN = 30;

    //if this object exists also in class diagram
    private Boolean existsInClassDiagram = true;

    //variable storing whether this object is destroyed (value greated then -1) or not (value -1)
    private Double objectDestroyedPosition = -1.0;

    //pane - canvas reference
    private Pane canvas;

    //intern representation of this object
    private UMLSeqClass umlSeqClass;
    private SequenceDiagram sequenceDiagram;

    //gui representation of this object
    private Text objNameText;
    private Rectangle objBackground;
    private Line objectTimeLine;
    private List<Rectangle> timeLineActiveRectangleList = new ArrayList<>();

    //list of all gui relations relevant to this object
    private List<SequenceMessageGUI> receivingMessageGUIList = new ArrayList<>();
    private List<SequenceMessageGUI> sendingMessageGUIList = new ArrayList<>();

    /**getters*/
    public Line getObjectTimeLine() {
        return objectTimeLine;
    }
    /**getter*/
    public Rectangle getObjBackground() {
        return objBackground;
    }
    /**getter*/
    public Text getObjNameText() {
        return objNameText;
    }
    /**getter*/
    public List<SequenceMessageGUI> getSequenceRelationGUIList() {
        return receivingMessageGUIList;
    }
    /**getter*/
    public UMLSeqClass getUmlSeqClass() {
        return umlSeqClass;
    }
    /**getter*/
    public List<SequenceMessageGUI> getReceivingMessageGUIList() {
        return Collections.unmodifiableList(receivingMessageGUIList);
    }
    /**getter*/
    public List<SequenceMessageGUI> getSendingMessageGUIList() {
        return Collections.unmodifiableList(sendingMessageGUIList);
    }
    /**getter*/
    public Double getObjectDestroyedPosition() {
        return objectDestroyedPosition;
    }

    /**constructor*/
    public SequenceObjectGUI(UMLSeqClass umlClass, SequenceDiagram sequenceDiagram, Pane canvas){
        this.umlSeqClass = umlClass;
        this.sequenceDiagram = sequenceDiagram;
        this.canvas = canvas;
    }

    /**method for creating the gui of this object*/
    public void createGUI(){
        //set the graphical representation of text of the object
        Text objName = new Text();
        objName.setText(":"+this.umlSeqClass.getName());

        //compute the width of whole object
        Double objWidth = objName.getLayoutBounds().getWidth() + 10;

        //create objects background
        Rectangle objBackground = new Rectangle();
        objBackground.setWidth(objWidth);
        objBackground.setHeight(40);
        objBackground.setFill(Color.LIGHTGRAY);
        objBackground.setStroke(Color.BLACK);
        //SET X AND Y COORD OF BOX
        objBackground.setX(this.umlSeqClass.getXcoord());
        objBackground.setY(YCOORD_BEGIN);

        //compute half of the background of the object exactly on X axis
        Double halfOnXAxis = objBackground.getWidth() / 2 + objBackground.getX();
        //compute Y position of bottom border of object background
        Double bottomOfBackgroundOnYAxis = objBackground.getY() + objBackground.getHeight();

        //SET TEXT Y,X COORD
        objName.setX(halfOnXAxis - objName.getLayoutBounds().getWidth()/2);
        objName.setY((YCOORD_BEGIN + bottomOfBackgroundOnYAxis) / 2);

        //create object timeline
        Line timeLine = new Line();
        timeLine.setStrokeWidth(5);
        timeLine.setStartX(halfOnXAxis);
        timeLine.setStartY(bottomOfBackgroundOnYAxis);
        timeLine.setEndX(halfOnXAxis);
        timeLine.setEndY(400); //todo - which height of object life-line should be chosen????
        timeLine.getStrokeDashArray().addAll(20d, 10d);

        //set all objects as attributes of this class
        this.objectTimeLine = timeLine;
        this.objBackground = objBackground;
        this.objNameText = objName;
    }

    private void updateObjectName(){
        this.umlSeqClass.setName(this.umlSeqClass.getUmlClass().getName());
        this.objNameText.setText(":"+this.umlSeqClass.getName());

        //compute the width of whole object
        Double objWidth = this.objNameText.getLayoutBounds().getWidth() + 10;

        //reset the width
        objBackground.setWidth(objWidth);

        Double prevLineX = this.objectTimeLine.getStartX();

        //compute half of the background of the object exactly on X axis
        Double halfOnXAxis = objBackground.getWidth() / 2 + objBackground.getX();

        Double diffX = halfOnXAxis - prevLineX;

        //move life-line
        this.objectTimeLine.setStartX(halfOnXAxis);
        this.objectTimeLine.setEndX(halfOnXAxis);

        //move all active areas of this object
        for (Rectangle activeArea : this.timeLineActiveRectangleList){
            activeArea.setX(activeArea.getX() + diffX);
        }

        //move all messages related to this object
        for (SequenceMessageGUI sequenceMessageGUI : this.sendingMessageGUIList){
            sequenceMessageGUI.messageGuiMove();
        }
        for (SequenceMessageGUI sequenceMessageGUI : this.receivingMessageGUIList){
            sequenceMessageGUI.messageGuiMove();
        }
    }

    public void moveObject(Double diffX){
        //test for not taking class object outside the canvas
        if (this.objBackground.getX() + diffX <= 0) { return; }

        //move all parts of the class

        //background
        this.objBackground.setX(this.objBackground.getX() + diffX);
        //label
        this.objNameText.setX(this.objNameText.getX() + diffX);
        //line
        this.objectTimeLine.setStartX(this.objectTimeLine.getStartX() + diffX);
        this.objectTimeLine.setEndX(this.objectTimeLine.getEndX() + diffX);
        //move all active areas of this object
        for (Rectangle activeArea : this.timeLineActiveRectangleList){
            activeArea.setX(activeArea.getX() + diffX);
        }

        //move all messages related to this object
        for (SequenceMessageGUI sequenceMessageGUI : this.sendingMessageGUIList){
            sequenceMessageGUI.messageGuiMove();
        }
        for (SequenceMessageGUI sequenceMessageGUI : this.receivingMessageGUIList){
            sequenceMessageGUI.messageGuiMove();
        }

        //set the new position also in its inner representation
        this.umlSeqClass.setXcoord(this.objBackground.getX());

    }

    public void moveActiveArea(){
        for (Rectangle rect : timeLineActiveRectangleList) {this.canvas.getChildren().remove(rect);}
        this.timeLineActiveRectangleList.clear();

        for (SequenceMessageGUI sequenceMessageGUI : sendingMessageGUIList){
            if (!sequenceMessageGUI.getDeactivateSender()){
                activateObject(sequenceMessageGUI, true);
            }
        }
        for (SequenceMessageGUI sequenceMessageGUI : receivingMessageGUIList){
            if (!sequenceMessageGUI.getDeactivateReceiver()){
                activateObject(sequenceMessageGUI, false);
            }
        }

        for (SequenceMessageGUI sequenceMessageGUI : sendingMessageGUIList){
            if (sequenceMessageGUI.getDeactivateSender()){
                deactivateObject(sequenceMessageGUI);
            }
        }
        for (SequenceMessageGUI sequenceMessageGUI : receivingMessageGUIList){
            if (sequenceMessageGUI.getDeactivateReceiver()){
                deactivateObject(sequenceMessageGUI);
            }
        }
    }

    /**method for adding message related to this object*/
    public void addReceivingMessageGui(SequenceMessageGUI sequenceMessageGUI){
        this.receivingMessageGUIList.add(sequenceMessageGUI);
        //if the message is of type destroy then it is implicitly deactivating
        if (sequenceMessageGUI.getMessageType() == Message.MessageType.DESTROY){
            this.objectDestroyedPosition = sequenceMessageGUI.getMessageLine().getEndY();
            deactivateObject(sequenceMessageGUI);
            return;
        }

        if (sequenceMessageGUI.getDeactivateReceiver()){
            deactivateObject(sequenceMessageGUI);
        } else {activateObject(sequenceMessageGUI, false);}
    }

    public void removeReceivingMessageGui(SequenceMessageGUI sequenceMessageGUI){
        //remove the message
        this.receivingMessageGUIList.remove(sequenceMessageGUI);

        //move all the active-rectangles
        moveActiveArea();
    }

    /**method for adding message, related to this object by being sending from it, to the list of sending message */
    public void addSendingMessageGui(SequenceMessageGUI sequenceMessageGUI){
        this.sendingMessageGUIList.add(sequenceMessageGUI);
        if (sequenceMessageGUI.getDeactivateSender()){
            deactivateObject(sequenceMessageGUI);
        } else {activateObject(sequenceMessageGUI, true);}
    }

    public void removeSendingMessageGui(SequenceMessageGUI sequenceMessageGUI){
        //remove the message
        this.sendingMessageGUIList.remove(sequenceMessageGUI);

        //move all the active-rectangles
        moveActiveArea();
    }

    /**
     * method handling removing this object from gui
     */
    public void removeSequenceObjectGui(){
        //remove active areas
        for (Rectangle rect : timeLineActiveRectangleList){ this.canvas.getChildren().remove(rect); }

        //remove all parts of object from canvas
        this.canvas.getChildren().removeAll(
                this.objBackground,
                this.objNameText,
                this.objectTimeLine
        );
    }

    /**
     *  method for activating this object - means creating "active" rectangle on the timeline
     * @param sequenceMessageGUI
     * @param isSender
     * */
    private void activateObject(SequenceMessageGUI sequenceMessageGUI, Boolean isSender){
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(15);
        Double time = Double.MAX_VALUE;

        //set activate rectangle coordinates - according to which end of line representing message should be use
        if (isSender){
            rectangle.setX(sequenceMessageGUI.getMessageLine().getStartX() - 8);
            rectangle.setY(sequenceMessageGUI.getMessageLine().getStartY() - 2);

            //set height - how long it lasts
            //get the closest message, which
            for (SequenceMessageGUI messageGUI : receivingMessageGUIList){
                if (messageGUI.getDeactivateSender() && messageGUI.getMessageLine().getStartY() < time && !sequenceMessageGUI.equals(messageGUI)){
                    //found message who deactivate this object
                    time = messageGUI.getMessageLine().getStartY();
                }
            }
            for (SequenceMessageGUI messageGUI : sendingMessageGUIList){
                if (messageGUI.getDeactivateSender() && messageGUI.getMessageLine().getStartY() < time && !sequenceMessageGUI.equals(messageGUI)){
                    //found message who deactivate this object
                    time = messageGUI.getMessageLine().getStartY();
                }
            }

        } else {
            rectangle.setX(sequenceMessageGUI.getMessageLine().getEndX() - 8);
            rectangle.setY(sequenceMessageGUI.getMessageLine().getEndY() - 2);
            //set height - how long it lasts
            //get the closest message, which
            for (SequenceMessageGUI messageGUI : receivingMessageGUIList){
                if (messageGUI.getDeactivateReceiver() && messageGUI.getMessageLine().getEndY() < time && !sequenceMessageGUI.equals(messageGUI)){
                    //found message who deactivate this object
                    time = messageGUI.getMessageLine().getEndY();
                }
            }
            for (SequenceMessageGUI messageGUI : sendingMessageGUIList){
                if (messageGUI.getDeactivateReceiver() && messageGUI.getMessageLine().getEndY() < time && !sequenceMessageGUI.equals(messageGUI)){
                    //found message who deactivate this object
                    time = messageGUI.getMessageLine().getEndY();
                }
            }
        }

        //if there isnt any deactivator for timeline of this object
        if (time == Double.MAX_VALUE){
            rectangle.setHeight(2000);
        } else {
            rectangle.setHeight(time);
        }

        //if the destroying object life time is set then set height as max value, if time is greater
        if (rectangle.getHeight() + rectangle.getY() > objectDestroyedPosition && objectDestroyedPosition > -1.0){
            rectangle.setHeight(objectDestroyedPosition);
        }
        rectangle.setFill(Color.AQUA);

        //add rectangle on canvas and to the list of active-announcing rectangles
        this.canvas.getChildren().add(rectangle);
        this.canvas.getChildren().get(this.canvas.getChildren().indexOf(rectangle)).toBack();
        this.timeLineActiveRectangleList.add(rectangle);

    }

    /**
     * method for deactivating part of the object lifetime
     * @param sequenceMessageGUI
     */
    private void deactivateObject(SequenceMessageGUI sequenceMessageGUI){
        //loop through all activating rectangles and find the one which should be deactivated
        for (Rectangle activeRect : this.timeLineActiveRectangleList){
            //test whether this message is in active rectangle
            if (activeRect.getY() <= sequenceMessageGUI.getMessageLine().getStartY() &&
                activeRect.getY()+activeRect.getHeight() >= sequenceMessageGUI.getMessageLine().getStartY()){

                //end that rect at position of this message
                activeRect.setHeight(sequenceMessageGUI.getMessageLine().getStartY() - activeRect.getY());

            }
        }

        //if the destroyed object position is set then shortened the life-line
        if (this.objectDestroyedPosition > -1.0){
            this.objectTimeLine.setEndY(sequenceMessageGUI.getMessageLine().getEndY());
        }
    }

    /**
     * set whether this class exists also in class diagram
     * @param existsInClassDiagram
     */
    public void setExistsInClassDiagram(Boolean existsInClassDiagram, UMLSeqClass newUMLSeqClass) {
        this.existsInClassDiagram = existsInClassDiagram;

        //reset the color of the not-existing or existing class
        if (existsInClassDiagram){
            //reset displayed name -> cause it can exists in classdiagram, but gui isnt updated when name of class is changed
            //reset the name both here and in its inner representation
            updateObjectName();

            this.objNameText.setFill(Color.BLACK);
            //if new uml class has been set then change the current one
            if (newUMLSeqClass != null){
                this.umlSeqClass = newUMLSeqClass;
            }
        } else {
            this.objNameText.setFill(Color.RED);
        }

        //upload messages
        for (SequenceMessageGUI sequenceMessageGUI : this.sendingMessageGUIList){
            sequenceMessageGUI.uploadRelatedSenderUmlSeqClasses(this.umlSeqClass);
        }
        for (SequenceMessageGUI sequenceMessageGUI : this.receivingMessageGUIList){
            sequenceMessageGUI.uploadRelatedReceiverUmlSeqClasses(this.umlSeqClass);
        }
    }

    public Boolean getExistsInClassDiagram() {
        return existsInClassDiagram;
    }
}

