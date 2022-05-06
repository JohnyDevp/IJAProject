package ija.ijaproject;

import ija.ijaproject.cls.UMLClassInterfaceTemplate;
import ija.ijaproject.cls.UMLRelation;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * class for storing the relation between two classes
 *
 * @author xholan11, xzimol04
 * @version 1.1
 */
public class RelationGUI {

    private boolean relationFromSet = false;
    private UMLRelation.RelationType relationType;
    private UMLRelation umlRelation;
    private Pane canvas;

    private UMLClassInterfaceTemplate relClassFrom;
    private GUIClassInterfaceTemplate relClassFromGUI;
    private UMLClassInterfaceTemplate relClassTo;
    private GUIClassInterfaceTemplate relClassToGUI;

    private Line relLine;

    private Polygon relLineEnd;
    // these to lines are here for the option of association relation - which is
    // created of one simple arrow
    private Line line1 = null;
    private Line line2 = null;

    // labels on the relation line
    private Text cardinalityByToClass;
    private Text cardinalityByFromClass;
    private Text nameOfRelation;

    /**
     * constructor used when loading from while - everything is known
     *
     * @param umlRelation representation of relation
     *                    creating the line and its event for handling selecting
     *                    this line
     *                    setting up the relation type
     * @param canvas      a {@link javafx.scene.layout.Pane} object
     */
    public RelationGUI(UMLRelation umlRelation, Pane canvas) {
        // set the canvas
        this.canvas = canvas;
        // set up the line and the event when click on the relation
        this.relLine = new Line();
        this.relLine.setStrokeWidth(2.5);
        this.relLine.toBack();
        this.relLine.setCursor(Cursor.HAND);

        // set type of relation
        this.relationType = umlRelation.getRelationType();
        this.umlRelation = umlRelation;

        /*
         * //set relation start and end objects
         * setRelationFrom(umlRelation.getRelationFromObject(),null,
         * umlRelation.getStartX(), umlRelation.getStartY());
         * setRelationTo(umlRelation.getRelationToObject(), null, umlRelation.getEndX(),
         * umlRelation.getEndY());
         */
        // set relation attributes
        setNameOfRelation(umlRelation.getName());
        setCardinalityByFromClass(umlRelation.getCardinalityByFromClass());
        setCardinalityByToClass(umlRelation.getCardinalityByToClass());
    }

    /**
     * constructor used when relation created in gui
     *
     * @param type   a {@link ija.ijaproject.cls.UMLRelation.RelationType} object
     * @param canvas a {@link javafx.scene.layout.Pane} object
     */
    public RelationGUI(UMLRelation.RelationType type, Pane canvas) {
        // set up the line and the event when click on the relation
        this.relLine = new Line();
        this.relLine.setStrokeWidth(2.5);
        this.relLine.toBack();
        this.relLine.setCursor(Cursor.HAND);

        // set the type of this relation
        this.relationType = type;
        // create the line from sufficient places

        this.canvas = canvas;
    }

    /**
     * set uml relation if it hasnt been set yet
     *
     * @param umlRelation a {@link ija.ijaproject.cls.UMLRelation} object
     */
    public void setUmlRelation(UMLRelation umlRelation) {
        this.umlRelation = umlRelation;
    }

    /**
     * set relation type for gui and also graphical representation
     *
     * @param relationType a {@link ija.ijaproject.cls.UMLRelation.RelationType}
     *                     object
     */
    public void setRelationType(UMLRelation.RelationType relationType) {
        this.relationType = relationType;
        this.umlRelation.setRelationType(relationType);
        generalizationStateHandling();
    }

    /**
     * set all information about relation beginning
     *
     * @param relClassFrom    class that is at beginning of this relation
     * @param X               X coordinate of the point where the relation starts
     * @param Y               Y coordinate of the point where the relation starts
     * @param relClassFromGUI a {@link ija.ijaproject.GUIClassInterfaceTemplate}
     *                        object
     */
    public void setRelationFrom(UMLClassInterfaceTemplate relClassFrom, GUIClassInterfaceTemplate relClassFromGUI,
            double X, double Y) {
        this.relLine.setStartX(X);
        this.relLine.setStartY(Y);
        this.relationFromSet = true;
        this.relClassFrom = relClassFrom;
        this.relClassFromGUI = relClassFromGUI;
    }

