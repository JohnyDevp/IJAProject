package ija.ijaproject.cls;

import java.util.*;

/**
 * class representing sequence diagram with its components
 * @author xholan11
 * */
public class SequenceDiagram extends Element{



    /**list  of uml classes, which are the only ones, which can participate in sequence diagram*/
    private List<UMLClass> listOfObjectsParticipants = new ArrayList<>();

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
     * @param umlClass class representing the object which takes part in the sequence diagram*/
    public boolean addObjectParticipatedIn(UMLClass umlClass){
        if (this.listOfObjectsParticipants.contains(umlClass)){
            return false;
        } else {
            return this.listOfObjectsParticipants.add(umlClass);
        }
    }

    /**adding message to specific position - rest of messages will move forward +1
     * @param messageType type of the message (sync, async, ...)
     * @param timeunit time when the message should be processed
     * @param umlClass class which is sending the message
     * @param umlOperation operation representing the method which is called*/
    public Message createMessage(Integer timeunit, UMLClass umlClass, UMLOperation umlOperation, Message.MessageType messageType){
        //create new message
        Message msg;
        //time when the message will be created
        Integer timeToAdd = timeunit;
        if (timeToAdd < 0) { timeToAdd = 0;}

        //move whole list forward if the message has been inserted somewhere into timeline
        //otherwise just increase the time stamp for whole diagram
        if (timeToAdd >= this.lastTimeStamp +1) { //the message will be added to the end
            this.lastTimeStamp += 1; //all the items mustn't be moved
            timeToAdd = this.lastTimeStamp;
        }else {
            for (Message msgLoop : getMessageList()){
                if (msgLoop.getTimeStamp() >= timeunit){
                    msgLoop.setTimeStamp(msgLoop.getTimeStamp()+1);
                }
            }
        }

        msg = new Message(timeToAdd, umlClass, umlOperation, messageType);
        return msg;
    }

    /**delete message from diagram
     * @param message message object to be deleted*/
    public void deleteMessage(Message message){
        if (!this.messageList.contains(message)) return; //if the message doesnt exist in list then dont continue

        //move the numbers of all messages
        for (Message msg : this.messageList){
            if (msg.equals(message)) continue;

            if (msg.getTimeStamp() > message.getTimeStamp()){
                msg.setTimeStamp(msg.getTimeStamp()-1);
            }
        }

        //remove desired message from the list
        this.messageList.remove(message);
    }

    /**getter
     * @return list of all objects, which taken part in this sequence diagram*/
    public List<UMLClass> getListOfObjectsParticipants() {
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
