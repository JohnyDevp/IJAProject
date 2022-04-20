package ija.ijaproject.cls;

public class Message {

    /**message types
     **/
    public enum MessageType  {SYNC, ASYNC, RETURN, CREATE, DESTROY};

    private Integer timeStamp;
    private UMLClass umlClass;
    private UMLOperation umlOperation;
    private Message.MessageType messageType;

    public Message(Integer timeStamp, UMLClass umlClass, UMLOperation umlOperation, Message.MessageType messageType){
        this.timeStamp  = timeStamp;
        this.umlClass = umlClass;
        this.messageType = messageType;
        this.umlOperation = umlOperation;
    }

    /**setters*/
    public void setTimeStamp(Integer timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**getter*/
    public Integer getTimeStamp() {
        return timeStamp;
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
