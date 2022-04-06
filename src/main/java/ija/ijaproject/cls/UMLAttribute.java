package ija.ijaproject.cls;

public class UMLAttribute extends Element{

    /**
     * name of the type
     * */
    private String type;

    /**
     * constructor
     * @param name
     */
    public UMLAttribute(String name) {
        super(name);
    }

    /**
     * constructor for attribute defined with name and type
     * @param name attribute name
     * @param type attribute type
     * */
    public UMLAttribute(String name, String type){
        super(name);
        this.type = type;
    }

    /**
     * method for setting attr type
     * @param type new type of attribute
     * */
    public void setType(String type){
        this.type = type;
    }


    /**
     * method for returning attribute type
     * @return attribute type
     * */
    public String getType() { return this.type; }

}
