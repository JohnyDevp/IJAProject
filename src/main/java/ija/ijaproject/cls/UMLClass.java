package ija.ijaproject.cls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class UMLClass extends Element{

    /**
     * name of the class
     * */
    protected String name;

    /**
     * list of attributes of class
     * */
    protected java.util.List<UMLAttribute> umlAttributesList = new ArrayList<UMLAttribute>();

    /**
     * list of operations of class
     * */
    protected List<UMLOperation> umlOperationsList = new ArrayList<UMLOperation>();

    /**
     * constructor
     * Creating an instance of class representing class in UML diagram
     * @param name name of class
     */
    public UMLClass(String name){
        super(name);
    }

    /**
     * adding an attribute (which is parameter) of this class
     * checking whether theres no other attribute with same name
     * @param attr parameter represent the whole attribute
     * @return true or false according successfulness of this method
     * */
    public boolean addAttribute(UMLAttribute attr){
        for (Iterator<UMLAttribute> itr = umlAttributesList.iterator(); itr.hasNext(); ) {
            UMLAttribute umlAttribute = itr.next();

            //if attribute has been find with desired name then fail
            if (umlAttribute.getName() == attr.getName()) {
                return false;
            }
        }

        //attribute hasn't been set yet
        this.umlAttributesList.add(attr);
        return true;

    }

    /**
     * deleting an attribute - returns nothing
     * @param name name of attribute for deletion
     * */
    public void deleteAttribute(String name){
        UMLAttribute umlAttribute = findAttribute(name);

        //check whether the attribute with entered name exists and remove it
        if (umlAttribute == null){
            return;
        } else {
            this.umlAttributesList.remove(umlAttribute);
        }
    }

    /**
     * find and get attribute according its name
     * helper method for deleteAttribute()
     * @param name attribute name
     * @return UMLAttribute or null if not found
     * */
    private UMLAttribute findAttribute(String name) {
        for (Iterator<UMLAttribute> itr = umlAttributesList.iterator(); itr.hasNext(); ) {
            UMLAttribute umlAttribute = itr.next();

            //if attribute has been find with desired name
            if (umlAttribute.getName() == name) {
                return umlAttribute;
            }
        }

        //attribute hasnt been found
        return null;
    }

    /**
     * method for get list of all attributes
     * @return unmodifiable list of all attributes
     * */
    public List<UMLAttribute> getUmlAttributesList(){
        return Collections.unmodifiableList(this.umlAttributesList);
    }

    /**
     * adding an operation (which is parameter) of this class
     * @param operation parameter represent the whole operation
     * @return true or false according successfullness of this method
     * */
    public boolean addOperation(UMLOperation operation){
        if (umlOperationsList.contains(operation)) {
            //list already contains the attribute
            return false;
        } else {
            return umlOperationsList.add(operation);
        }
    }

    /**
     * deleting an operation
     * @param name name of operation for deletion
     * */
    public void deleteOperation(String name){
        UMLOperation umlOperation = findOperation(name);

        //check whether the attribute with entered name exists and remove it
        if (umlOperation == null){
            return;
        } else {
            this.umlOperationsList.remove(umlOperation);
        }
    }

    /**
     * find and get operation according its name
     * helper method for deleteOperation()
     * @param name operation name
     * @return UMLOperation or null if not found
     * */
    private UMLOperation findOperation(String name) {
        for (Iterator<UMLOperation> itr = umlOperationsList.iterator(); itr.hasNext(); ) {
            UMLOperation umlOperation = itr.next();

            //if attribute has been find with desired name
            if (umlOperation.getName() == name) {
                return umlOperation;
            }
        }

        //operation hasn't been found
        return null;
    }

    /**
     * method for get list of all operations
     * @return unmodifiable list of all operations
     * */
    public List<UMLOperation> getUmlOperationsList(){
        return Collections.unmodifiableList(this.umlOperationsList);
    }
}
