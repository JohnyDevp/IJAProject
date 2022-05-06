package ija.ijaproject.cls;

/**
 * class representing uml attribute of class or of operation in class or
 * interface
 *
 * @author xholan11
 * @author dr.Koci
 * @version 1.1
 */
public class UMLAttribute extends Element {

    /**
     * name of the type
     */
    public String type;
    public Character modifier;

    /**
     * Default constructor used for JSON parsing
     */
    public UMLAttribute() {
    }

    /**
     * constructor
     *
     * @param name a {@link java.lang.String} object
     */
    public UMLAttribute(String name) {
        super(name);
    }

    /**
     * <p>
     * Constructor for UMLAttribute.
     * </p>
     *
     * @param name a {@link java.lang.String} object
     * @param type a {@link java.lang.String} object
     */
    public UMLAttribute(String name, String type) {
        super(name);
        this.type = type;
    }

    /**
     * constructor for attribute defined with name and type
     *
     * @param name     attribute name
     * @param type     attribute type
     * @param modifier a {@link java.lang.Character} object
     */
    public UMLAttribute(Character modifier, String name, String type) {
        super(name);
        this.type = type;
        this.modifier = modifier;
    }

    /**
     * method for setting attr type
     *
     * @param type new type of attribute
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * method for returning attribute type
     *
     * @return attribute type
     */
    public String getType() {
        return this.type;
    }

    /**
     * modifier getter
     *
     * @return a {@link java.lang.Character} object
     */
    public Character getModifier() {
        return modifier;
    }
}
