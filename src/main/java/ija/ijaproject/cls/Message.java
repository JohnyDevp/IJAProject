package ija.ijaproject.cls;

public class Message {

    /**message types
     **/
    public enum MessageType  {SYNC, ASYNC, RETURN, CREATE, DESTROY};

    private Double Ycoord;
    private UMLClass umlClass;
    private UMLOperation umlOperation;
    private Message.MessageType messageType;

    public Message(Double Ycoord, UMLClass umlClass, UMLOperation umlOperation, Message.MessageType messageType){
        this.Ycoord  = Ycoord;
        this.umlClass = umlClass;
        this.messageType = messageType;
        this.umlOperation = umlOperation;
    }

    /**setters*/
    public void setYCoord(Double Ycoord) {
        this.Ycoord = Ycoord;
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
    public UMLClass getUmlClass() {
        return umlClass;
    }

    /**getter*/
    public UMLOperation getUmlOperation() {
        return umlOperation;
    }
}
