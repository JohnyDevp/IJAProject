package ija.ijaproject.cls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class UMLClass extends UMLInterface{

    /**
     * list of attributes of class
     * */
    protected java.util.List<UMLAttribute> umlAttributesList = new ArrayList<UMLAttribute>();

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
     * also helper method for deleteAttribute()
     * @param name attribute name
     * @return UMLAttribute or null if not found
     * */
    public UMLAttribute findAttribute(String name) {
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

}
