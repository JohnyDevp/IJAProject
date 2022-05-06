package ija.ijaproject.cls;

/**
 * <p>
 * UMLSeqClass class.
 * </p>
 *
 * @author musta-pollo
 * @version 1.1
 */
public class UMLSeqClass extends Element {

    public transient UMLClass umlClass;

    public String umlClassName;

    public Double Xcoord;

    public Integer indexOfInstance = 0;

    /**
     * <p>
     * Constructor for UMLSeqClass.
     * </p>
     */
    public UMLSeqClass() {
    }

    /**
     * <p>
     * storeClassName.
     * </p>
     */
    public void storeClassName() {
        umlClassName = umlClass.name;
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
     * constructor - when non-existing uml object is add in sequence diagram
     *
     * @param name   a {@link java.lang.String} object
     * @param xcoord a {@link java.lang.Double} object
     */
    public UMLSeqClass(String name, Double xcoord) {
        super(name);
        setXcoord(xcoord);
        // create temporary uml class - with the name of this class
        this.umlClass = new UMLClass(name);
    }

    /**
     * constructor
     *
     * @param umlClass a {@link ija.ijaproject.cls.UMLClass} object
     * @param xcoord   a {@link java.lang.Double} object
     */
    public UMLSeqClass(UMLClass umlClass, Double xcoord) {
        super(umlClass.getName());
        this.umlClass = umlClass;
        setXcoord(xcoord);
    }

}
