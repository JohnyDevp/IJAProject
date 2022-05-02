package ija.ijaproject.cls;

public class UMLSeqClass extends Element {

    public UMLClass umlClass;

    public Double Xcoord;

    public Integer indexOfInstance = 0;

    public UMLSeqClass() {
    }

    /** getter */
    public UMLClass getUmlClass() {
        return umlClass;
    }

    /** setter */
    public void setUmlClass(UMLClass umlClass) {
        this.umlClass = umlClass;
    }

    /** setter */
    public void setXcoord(Double xcoord) {
        Xcoord = xcoord;
    }

    /** getter */
    public Double getXcoord() {
        return Xcoord;
    }

    /** constructor - when non-existing uml object is add in sequence diagram */
    public UMLSeqClass(String name, Double xcoord) {
        super(name);
        setXcoord(xcoord);
        // create temporary uml class - with the name of this class
        this.umlClass = new UMLClass(name);
    }

    /** constructor */
    public UMLSeqClass(UMLClass umlClass, Double xcoord) {
        super(umlClass.getName());
        this.umlClass = umlClass;
        setXcoord(xcoord);
    }

}
