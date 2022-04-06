package ija.ijaproject.cls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClassDiagram extends Element{
    /**
     * list of all classes in class diagram
     * */
    private List<UMLClass> umlClassList = new ArrayList<UMLClass>();

    /**
     * list of all interfaces in class diagram
     * */
    private List<UMLInterface> umlInterfaceList = new ArrayList<UMLInterface>();


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
        for(Iterator<UMLClass> itr = umlClassList.iterator(); itr.hasNext();){
            UMLClass uc = itr.next();
            if (uc.getName().equals(name))
            {
                //diagram already contains class with entered name
                //returns null
                return null;
            }
        }

        //return newly created instance of UMLClass
        UMLClass uc = new UMLClass(name);
        this.umlClassList.add(uc);
        return uc;
    }

    /**
     * Adds created class to the diagram, if doesnt exists
     * @param umlClass class to be added
     * */
    public boolean addClass(UMLClass umlClass){
        return umlClassList.add(umlClass);
    }

    /**
     * Removes class of the diagram, if exists
     * @param umlClass class to be deleted
     * */
    public void deleteClass(UMLClass umlClass){
        umlClassList.remove(umlClass);
    }

    /**
     * Creates an instance of UML interface and inserts it into diagram
     * If the name already exists returns null
     * @param name Name of the class
     * @return object representing class, if class already exists returns null
     */
    public UMLInterface createInterface(String name){
        for(Iterator<UMLInterface> itr = umlInterfaceList.iterator(); itr.hasNext();){
            UMLInterface ui = itr.next();
            if (ui.getName().equals(name))
            {
                //diagram already contains class with entered name
                //returns null
                return null;
            }
        }

        //return newly created instance of UMLClass
        UMLInterface ui = new UMLInterface(name);
        this.umlInterfaceList.add(ui);
        return ui;
    }

    /**
     * Adds created interface to the diagram, if doesnt exists
     * @param umlInterface interface to be added
     * */
    public boolean addInterface(UMLInterface umlInterface){
        return umlInterfaceList.add(umlInterface);
    }

    /**
     * Removes interface of the diagram, if exists
     * @param umlInterface interface to be deleted
     * */
    public void deleteInterface(UMLInterface umlInterface){
        umlInterfaceList.remove(umlInterface);
    }
}