    /**
     * set all information about relation ending
     * also creating sufficient line ending
     *
     * @param relClassTo    class that is at end of this relation
     * @param X             X coordinate of the point where the relation ends
     * @param Y             Y coordinate of the point where the relation ends
     * @param relClassToGUI a {@link ija.ijaproject.GUIClassInterfaceTemplate}
     *                      object
     */
    public void setRelationTo(UMLClassInterfaceTemplate relClassTo, GUIClassInterfaceTemplate relClassToGUI, double X,
            double Y) {
        this.relLine.setEndX(X);
        this.relLine.setEndY(Y);
        this.relClassTo = relClassTo;
        this.relClassToGUI = relClassToGUI;
        // set up the polygon representing the relation line ending
        this.relLineEnd = new Polygon();

        this.canvas.getChildren().add(getRelLine());
        // check whether the end isn't interface, so the start object would have colored
        // operations(method) when the
        // creates relation line ending
        setNewRelLineEndPosition();

        // if there is a generalization then handle overridden methods
        generalizationStateHandling();

    }

    /**
     * when the relation is generalization it is necessary to check for the
     * overriding methods
     */
    public void generalizationStateHandling() {
        if (relClassToGUI == null || relClassFromGUI == null)
            return;

        for (Text txtOperation : getRelClassFromGUI().getListOfOperations()) {
            // if there is going for generalization
            if (getRelationType() == UMLRelation.RelationType.GENERALIZATION) {
                for (Text txtOverrideOperation : getRelClassToGUI().getListOfOperations()) {
                    if (txtOperation.getText().substring(1).equals(txtOverrideOperation.getText().substring(1))) {
                        txtOperation.setFill(Color.ORANGE);
                    }
                }
            } else {
                txtOperation.setFill(Color.BLACK);
            }
        }
    }

    /**
     * method for moving the relation on canvas
     *
     * @param classObject a {@link ija.ijaproject.cls.UMLClassInterfaceTemplate}
     *                    object
     * @param diffX       a double
     * @param diffY       a double
     */
    public void recomputeRelationDesign(UMLClassInterfaceTemplate classObject, double diffX, double diffY) {
        // decide what end position of relation to change
        if (getRelClassFrom() == classObject) {
            getRelLine().setStartX(getRelLine().getStartX() + diffX);
            getRelLine().setStartY(getRelLine().getStartY() + diffY);
        } else {
            getRelLine().setEndX(getRelLine().getEndX() + diffX);
            getRelLine().setEndY(getRelLine().getEndY() + diffY);
        }

        // reset coordinates of relation in its intern representation
        this.getUmlRelation().setStartX(getRelLine().getStartX());
        this.getUmlRelation().setStartY(getRelLine().getStartY());
        this.getUmlRelation().setEndX(getRelLine().getEndX());
        this.getUmlRelation().setEndY(getRelLine().getEndY());

        // set new position of the line endings (explicitly said the arrow)
        setNewRelLineEndPosition();

        // reset also the cardinalities and the name of the relation
        if (getNameOfRelation() != null && !getNameOfRelation().getText().equals(""))
            setNameOfRelation(getNameOfRelation().getText());
        if (getCardinalityByFromClass() != null && getCardinalityByToClass() != null
                && !(getCardinalityByFromClass().getText().equals("")
                        && getCardinalityByToClass().getText().equals(""))) {
            setCardinalityByFromClass(getCardinalityByFromClass().getText());
            setCardinalityByToClass(getCardinalityByToClass().getText());
        }

    }

