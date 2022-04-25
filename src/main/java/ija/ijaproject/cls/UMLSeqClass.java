package ija.ijaproject.cls;

public class UMLSeqClass extends Element{

    private UMLClass umlClass;

    private Double Xcoord;


    /**getter*/
    public UMLClass getUmlClass() {
        return umlClass;
    }
    /**setter*/
    public void setUmlClass(UMLClass umlClass) {
        this.umlClass = umlClass;
    }
    /**setter*/
    public void setXcoord(Double xcoord) {
        Xcoord = xcoord;
    }
    /**getter*/
    public Double getXcoord() {
        return Xcoord;
    }

    /**constructor*/
    public UMLSeqClass(String name, Double xcoord){
        super(name);
        setXcoord(xcoord);
    }

    /**constructor*/
    public UMLSeqClass(UMLClass umlClass, Double xcoord){
        super(umlClass.getName());
        setXcoord(xcoord);
    }


}
