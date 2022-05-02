package ija.ijaproject.cls;

/**
 * class representing uml relation between uml classes and interfaces
 * 
 * @author xzimol04
 */
public class UMLRelation extends Element {
    /**
     * type accesible through all classes to determine type of relation
     */
    public enum RelationType {
        ASSOCIATION, AGGREGATION, GENERALIZATION, COMPOSITION
    };

    public String relationFromObjectName;
    public String relationToObjectName;
    public transient UMLClassInterfaceTemplate relationFromObject;
    public transient UMLClassInterfaceTemplate relationToObject;
    public RelationType relationType;
    public String cardinalityByFromClass;
    public String cardinalityByToClass;

    // graphical points
    public double startX, startY;
    public double endX, endY;

    public UMLRelation() {
    }

    public void setRelationNames() {
        relationFromObjectName = relationFromObject.name;
        relationToObjectName = relationToObject.name;
    }

    /**
     * constructor
     * 
     * @param name name of relation
     */
    public UMLRelation(String name) {
        super(name);
        // set the coordinates to values which indicates that it is out of pane - not
        // defined
        setStartX(-1.0);
        setStartY(-1.0);
        setEndX(-1.0);
        setEndY(-1.0);
    }

    /**
     * constructor
     * 
     * @param name               name of relation
     * @param relationFromObject interfaces or class where the relation begins
     * @param relationToObject   interface or class where the relation ends
     * @param type               type of relation according to RelationType defined
     *                           above in this class
     */
    public UMLRelation(String name, UMLClassInterfaceTemplate relationFromObject,
            UMLClassInterfaceTemplate relationToObject, RelationType type) {
        super(name);
        this.relationFromObject = relationFromObject;
        this.relationToObject = relationToObject;
        this.relationType = type;
    }

    /**
     * getter
     * 
     * @return object where the relation begins
     */
    public UMLClassInterfaceTemplate getRelationFromObject() {
        return relationFromObject;
    }

    /**
     * getter
     * 
     * @return object where the relation ends
     */
    public UMLClassInterfaceTemplate getRelationToObject() {
        return relationToObject;
    }

    /**
     * getter
     * 
     * @return type of relation
     */
    public RelationType getRelationType() {
        return relationType;
    }

    /**
     * getter
     * 
     * @return cardinality visible by ending relation object
     */
    public String getCardinalityByFromClass() {
        return cardinalityByFromClass;
    }

    /**
     * getter
     * 
     * @return cardinality visible by starting relation object
     */
    public String getCardinalityByToClass() {
        return cardinalityByToClass;
    }

    /** getter */
    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getEndX() {
        return endX;
    }

    public double getEndY() {
        return endY;
    }

    /**
     * setter
     */
    public void setRelationFromObject(UMLClassInterfaceTemplate relationFromObject) {
        this.relationFromObject = relationFromObject;
    }

    /** setter */
    public void setCardinalityByFromClass(String cardinalityByFromClass) {
        this.cardinalityByFromClass = cardinalityByFromClass;
    }

    /** setter */
    public void setCardinalityByToClass(String cardinalityByToClass) {
        this.cardinalityByToClass = cardinalityByToClass;
    }

    /** setter */
    public void setRelationToObject(UMLClassInterfaceTemplate relationToObject) {
        this.relationToObject = relationToObject;
    }

    /** setter */
    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    /** setter */
    public void setStartX(double startX) {
        this.startX = startX;
    }

    /** setter */
    public void setStartY(double startY) {
        this.startY = startY;
    }

    /** setter */
    public void setEndX(double endX) {
        this.endX = endX;
    }

    /** setter */
    public void setEndY(double endY) {
        this.endY = endY;
    }
}