    /**
     * set the ending polygon which sets the type of the relation
     * or arrow which is compound of two lines
     */
    public void setNewRelLineEndPosition() {
        // remove the rel line ending if already on canvas
        this.canvas.getChildren().removeAll(getRelLineEnd(), line1, line2);
        this.relLineEnd = new Polygon();

        double slope = (this.relLine.getStartY() - this.relLine.getEndY())
                / (this.relLine.getStartX() - this.relLine.getEndX());
        double lineAngle = Math.atan(slope);
        double lineLength = Math.sqrt(Math.pow((this.relLine.getStartY() - this.relLine.getEndY()), 2)
                + Math.pow((this.relLine.getStartX() - this.relLine.getEndX()), 2));
        double arrowAngle, arrowLength, arrowWide;

        switch (this.relationType) {
            // filled arrow ("big")
            case GENERALIZATION: {
                System.out.println("generalization");
                arrowAngle = this.relLine.getStartX() > this.relLine.getEndX() ? Math.toRadians(45)
                        : -Math.toRadians(225);
                arrowLength = 20;
                this.relLineEnd.getPoints().addAll(
                        // the aim
                        this.relLine.getEndX(), this.relLine.getEndY(),
                        // left corner
                        arrowLength * Math.cos(lineAngle - arrowAngle) + this.relLine.getEndX(),
                        arrowLength * Math.sin(lineAngle - arrowAngle) + this.relLine.getEndY(),
                        // right corner
                        arrowLength * Math.cos(lineAngle + arrowAngle) + this.relLine.getEndX(),
                        arrowLength * Math.sin(lineAngle + arrowAngle) + this.relLine.getEndY());
                this.relLineEnd.setStroke(Color.BLACK);
                this.relLineEnd.setFill(Color.WHITE);
            }
                break;

            // white filled 4-point-polygon
            case AGGREGATION: {
                System.out.println("aggregation");
                arrowAngle = this.relLine.getStartX() > this.relLine.getEndX() ? Math.toRadians(45)
                        : -Math.toRadians(225);
                arrowLength = 15;
                arrowWide = 8;
                double u1 = this.relLine.getEndX() - this.relLine.getStartX();
                double u2 = this.relLine.getEndY() - this.relLine.getStartY();
                double Ax = this.relLine.getEndX();
                double Ay = this.relLine.getEndY();
                double resultX = Ax - u1 * (30 / lineLength);
                double resultY = Ay - u2 * (30 / lineLength);

                this.relLineEnd.getPoints().addAll(
                        // the aim
                        this.relLine.getEndX(), this.relLine.getEndY(),
                        // left corner
                        (arrowLength) * Math.cos(lineAngle - arrowAngle) + this.relLine.getEndX(),
                        arrowWide * Math.sin(lineAngle - arrowAngle) + this.relLine.getEndY(),
                        // the last point
                        resultX, resultY,
                        // right corner
                        (arrowLength) * Math.cos(lineAngle + arrowAngle) + this.relLine.getEndX(),
                        arrowWide * Math.sin(lineAngle + arrowAngle) + this.relLine.getEndY());
                this.relLineEnd.setStroke(Color.BLACK);
                this.relLineEnd.setFill(Color.WHITE);
            }
                break;

            // black normal arrow
            case ASSOCIATION: {
                System.out.println("association");
                arrowAngle = this.relLine.getStartX() > this.relLine.getEndX() ? Math.toRadians(45)
                        : -Math.toRadians(225);
                arrowLength = 21;
                arrowWide = 10;
                Line line1 = new Line(
                        (arrowLength) * Math.cos(lineAngle - arrowAngle) + this.relLine.getEndX(),
                        arrowWide * Math.sin(lineAngle - arrowAngle) + this.relLine.getEndY(),
                        this.relLine.getEndX(),
                        this.relLine.getEndY());
                Line line2 = new Line(
                        (arrowLength) * Math.cos(lineAngle + arrowAngle) + this.relLine.getEndX(),
                        arrowWide * Math.sin(lineAngle + arrowAngle) + this.relLine.getEndY(),
                        this.relLine.getEndX(),
                        this.relLine.getEndY());
                line1.setStrokeWidth(2.5);
                line2.setStrokeWidth(2.5);

                // adding both lines to canvas
                // and removing old ones, if exists
                // warning it has to be here
                if (this.line1 != null)
                    canvas.getChildren().remove(this.line1);
                if (this.line2 != null)
                    canvas.getChildren().remove(this.line2);
                canvas.getChildren().add(line1);
                canvas.getChildren().add(line2);
                this.line1 = line1;
                this.line2 = line2;
            }
                break;

            // black filled 4-point-polygon
            case COMPOSITION: {
                System.out.println("composition");
                arrowAngle = this.relLine.getStartX() > this.relLine.getEndX() ? Math.toRadians(45)
                        : -Math.toRadians(225);
                arrowLength = 15;
                arrowWide = 8;
                double u1 = this.relLine.getEndX() - this.relLine.getStartX();
                double u2 = this.relLine.getEndY() - this.relLine.getStartY();
                double Ax = this.relLine.getEndX();
                double Ay = this.relLine.getEndY();
                double resultX = Ax - u1 * (30 / lineLength);
                double resultY = Ay - u2 * (30 / lineLength);

                this.relLineEnd.getPoints().addAll(
                        // the aim
                        this.relLine.getEndX(), this.relLine.getEndY(),
                        // left corner
                        (arrowLength) * Math.cos(lineAngle - arrowAngle) + this.relLine.getEndX(),
                        arrowWide * Math.sin(lineAngle - arrowAngle) + this.relLine.getEndY(),
                        // the last point
                        resultX, resultY,
                        // right corner
                        (arrowLength) * Math.cos(lineAngle + arrowAngle) + this.relLine.getEndX(),
                        arrowWide * Math.sin(lineAngle + arrowAngle) + this.relLine.getEndY());
                this.relLineEnd.setStroke(Color.BLACK);
                this.relLineEnd.setFill(Color.BLACK);
            }
                break;
        }

        this.canvas.getChildren().add(getRelLineEnd());
    }

