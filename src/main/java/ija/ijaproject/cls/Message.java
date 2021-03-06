package ija.ijaproject.cls;

public class Message {

    /**
     * message types
     **/
    public enum MessageType {
        SYNC, ASYNC, RETURN, CREATE, DESTROY
    };

    public Double Ycoord;
    public UMLSeqClass classSender;
    public UMLSeqClass classReceiver;
    public UMLOperation umlOperation;
    public Message.MessageType messageType;

    public Boolean senderDeactivation = false;
    public Boolean receiverDeactivation = false;

    /**
     * Default constructor used for json parsing
     */
    public Message() {
    }

    public Message(Double Ycoord, UMLSeqClass classSender, UMLSeqClass classReceiver, UMLOperation umlOperation,
            Message.MessageType messageType) {
        this.Ycoord = Ycoord;
        this.classSender = classSender;
        this.classReceiver = classReceiver;
        this.messageType = messageType;
        this.umlOperation = umlOperation;
    }

    /** setters */
    public void setYCoord(Double Ycoord) {
        this.Ycoord = Ycoord;
    }

    /** setter */
    public void setSenderDeactivation(Boolean senderDeactivation) {
        this.senderDeactivation = senderDeactivation;
    }

    /** setter */
    public void setReceiverDeactivation(Boolean receiverDeactivation) {
        this.receiverDeactivation = receiverDeactivation;
    }

    /** setter */
    public void setClassReceiver(UMLSeqClass classReceiver) {
        this.classReceiver = classReceiver;
    }

    /** setter */
    public void setClassSender(UMLSeqClass classSender) {
        this.classSender = classSender;
    }

    /** getter */
    public Boolean getReceiverDeactivation() {
        return receiverDeactivation;
    }

    /** getter */
    public Boolean getSenderDeactivation() {
        return senderDeactivation;
    }

    /** getter */
    public Double getYCoord() {
        return Ycoord;
    }

    /** getter */
    public MessageType getMessageType() {
        return messageType;
    }

    /** getter */
    public UMLSeqClass getClassSender() {
        return classSender;
    }

    /** getter */
    public UMLSeqClass getClassReceiver() {
        return classReceiver;
    }

    /** getter */
    public UMLOperation getUmlOperation() {
        return umlOperation;
    }

}
