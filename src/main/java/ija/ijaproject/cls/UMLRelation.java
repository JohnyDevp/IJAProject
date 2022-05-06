package ija.ijaproject.cls;

/**
 * class representing uml relation between uml classes and interfaces
 *
 * @author xzimol04, xholan11
 * @version 1.1
 */
public class UMLRelation extends Element {
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "UMLRelation{" +
                "startX=" + startX +
                ", startY=" + startY +
                ", endX=" + endX +
                ", endY=" + endY +
                '}';
    }

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

    /**
     * 
     * Constructor for UMLRelation.
     * 
     */
    public UMLRelation() {
    }

    /**
     * 
     * setRelationNames.
     * 
     */
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

    /**
     * getter
     *
     * @return a double
     */
    public double getStartX() {
        return startX;
    }

    /**
     * 
     * Getter for the field <code>startY</code>.
     * 
     *
     * @return a double
     */
    public double getStartY() {
        return startY;
    }

    /**
     * 
     * Getter for the field <code>endX</code>.
     * 
     *
     * @return a double
     */
    public double getEndX() {
        return endX;
    }

    /**
     * 
     * Getter for the field <code>endY</code>.
     * 
     *
     * @return a double
     */
    public double getEndY() {
        return endY;
    }

    /**
     * setter
     *
     * @param relationFromObject a
     *                           {@link ija.ijaproject.cls.UMLClassInterfaceTemplate}
     *                           object
     */
    public void setRelationFromObject(UMLClassInterfaceTemplate relationFromObject) {
        this.relationFromObject = relationFromObject;
    }

    /**
     * setter
     *
     * @param cardinalityByFromClass a {@link java.lang.String} object
     */
    public void setCardinalityByFromClass(String cardinalityByFromClass) {
        this.cardinalityByFromClass = cardinalityByFromClass;
    }

    /**
     * setter
     *
     * @param cardinalityByToClass a {@link java.lang.String} object
     */
    public void setCardinalityByToClass(String cardinalityByToClass) {
        this.cardinalityByToClass = cardinalityByToClass;
    }

    /**
     * setter
     *
     * @param relationToObject a
     *                         {@link ija.ijaproject.cls.UMLClassInterfaceTemplate}
     *                         object
     */
    public void setRelationToObject(UMLClassInterfaceTemplate relationToObject) {
        this.relationToObject = relationToObject;
    }

    /**
     * setter
     *
     * @param relationType a {@link ija.ijaproject.cls.UMLRelation.RelationType}
     *                     object
     */
    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    /**
     * setter
     *
     * @param startX a double
     */
    public void setStartX(double startX) {
        this.startX = startX;
    }

    /**
     * setter
     *
     * @param startY a double
     */
    public void setStartY(double startY) {
        this.startY = startY;
    }

    /**
     * setter
     *
     * @param endX a double
     */
    public void setEndX(double endX) {
        this.endX = endX;
    }

    /**
     * setter
     *
     * @param endY a double
     */
    public void setEndY(double endY) {
        this.endY = endY;
    }
}