    /**
     * 
     * Setter for the field <code>nameOfRelation</code>.
     * 
     *
     * @param name a {@link java.lang.String} object
     */
    public void setNameOfRelation(String name) {
        // remove the label with name of relation if has been set already
        this.canvas.getChildren().remove(this.getNameOfRelation());

        Text text = new Text();

        double lineLength = Math.sqrt(Math.pow((this.relLine.getStartY() - this.relLine.getEndY()), 2)
                + Math.pow((this.relLine.getStartX() - this.relLine.getEndX()), 2));
        double u1 = this.relLine.getEndX() - this.relLine.getStartX();
        double u2 = this.relLine.getEndY() - this.relLine.getStartY();
        double Ax = this.relLine.getEndX();
        double Ay = this.relLine.getEndY();
        double resultX = Ax - u1 * (0.5);
        double resultY = Ay - u2 * (0.5);

        text.setX(resultX);
        text.setY(resultY);
        // text.setFill(Color.WHITE);
        text.setText(name);
        text.setStyle("-fx-background-color: red");
        text.setFont(Font.font("verdana", 15));
        System.out.println(text.toString() + " " + lineLength + " " + u1 + " " + u2 + " " + Ax + " " + Ay);
        System.out.println(resultX + " " + resultY);
        text.toFront();
        this.nameOfRelation = text;
        canvas.getChildren().add(text);
    }

    /**
     * 
     * Setter for the field <code>cardinalityByToClass</code>.
     * 
     *
     * @param cardinality a {@link java.lang.String} object
     */
    public void setCardinalityByToClass(String cardinality) {
        // remove from canvas the label with cardinality, if set already
        this.canvas.getChildren().remove(getCardinalityByToClass());

        Text text = new Text();

        // computed values for choose the right place on the relation line for the text
        // label
        // used parametric representation of line
        double lineLength = Math.sqrt(Math.pow((this.relLine.getStartY() - this.relLine.getEndY()), 2)
                + Math.pow((this.relLine.getStartX() - this.relLine.getEndX()), 2));
        double u1 = this.relLine.getEndX() - this.relLine.getStartX();
        double u2 = this.relLine.getEndY() - this.relLine.getStartY();
        double Ax = this.relLine.getEndX();
        double Ay = this.relLine.getEndY();
        // this place choosing coordinates at the specific point at the line
        double resultX = Ax - u1 * (40 / lineLength);
        double resultY = Ay - u2 * (20 / lineLength);

        // set the label
        text.setX(resultX);
        text.setY(resultY);
        // todo check cardinality format
        text.setText(cardinality);
        text.setFont(Font.font("verdana", 15));
        text.toFront();
        this.cardinalityByToClass = text;
        canvas.getChildren().add(text);
    }

