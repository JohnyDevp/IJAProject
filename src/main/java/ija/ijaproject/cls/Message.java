package ija.ijaproject.cls;

public class Message {

    /**message types
     **/
    public enum MessageType  {SYNC, ASYNC, RETURN, CREATE, DESTROY};

    private Double Ycoord;
    private UMLSeqClass classSender;
    private UMLSeqClass classReceiver;
    private UMLOperation umlOperation;
    private Message.MessageType messageType;

    private Boolean senderDeactivation = false;
    private Boolean receiverDeactivation = false;

    public Message(Double Ycoord, UMLSeqClass classSender, UMLSeqClass classReceiver, UMLOperation umlOperation, Message.MessageType messageType){
        this.Ycoord  = Ycoord;
        this.classSender = classSender;
        this.classReceiver = classReceiver;
        this.messageType = messageType;
        this.umlOperation = umlOperation;
    }

    /**setters*/
    public void setYCoord(Double Ycoord) {
        this.Ycoord = Ycoord;
    }

    /**setter*/
    public void setSenderDeactivation(Boolean senderDeactivation) {
        this.senderDeactivation = senderDeactivation;
    }

    /**setter*/
    public void setReceiverDeactivation(Boolean receiverDeactivation) {
        this.receiverDeactivation = receiverDeactivation;
    }

    /**getter*/
    public Boolean getReceiverDeactivation() {
        return receiverDeactivation;
    }

    /**getter*/
    public Boolean getSenderDeactivation() {
        return senderDeactivation;
    }

    /**getter*/
    public Double getYCoord() {
        return Ycoord;
    }

    /**getter*/
    public MessageType getMessageType() {
        return messageType;
    }

    /**getter*/
    public UMLSeqClass getClassSender() {
        return classSender;
    }

    /**getter*/
    public UMLOperation getUmlOperation() {
        return umlOperation;
    }

}
