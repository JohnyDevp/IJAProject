package ija.ijaproject.cls;

import java.util.*;

/**
 * class representing sequence diagram with its components
 * @author xholan11
 * */
public class SequenceDiagram extends Element{



    /**list  of uml classes, which are the only ones, which can participate in sequence diagram*/
    private List<UMLSeqClass> listOfObjectsParticipants = new ArrayList<>();

    /**list of all messages used in this sequence diagram*/
    private List<Message> messageList = new ArrayList<>();

    /**variable storing last time used for sending message*/
    private Integer lastTimeStamp;

    /**constructor
     * @param name name of sequence diagram*/
    public SequenceDiagram(String name){
        super(name);
        lastTimeStamp = 0;
    }

    /**method for adding new participated class in sequence diagram
     * @param umlSeqClass class representing the object which takes part in the sequence diagram*/
    public boolean addObject(UMLSeqClass umlSeqClass){
        if (this.listOfObjectsParticipants.contains(umlSeqClass)){
            return false;
        } else {
            return this.listOfObjectsParticipants.add(umlSeqClass);
        }
    }

    /**adding message to specific position - rest of messages will move forward +1
     * @param messageType type of the message (sync, async, ...)
     * @param Ycoord Y coordination on timeline of object
     * @param umlClass class which is sending the message
     * @param umlOperation operation representing the method which is called*/
    public Message createMessage(Double Ycoord, UMLClass umlClass, UMLOperation umlOperation, Message.MessageType messageType){
        //create new message
        Message msg;
        msg = new Message(Ycoord, umlClass, umlOperation, messageType);
        return msg;
    }

    /**delete message from diagram
     * @param message message object to be deleted*/
    public void deleteMessage(Message message){
        if (!this.messageList.contains(message)) return; //if the message doesnt exist in list then dont continue

        //remove desired message from the list
        this.messageList.remove(message);
    }

    /**getter
     * @return list of all objects, which taken part in this sequence diagram*/
    public List<UMLSeqClass> getListOfObjectsParticipants() {
        return listOfObjectsParticipants;
    }

    /**getter
     * @return map [integer, messageType] representing what type of message is sent in what time*/
    public List<Message> getMessageList() {
        return this.messageList;
    }

    /**getter
     * @return the last*/
    public Integer getLastTimeStamp() {
        return lastTimeStamp;
    }
}
