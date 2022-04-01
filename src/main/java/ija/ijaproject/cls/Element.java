package ija.ijaproject.cls;

public class Element {

    private String name;

    /**
     * constructor
     * @param name name of element
     */
    public Element(String name){
        this.name = name;
    }

    /**
     * method getName()
     */
    public String getName(){
        return this.name;
    }

    /**
     *
     * @param newName new name for property name
     * it renames the property of this class name
     */
    public void setName(String newName){
        this.name = newName;
    }
}
