package ija.ijaproject.cls;

/**
 * 
 * Class that represents UMLClass in sequence diagram
 * 
 *
 * @author xzimol04, xholan11
 * @version 1.1
 */
public class UMLSeqClass extends Element {

    public transient UMLClass umlClass;

    public String umlClassName;

    public Double Xcoord;

    /**
     * 
     * Constructor for UMLSeqClass.
     * 
     */
    public UMLSeqClass() {
    }

    /**
     * 
     * storeClassName.
     * 
     */
    public void storeClassName() {
        umlClassName = umlClass.name;
    }

    /**
     * The name of instance + class is unique
     * 
     * @return
     */
    public String getUniqueName() {
        return name + ":" + umlClass.name;
    }

    /**
     * getter
     *
     * @return a {@link ija.ijaproject.cls.UMLClass} object
     */
    public UMLClass getUmlClass() {
        return umlClass;
    }

    /**
     * setter
     *
     * @param umlClass a {@link ija.ijaproject.cls.UMLClass} object
     */
    public void setUmlClass(UMLClass umlClass) {
        this.umlClass = umlClass;
    }

    /**
     * setter
     *
     * @param xcoord a {@link java.lang.Double} object
     */
    public void setXcoord(Double xcoord) {
        Xcoord = xcoord;
    }

    /**
     * getter
     *
     * @return a {@link java.lang.Double} object
     */
    public Double getXcoord() {
        return Xcoord;
    }

    /**
     * constructor
     *
     * @param umlClass a {@link ija.ijaproject.cls.UMLClass} object
     * @param xcoord   a {@link java.lang.Double} object
     */
    public UMLSeqClass(String name, UMLClass umlClass, Double xcoord) {
        super(name);
        this.umlClass = umlClass;
        setXcoord(xcoord);
    }

    /**
     * check if it contains spaces
     * 
     * @return
     */
    public boolean isCorrect() {
        return !name.contains(" ");
    }

}
