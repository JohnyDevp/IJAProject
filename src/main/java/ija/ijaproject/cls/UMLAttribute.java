package ija.ijaproject.cls;

public class UMLAttribute {
    /**
     * name of the attribute
     * */
    private String name;

    /**
     * name of the type
     * */
    private String type;

    /**
     * constructor
     * @param name
     */
    public UMLAttribute(String name) {
        this.name = name;
    }

    /**
     * constructor for attribute defined with name and type
     * @param name attribute name
     * @param type attribute type
     * */
    public UMLAttribute(String name, String type){
        this.name = name;
        this.type = type;
    }

    /**
     * method for setting attr name
     * @param name new name of attribute
     * */
    protected void setName(String name){
        this.name = name;
    }

    /**
     * method for setting attr type
     * @param type new type of attribute
     * */
    protected void setType(String type){
        this.type = type;
    }

    /**
     * method for returning name
     * @return attribute name
     * */
    public String getName() { return this.name; }

    /**
     * method for returning attribute type
     * @return attribute type
     * */
    public String getType() { return this.type; }

}