    /**
     * 
     * Setter for the field <code>cardinalityByFromClass</code>.
     * 
     *
     * @param cardinality a {@link java.lang.String} object
     */
    public void setCardinalityByFromClass(String cardinality) {
        // remove the label with cardinality from canvas if it has been set already
        this.canvas.getChildren().remove(getCardinalityByFromClass());

        Text text = new Text();

        // computed values for choose the right place on the relation line for the text
        // label
        // used parametric representation of line
        double lineLength = Math.sqrt(Math.pow((this.relLine.getStartY() - this.relLine.getEndY()), 2)
                + Math.pow((this.relLine.getStartX() - this.relLine.getEndX()), 2));
        double u1 = this.relLine.getEndX() - this.relLine.getStartX();
        double u2 = this.relLine.getEndY() - this.relLine.getStartY();
        double Ax = this.relLine.getEndX();
        double Ay = this.relLine.getEndY();
        // this place choosing coordinates at the specific point at the line
        double resultX = Ax - u1 * ((lineLength - 20) / lineLength);
        double resultY = Ay - u2 * ((lineLength - 20) / lineLength);

        // set the label
        text.setX(resultX);
        text.setY(resultY);
        // todo check cardinality format
        text.setText(cardinality);
        text.setFont(Font.font("verdana", 15));
        text.toFront();
        this.cardinalityByFromClass = text;
        canvas.getChildren().add(text);
    }

    /**
     * function for removal relation from canvas
     */
    public void removeFromCanvas() {
        this.canvas.getChildren().removeAll(
                this.getRelLine(),
                this.getNameOfRelation(),
                this.getCardinalityByToClass(),
                this.getCardinalityByFromClass(),
                this.getRelLineEnd(),
                this.line1,
                this.line2);

    }

    /**
     * getters
     *
     * @return a {@link javafx.scene.text.Text} object
     */
    public Text getNameOfRelation() {
        return this.nameOfRelation;
    }

    /**
     * 
     * Getter for the field <code>umlRelation</code>.
     * 
     *
     * @return a {@link ija.ijaproject.cls.UMLRelation} object
     */
    public UMLRelation getUmlRelation() {
        return umlRelation;
    }

    /**
     * 
     * Getter for the field <code>cardinalityByToClass</code>.
     * 
     *
     * @return a {@link javafx.scene.text.Text} object
     */
    public Text getCardinalityByToClass() {
        return this.cardinalityByToClass;
    }

    /**
     * 
     * Getter for the field <code>cardinalityByFromClass</code>.
     * 
     *
     * @return a {@link javafx.scene.text.Text} object
     */
    public Text getCardinalityByFromClass() {
        return this.cardinalityByFromClass;
    }

    /**
     * 
     * Getter for the field <code>relationFromSet</code>.
     * 
     *
     * @return a boolean
     */
    public boolean getRelationFromSet() {
        return this.relationFromSet;
    }

    /**
     * 
     * Getter for the field <code>relationType</code>.
     * 
     *
     * @return a {@link ija.ijaproject.cls.UMLRelation.RelationType} object
     */
    public UMLRelation.RelationType getRelationType() {
        return relationType;
    }

    /**
     * 
     * Getter for the field <code>relClassFromGUI</code>.
     * 
     *
     * @return a {@link ija.ijaproject.GUIClassInterfaceTemplate} object
     */
    public GUIClassInterfaceTemplate getRelClassFromGUI() {
        return relClassFromGUI;
    }

    /**
     * 
     * Getter for the field <code>relClassFrom</code>.
     * 
     *
     * @return a {@link ija.ijaproject.cls.UMLClassInterfaceTemplate} object
     */
    public UMLClassInterfaceTemplate getRelClassFrom() {
        return this.relClassFrom;
    }

    /**
     * 
     * Getter for the field <code>relClassToGUI</code>.
     * 
     *
     * @return a {@link ija.ijaproject.GUIClassInterfaceTemplate} object
     */
    public GUIClassInterfaceTemplate getRelClassToGUI() {
        return relClassToGUI;
    }

    /**
     * 
     * Getter for the field <code>relClassTo</code>.
     * 
     *
     * @return a {@link ija.ijaproject.cls.UMLClassInterfaceTemplate} object
     */
    public UMLClassInterfaceTemplate getRelClassTo() {
        return this.relClassTo;
    }

    /**
     * 
     * Getter for the field <code>relLine</code>.
     * 
     *
     * @return a {@link javafx.scene.shape.Line} object
     */
    public Line getRelLine() {
        return this.relLine;
    }

    /**
     * 
     * Getter for the field <code>relLineEnd</code>.
     * 
     *
     * @return a {@link javafx.scene.shape.Polygon} object
     */
    public Polygon getRelLineEnd() {
        return this.relLineEnd;
    }
}
