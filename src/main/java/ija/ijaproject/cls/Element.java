package ija.ijaproject.cls;

/**
 * class representing element in string format - to be inherited
 * 
 * @author dr.Koci
 */
public class Element {

    public String name;

    /**
     * used for json parsing
     */
    public Element() {
    }

    /**
     * constructor
     * 
     * @param name name of element
     */
    public Element(String name) {
        this.name = name;
    }

    /**
     * method getName()
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @param newName new name for property name
     *                it renames the property of this class name
     */
    public void setName(String newName) {
        this.name = newName;
    }
}
