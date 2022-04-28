package ija.ijaproject;

import ija.ijaproject.cls.*;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SequenceMessageGUI {
    private Message.MessageType messageType;
    private Message umlMessage;

    private Pane canvas;
    private double mousePrevY;

    private SequenceObjectGUI objectSender;
    private SequenceObjectGUI objectReceiver;

    private Boolean deactivateReceiver = false;
    private Boolean deactivateSender = false;

    private Line messageLine;

    private Polygon relLineEnd;

    //labels on the relation line
    private Text messageText;

    /**getter*/
    public Boolean getDeactivateReceiver() {
        return deactivateReceiver;
    }

    /**getter*/
    public Boolean getDeactivateSender() {
        return deactivateSender;
    }

    /**getter*/
    public Message.MessageType getMessageType() {
        return messageType;
    }

    /**getter
     * serve for objects who want to activate their lifetime - they have to know from where they should activate themselves*
     */
    public Line getMessageLine() {
        return messageLine;
    }

    /**
     * constructor of new message gui - everything is known
     * creating the line and its event for handling selecting this line
     * setting up the relation type
     * @param message
     * @param objectSender
     * @param objectReceiver
     * @param canvas
     */
    public SequenceMessageGUI(Message message, SequenceObjectGUI objectSender, SequenceObjectGUI objectReceiver, Pane canvas){
        //set the canvas
        this.canvas = canvas;

        //set type of relation
        this.messageType = message.getMessageType();
        this.umlMessage = message;

        //set the gui
        this.objectReceiver = objectReceiver;
        this.objectSender = objectSender;

        //set if it deactivate any object or not
        this.deactivateSender = message.getSenderDeactivation();
        this.deactivateReceiver = message.getReceiverDeactivation();

        createMessageGui();
    }

    public void deleteMessageGui(){

    }

    /**
     * creating the gui representation of the message
     */
    private void createMessageGui(){
        //set up the line and the event when click on the message
        this.messageLine = new Line();
        this.messageLine.setStrokeWidth(2.5);
        this.messageLine.toBack();
        this.messageLine.setCursor(Cursor.HAND);

        //set actions for this line to enable its movement
        this.messageLine.setOnMousePressed(mouseEvent -> {
            this.mousePrevY = mouseEvent.getY();
        });

        this.messageLine.setOnMouseDragged(mouseEvent -> {
            double diffY = mouseEvent.getY() - this.mousePrevY;
            this.mousePrevY = mouseEvent.getY();

            this.umlMessage.setYCoord(this.umlMessage.getYCoord() + diffY);

            messageGuiMove();
            //TODO - move if create message
        });

        //create line end points
        this.messageLine.setStartX(this.objectSender.getObjectTimeLine().getStartX());
        this.messageLine.setStartY(this.umlMessage.getYCoord());
        this.messageLine.setEndX(this.objectReceiver.getObjectTimeLine().getStartX());
        this.messageLine.setEndY(this.umlMessage.getYCoord());

        //set text of message
        //create operation text
        StringBuilder textOfOperation = new StringBuilder(umlMessage.getUmlOperation().getModifier() + umlMessage.getUmlOperation().getName() + " (");
        for (UMLAttribute umlParam : umlMessage.getUmlOperation().getParametersOfOperationList()){
            textOfOperation.append(umlParam.getName()).append(" : ").append(umlParam.getType());
        }
        textOfOperation.append(") : ").append(umlMessage.getUmlOperation().getType()).append(" | ").append(this.messageType.toString());

        this.messageText = new Text(textOfOperation.toString());
        this.messageText.setX((this.messageLine.getStartX() + this.messageLine.getEndX()) / 2 - this.messageText.getLayoutBounds().getWidth()/2);
        this.messageText.setY(this.umlMessage.getYCoord() - 20);

        //set the ending part of line
        createMessageLineEnding();

        //add everything on canvas
        this.canvas.getChildren().addAll(
                this.messageLine,
                this.relLineEnd,
                this.messageText
        );

    }

    /**
     * creates message end - it has to be recomputed every time it has been moved
     */
    private void createMessageLineEnding(){
        this.relLineEnd = new Polygon();

        double slope = (this.messageLine.getStartY() - this.messageLine.getEndY()) / (this.messageLine.getStartX() - this.messageLine.getEndX());
        double lineAngle = Math.atan(slope);
        double lineLength = Math.sqrt(Math.pow((this.messageLine.getStartY() - this.messageLine.getEndY()), 2) + Math.pow((this.messageLine.getStartX() - this.messageLine.getEndX()),2));
        double arrowAngle, arrowLength, arrowWide;

        arrowAngle = this.messageLine.getStartX() > this.messageLine.getEndX() ? Math.toRadians(45) : -Math.toRadians(225);
        arrowLength = 20;
        this.relLineEnd.getPoints().addAll(
                //the aim
                this.messageLine.getEndX(), this.messageLine.getEndY(),
                //left corner
                arrowLength * Math.cos(lineAngle - arrowAngle) + this.messageLine.getEndX(), arrowLength * Math.sin(lineAngle - arrowAngle) + this.messageLine.getEndY(),
                //right corner
                arrowLength * Math.cos(lineAngle + arrowAngle) + this.messageLine.getEndX(), arrowLength * Math.sin(lineAngle + arrowAngle) + this.messageLine.getEndY()
        );
        this.relLineEnd.setStroke(Color.BLACK);
        this.relLineEnd.setFill(Color.WHITE);

        /*switch (this.messageType){
            case CREATE: break;
            case SYNC: break;
            case ASYNC: break;
            case RETURN:
                //set dashed line
                this.messageLine.getStrokeDashArray().addAll(25d, 10d);

                //set the end arrow

                break;
            case DESTROY: break;
        }*/
    }

    /**
     * method for moving the message - resizing and everything around it
     * */
    public void messageGuiMove() {
        //resize all the points of the line
        this.messageLine.setStartX(this.objectSender.getObjectTimeLine().getStartX());
        this.messageLine.setStartY(this.umlMessage.getYCoord());
        this.messageLine.setEndX(this.objectReceiver.getObjectTimeLine().getStartX());
        this.messageLine.setEndY(this.umlMessage.getYCoord());

        //reset text of message coords
        this.messageText.setX((this.messageLine.getStartX() + this.messageLine.getEndX()) / 2 - this.messageText.getLayoutBounds().getWidth()/2);
        this.messageText.setY(this.umlMessage.getYCoord() - 20);

        //reset the ending part of line (firstly remove it from canvas, then add)
        this.canvas.getChildren().remove(this.relLineEnd);
        createMessageLineEnding();
        this.canvas.getChildren().add(this.relLineEnd);

        //notify the related objects about movement of active area
        this.objectSender.moveActiveArea();
        this.objectReceiver.moveActiveArea();
    }
}
