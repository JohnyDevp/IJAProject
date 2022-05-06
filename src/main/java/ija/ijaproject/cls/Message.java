package ija.ijaproject.cls;

/**
 * 
 * Represent Message in suqence diagram
 * 
 *
 * @author xzimol04, xholan11
 * @version 1.1
 */
public class Message {

    /**
     * message types
     **/
    public enum MessageType {
        SYNC, ASYNC, RETURN, CREATE, DESTROY
    };

    public Double Ycoord;
    public String senderName;
    public String recieverName;
    public transient UMLSeqClass classSender;
    public transient UMLSeqClass classReceiver;
    public UMLOperation umlOperation;
    public Message.MessageType messageType;

    public Boolean senderDeactivation = false;
    public Boolean receiverDeactivation = false;

    /**
     * Default constructor used for json parsing
     */
    public Message() {
    }

    /**
     * 
     * Constructor for Message.
     * 
     *
     * @param Ycoord        a {@link java.lang.Double} object
     * @param classSender   a {@link ija.ijaproject.cls.UMLSeqClass} object
     * @param classReceiver a {@link ija.ijaproject.cls.UMLSeqClass} object
     * @param umlOperation  a {@link ija.ijaproject.cls.UMLOperation} object
     * @param messageType   a {@link ija.ijaproject.cls.Message.MessageType} object
     */
    public Message(Double Ycoord, UMLSeqClass classSender, UMLSeqClass classReceiver, UMLOperation umlOperation,
            Message.MessageType messageType) {
        this.Ycoord = Ycoord;
        this.classSender = classSender;
        this.classReceiver = classReceiver;
        this.messageType = messageType;
        this.umlOperation = umlOperation;
    }

    /**
     * setters
     *
     * @param Ycoord a {@link java.lang.Double} object
     */
    public void setYCoord(Double Ycoord) {
        this.Ycoord = Ycoord;
    }

    /**
     * Used before storing
     */

    public void setSenderRecieverName() {
        senderName = classSender.name;
        recieverName = classReceiver.name;
    }

    /**
     * setter
     *
     * @param senderDeactivation a {@link java.lang.Boolean} object
     */
    public void setSenderDeactivation(Boolean senderDeactivation) {
        this.senderDeactivation = senderDeactivation;
    }

    /**
     * setter
     *
     * @param receiverDeactivation a {@link java.lang.Boolean} object
     */
    public void setReceiverDeactivation(Boolean receiverDeactivation) {
        this.receiverDeactivation = receiverDeactivation;
    }

    /**
     * setter
     *
     * @param classReceiver a {@link ija.ijaproject.cls.UMLSeqClass} object
     */
    public void setClassReceiver(UMLSeqClass classReceiver) {
        this.classReceiver = classReceiver;
    }

    /**
     * setter
     *
     * @param classSender a {@link ija.ijaproject.cls.UMLSeqClass} object
     */
    public void setClassSender(UMLSeqClass classSender) {
        this.classSender = classSender;
    }

    /**
     * getter
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getReceiverDeactivation() {
        return receiverDeactivation;
    }

    /**
     * getter
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getSenderDeactivation() {
        return senderDeactivation;
    }

    /**
     * getter
     *
     * @return a {@link java.lang.Double} object
     */
    public Double getYCoord() {
        return Ycoord;
    }

    /**
     * getter
     *
     * @return a {@link ija.ijaproject.cls.Message.MessageType} object
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * getter
     *
     * @return a {@link ija.ijaproject.cls.UMLSeqClass} object
     */
    public UMLSeqClass getClassSender() {
        return classSender;
    }

    /**
     * getter
     *
     * @return a {@link ija.ijaproject.cls.UMLSeqClass} object
     */
    public UMLSeqClass getClassReceiver() {
        return classReceiver;
    }

    /**
     * getter
     *
     * @return a {@link ija.ijaproject.cls.UMLOperation} object
     */
    public UMLOperation getUmlOperation() {
        return umlOperation;
    }

}
