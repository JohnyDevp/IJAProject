package ija.ijaproject;

import ija.ijaproject.cls.Message;
import ija.ijaproject.cls.SequenceDiagram;
import ija.ijaproject.cls.UMLAttribute;
import ija.ijaproject.cls.UMLSeqClass;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

/**
 * <p>
 * SequenceMessageGUI class.
 * </p>
 *
 * @author musta-pollo
 * @version 1.1
 */
public class SequenceMessageGUI {
    // implicitly set minimal Y for each not message
    // defined according to where ends the box of object
    private static final int YCOORD_BEGIN = 80;

    private Boolean existsInClassDiagram = true;

    private SequenceDiagram sequenceDiagram;

    private Message.MessageType messageType;
    private Message umlMessage;

    private Pane canvas;
    private double mousePrevY;

    private SequenceObjectGUI objectSender;
    private SequenceObjectGUI objectReceiver;

    private Boolean deactivateReceiver = false;
    private Boolean deactivateSender = false;

    private Line messageLine;

    private Polygon messageArrow;

    // labels on the relation line
    private Text messageText;

    /**
     * getter
     *
     * @return a {@link ija.ijaproject.cls.Message} object
     */
    public Message getUmlMessage() {
        return umlMessage;
    }

    /**
     * getter
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getDeactivateReceiver() {
        return deactivateReceiver;
    }

    /**
     * getter
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getDeactivateSender() {
        return deactivateSender;
    }

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
     * serve for objects who want to activate their lifetime - they have to know
     * from where they should activate themselves*
     *
     * @return a {@link javafx.scene.shape.Line} object
     */
    public Line getMessageLine() {
        return messageLine;
    }

    /**
     * constructor of new message gui - everything is known
     * creating the line and its event for handling selecting this line
     * setting up the relation type
     *
     * @param message         a {@link ija.ijaproject.cls.Message} object
     * @param objectSender    a {@link ija.ijaproject.SequenceObjectGUI} object
     * @param objectReceiver  a {@link ija.ijaproject.SequenceObjectGUI} object
     * @param canvas          a {@link javafx.scene.layout.Pane} object
     * @param sequenceDiagram a {@link ija.ijaproject.cls.SequenceDiagram} object
     */
    public SequenceMessageGUI(SequenceDiagram sequenceDiagram, Message message, SequenceObjectGUI objectSender,
            SequenceObjectGUI objectReceiver, Pane canvas) {
        // set sequence diagram
        this.sequenceDiagram = sequenceDiagram;

        // set the canvas
        this.canvas = canvas;

        // set type of relation
        this.messageType = message.getMessageType();
        this.umlMessage = message;

        // set the gui
        this.objectReceiver = objectReceiver;
        this.objectSender = objectSender;

        // set if it deactivate any object or not
        this.deactivateSender = message.getSenderDeactivation();
        this.deactivateReceiver = message.getReceiverDeactivation();

        // always deactivate receiver when destroy message is the type
        if (this.messageType == Message.MessageType.DESTROY)
            this.deactivateReceiver = true;

        createMessageGui();
    }

    /**
     * <p>
     * notifyObjectsAboutDeletion.
     * </p>
     *
     * @param who a {@link java.lang.String} object
     */
    public void notifyObjectsAboutDeletion(String who) {
        switch (who) {
            case "sender":
                this.objectSender.removeSendingMessageGui(this);
                break;
            case "receiver":
                this.objectReceiver.removeReceivingMessageGui(this);
                break;
            case "both":
                this.objectSender.removeSendingMessageGui(this);
                this.objectReceiver.removeReceivingMessageGui(this);
                break;

        }
    }

    /**
     * remove only gui of message and its intern representation
     */
    public void removeMessageGui() {
        // remove from canvas
        this.canvas.getChildren().removeAll(
                this.messageText,
                this.messageLine,
                this.messageArrow);
    }

    /**
     * creating the gui representation of the message
     */
    private void createMessageGui() {
        // set up the line and the event when click on the message
        this.messageLine = new Line();
        this.messageLine.setStrokeWidth(2.5);
        this.messageLine.toBack();
        this.messageLine.setCursor(Cursor.HAND);

        // set actions for this line to enable its movement
        this.messageLine.setOnMousePressed(mouseEvent -> {
            this.mousePrevY = mouseEvent.getY();
        });

        this.messageLine.setOnMouseDragged(mouseEvent -> {
            double diffY = mouseEvent.getY() - this.mousePrevY;
            this.mousePrevY = mouseEvent.getY();

            // test whether it can be moved
            if (this.umlMessage.getYCoord() + diffY < YCOORD_BEGIN) {
                return;
            }

            this.umlMessage.setYCoord(this.umlMessage.getYCoord() + diffY);

            messageGuiMove();
            // TODO - move if create message
        });

        // create line end points
        this.messageLine.setStartX(this.objectSender.getObjectTimeLine().getStartX());
        this.messageLine.setStartY(this.umlMessage.getYCoord());
        this.messageLine.setEndX(this.objectReceiver.getObjectTimeLine().getStartX());
        this.messageLine.setEndY(this.umlMessage.getYCoord());

        // set dashed if the message has return type
        if (messageType == Message.MessageType.RETURN) {
            this.messageLine.getStrokeDashArray().addAll(20d, 20d);
        }

        // set text of message
        // create operation text
        StringBuilder textOfOperation = new StringBuilder(
                umlMessage.getUmlOperation().getModifier() + umlMessage.getUmlOperation().getName() + " (");
        for (UMLAttribute umlParam : umlMessage.getUmlOperation().getParametersOfOperationList()) {
            textOfOperation.append(umlParam.getName()).append(" : ").append(umlParam.getType());
        }
        textOfOperation.append(") : ").append(umlMessage.getUmlOperation().getType()).append(" | ")
                .append(this.messageType.toString());

        this.messageText = new Text(textOfOperation.toString());
        this.messageText.setX((this.messageLine.getStartX() + this.messageLine.getEndX()) / 2
                - this.messageText.getLayoutBounds().getWidth() / 2);
        this.messageText.setY(this.umlMessage.getYCoord() - 20);

        // set the ending part of line
        createMessageLineEnding();

        // add everything on canvas
        this.canvas.getChildren().addAll(
                this.messageLine,
                this.messageArrow,
                this.messageText);

    }

