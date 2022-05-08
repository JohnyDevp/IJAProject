package ija.ijaproject.cls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class representing sequence diagram with its components
 *
 * @author xholan11, xzimol04
 * @version 1.1
 */
public class SequenceDiagram extends Element {

    /**
     * list of uml classes, which are the only ones, which can participate in
     * sequence diagram
     */
    public Map<String, UMLSeqClass> listOfObjectsParticipants = new HashMap<String, UMLSeqClass>();

    /** list of all messages used in this sequence diagram */
    public List<Message> messageList = new ArrayList<>();

    /**
     * Json constructor used for json parsing
     */
    public SequenceDiagram() {
    }

    /**
     * constructor
     *
     * @param name name of sequence diagram
     */
    public SequenceDiagram(String name) {
        super(name);
    }

    /**
     * method for adding new participated class in sequence diagram
     *
     * @param umlSeqClass class representing the object which takes part in the
     *                    sequence diagram
     * @return a boolean
     */
    public boolean addObject(UMLSeqClass umlSeqClass) {
        if (this.listOfObjectsParticipants.containsKey(umlSeqClass.getUniqueName())) {
            return false;
        } else {
            this.listOfObjectsParticipants.put(umlSeqClass.getUniqueName(), umlSeqClass);
            return true;
        }
    }

    /**
     * function for removing the object from the list
     *
     * @param umlSeqClass a {@link ija.ijaproject.cls.UMLSeqClass} object
     */
    public void removeObject(UMLSeqClass umlSeqClass) {
        this.getListOfObjectsParticipants().remove(umlSeqClass.getUniqueName());
    }

    /**
     * adding message to specific position - rest of messages will move forward +1
     *
     * @param messageType   type of the message (sync, async, ...)
     * @param Ycoord        Y coordination on timeline of object
     * @param classSender   class which is sending the message
     * @param classReceiver class which is receiving the message
     * @param umlOperation  operation representing the method which is called
     * @return a {@link ija.ijaproject.cls.Message} object
     */
    public Message createMessage(Double Ycoord, UMLSeqClass classSender, UMLSeqClass classReceiver,
            UMLOperation umlOperation, Message.MessageType messageType) {
        // create new message
        Message msg;
        msg = new Message(Ycoord, classSender, classReceiver, umlOperation, messageType);
        return msg;
    }

    /**
     * 
     * createReturnMessage.
     * 
     *
     * @param Ycoord      a {@link java.lang.Double} object
     * @param clsSender   a {@link ija.ijaproject.cls.UMLSeqClass} object
     * @param clsReceiver a {@link ija.ijaproject.cls.UMLSeqClass} object
     * @param text        a {@link java.lang.String} object
     * @return a {@link ija.ijaproject.cls.Message} object
     */
    public Message createReturnMessage(Double Ycoord, UMLSeqClass clsSender, UMLSeqClass clsReceiver, String text) {
        // create new message
        Message msg;
        msg = new Message(Ycoord, clsSender, clsReceiver, new UMLOperation(text), Message.MessageType.RETURN);
        return msg;
    }

    /**
     * delete message from diagram
     *
     * @param message message object to be deleted
     */
    public void deleteMessage(Message message) {
        if (!this.messageList.contains(message))
            return; // if the message doesnt exist in list then dont continue

        // remove desired message from the list
        this.messageList.remove(message);
    }

    /**
     * getter
     *
     * @return map [integer, messageType] representing what type of message is sent
     *         in what time
     */
    public List<Message> getMessageList() {
        return this.messageList;
    }

    /**
     * 
     * findObject.
     * 
     *
     * @param name can be either name of object or object to found
     * @return found object or null if not found or bad object has been passed
     */
    public UMLSeqClass findObject(String name) {

        return listOfObjectsParticipants.get(name);
    }

    /**
     * list of all objects, which taken part in this sequence diagram
     * 
     * @return
     */
    public Map<String, UMLSeqClass> getListOfObjectsParticipants() {
        return listOfObjectsParticipants;
    }

}
