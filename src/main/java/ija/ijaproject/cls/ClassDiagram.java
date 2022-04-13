package ija.ijaproject.cls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * class representing the whole class diagram with its items
 * @author xholan11
 * */
public class ClassDiagram extends Element{
    /**
     * list of all classes and interfaces in class diagram
     * */
    private List<UMLClassInterfaceTemplate> umlList = new ArrayList<>();

    /**
     * list of all relations in this diagram
     * */
    private List<UMLRelation> umlRelationList = new ArrayList<>();

    /**getter*/
    public List<UMLClassInterfaceTemplate> getUmlObjectsList(){return this.umlList;}

    /**getter*/
    public List<UMLRelation> getUmlRelationList() {
        return umlRelationList;
    }

    /**
     * constructor
     * set the name of this class diagram
     * @param name name of this class diagram
     */
    public ClassDiagram(String name) {
        super(name);
    }

    /**
     * Creates an instance of UML class and inserts it into diagram
     * If the name already exists returns null
     * @param name Name of the class
     * @return object representing class, if class already exists returns null
     */
    public UMLClass createClass(String name){
        for (UMLClassInterfaceTemplate umlItem : umlList){
            if (umlItem.getName().equals(name)){
                //diagram already contains class with entered name
                //returns null
                return null;
            }
        }

        //return newly created instance of UMLClass
        UMLClass uc = new UMLClass(name);
        this.umlList.add(uc);
        return uc;
    }

    /**
     * Adds created class to the diagram, if doesnt exists
     * @param umlClass class to be added
     * */
    public boolean addClass(UMLClass umlClass){
        for (UMLClassInterfaceTemplate umlItem : umlList){
            if (umlItem.getName().equals(umlClass.getName())){
                //diagram already contains class with entered name
                //returns null
                return false;
            }
        }

        //return newly success of adding instance of UMLClass to list
        return umlList.add(umlClass);
    }

    /**
     * Removes class of the diagram, if exists
     * @param umlClass class to be deleted
     * */
    public void deleteClass(UMLClass umlClass){
        umlList.remove(umlClass);
    }

    /**remove object representation from uml class diagram*/
    public void deleteObject(UMLClassInterfaceTemplate umlObject){
        umlList.remove(umlObject);
    }

    /**
     * Creates an instance of UML interface and inserts it into diagram
     * If the name already exists returns null
     * @param name Name of the class
     * @return object representing class, if class already exists returns null
     */
    public UMLInterface createInterface(String name){
        for (UMLClassInterfaceTemplate umlItem : umlList){
            if (umlItem.getName().equals(name)){
                //diagram already contains class with entered name
                //returns null
                return null;
            }
        }

        //return newly created instance of UMLClass
        UMLInterface ui = new UMLInterface(name);
        this.umlList.add(ui);
        return ui;
    }

    /**
     * Adds created interface to the diagram, if doesnt exists
     * @param umlInterface interface to be added
     * */
    public boolean addInterface(UMLInterface umlInterface){
        for (UMLClassInterfaceTemplate umlItem : umlList){
            if (umlItem.getName().equals(umlInterface.getName())){
                //diagram already contains class with entered name
                return false;
            }
        }

        //return newly success of adding instance of UMLClass to list
        return umlList.add(umlInterface);
    }

    /**
     * Removes interface of the diagram, if exists
     * @param umlInterface interface to be deleted
     * */
    public void deleteInterface(UMLInterface umlInterface){
        umlList.remove(umlInterface);
    }

    /**
     * @param o can be either name of object or object to found
     * @return found object or null if not found or bad object has been passed
     * */
    public UMLClassInterfaceTemplate findObject(Object o){
        if (o.getClass() == String.class){
            for (UMLClassInterfaceTemplate umlObject : getUmlObjectsList()){
                if (umlObject.getName().equals(o)){
                    return umlObject;
                }
            }
        } else if (o.getClass() == UMLClass.class || o.getClass() == UMLInterface.class){
            return getUmlObjectsList().get(getUmlObjectsList().indexOf(o));
        } else {
            return null;
        }
        return  null;
    }
    /**
     * @param name of new relation
     * @return UMLRelation object
     * */
    public UMLRelation createRelation(String name){
        UMLRelation umlRelation = new UMLRelation(name);
        this.getUmlRelationList().add(umlRelation);
        return umlRelation;
    }

    /**
     * @param umlRelation relation object
     * @return boolean according success of adding operation
     */
    public boolean addRelation(UMLRelation umlRelation){
        return getUmlRelationList().add(umlRelation);
    }

    /**
     * @param umlRelation relation object to be removed
     * */
    public void removeRelation(UMLRelation umlRelation){
        getUmlRelationList().remove(umlRelation);
    }
}