    /**
     * creates message end - it has to be recomputed every time it has been moved
     */
    private void createMessageLineEnding() {
        this.messageArrow = new Polygon();

        double slope = (this.messageLine.getStartY() - this.messageLine.getEndY())
                / (this.messageLine.getStartX() - this.messageLine.getEndX());
        double lineAngle = Math.atan(slope);
        double lineLength = Math.sqrt(Math.pow((this.messageLine.getStartY() - this.messageLine.getEndY()), 2)
                + Math.pow((this.messageLine.getStartX() - this.messageLine.getEndX()), 2));
        double arrowAngle, arrowLength, arrowWide;

        arrowAngle = this.messageLine.getStartX() > this.messageLine.getEndX() ? Math.toRadians(45)
                : -Math.toRadians(225);
        arrowLength = 20;
        this.messageArrow.getPoints().addAll(
                // the aim
                this.messageLine.getEndX(), this.messageLine.getEndY(),
                // left corner
                arrowLength * Math.cos(lineAngle - arrowAngle) + this.messageLine.getEndX(),
                arrowLength * Math.sin(lineAngle - arrowAngle) + this.messageLine.getEndY(),
                // right corner
                arrowLength * Math.cos(lineAngle + arrowAngle) + this.messageLine.getEndX(),
                arrowLength * Math.sin(lineAngle + arrowAngle) + this.messageLine.getEndY());
        this.messageArrow.setStroke(Color.BLACK);
        this.messageArrow.setFill(Color.WHITE);

        /*
         * switch (this.messageType){
         * case CREATE: break;
         * case SYNC: break;
         * case ASYNC: break;
         * case RETURN:
         * //set dashed line
         * this.messageLine.getStrokeDashArray().addAll(25d, 10d);
         * 
         * //set the end arrow
         * 
         * break;
         * case DESTROY: break;
         * }
         */
    }

    /**
     * method for moving the message - resizing and everything around it
     */
    public void messageGuiMove() {
        // resize all the points of the line
        this.messageLine.setStartX(this.objectSender.getObjectTimeLine().getStartX());
        this.messageLine.setStartY(this.umlMessage.getYCoord());
        this.messageLine.setEndX(this.objectReceiver.getObjectTimeLine().getStartX());
        this.messageLine.setEndY(this.umlMessage.getYCoord());

        // reset text of message coords
        this.messageText.setX((this.messageLine.getStartX() + this.messageLine.getEndX()) / 2
                - this.messageText.getLayoutBounds().getWidth() / 2);
        this.messageText.setY(this.umlMessage.getYCoord() - 20);

        // reset the ending part of line (firstly remove it from canvas, then add)
        this.canvas.getChildren().remove(this.messageArrow);
        createMessageLineEnding();
        this.canvas.getChildren().add(this.messageArrow);

        // notify the related objects about movement of active area
        this.objectSender.moveActiveArea();
        this.objectReceiver.moveActiveArea();
    }

    /**
     * <p>
     * Setter for the field <code>existsInClassDiagram</code>.
     * </p>
     *
     * @param existsInClassDiagram a {@link java.lang.Boolean} object
     */
    public void setExistsInClassDiagram(Boolean existsInClassDiagram) {
        this.existsInClassDiagram = existsInClassDiagram;
        if (existsInClassDiagram) {
            this.messageText.setFill(Color.BLACK);
        } else {
            this.messageText.setFill(Color.RED);
        }
    }

    /**
     * <p>
     * uploadRelatedReceiverUmlSeqClasses.
     * </p>
     *
     * @param umlSeqClass a {@link ija.ijaproject.cls.UMLSeqClass} object
     */
    public void uploadRelatedReceiverUmlSeqClasses(UMLSeqClass umlSeqClass) {
        this.umlMessage.setClassReceiver(umlSeqClass);

        // check whether the class still contains this operation
        if (umlSeqClass.getUmlClass().foundOperation(this.umlMessage.getUmlOperation())) {
            this.messageText.setFill(Color.BLACK);
        } else {
            this.messageText.setFill(Color.RED);
        }
    }

    /**
     * <p>
     * uploadRelatedSenderUmlSeqClasses.
     * </p>
     *
     * @param umlSeqClass a {@link ija.ijaproject.cls.UMLSeqClass} object
     */
    public void uploadRelatedSenderUmlSeqClasses(UMLSeqClass umlSeqClass) {
        this.umlMessage.setClassSender(umlSeqClass);
    }
}
