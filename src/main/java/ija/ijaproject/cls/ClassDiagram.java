package ija.ijaproject.cls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClassDiagram extends Element{
    /**
     * list of all classes and interfaces in class diagram
     * */
    private List<UMLClassInterfaceTemplate> umlList = new ArrayList<>();

    /**getter*/
    public List<UMLClassInterfaceTemplate> getUmlObjectsList(){return this.umlList;}

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
}
