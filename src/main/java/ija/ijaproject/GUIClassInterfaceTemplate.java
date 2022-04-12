
package ija.ijaproject;

import ija.ijaproject.cls.*;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * template for class and interface fui representation
 * @author xholan11
 * */
public abstract class GUIClassInterfaceTemplate {

    /**
     * object representing intern class/interface representation
     * */
    protected UMLClassInterfaceTemplate object;

    /**map for operation - its uml and graphical representation*/
    private Map<UMLOperation, Text> mapOfOperations = new HashMap<UMLOperation, Text>();

    /**
     * starting position on canvas
     * */
    private double Xcoord = 0.0, Ycoord = 0.0;
    private String name;

    /**graphical parts of class object*/
    private Rectangle classBox;
    private Rectangle classBorder;
    private Rectangle clickableCorner;
    private Text classNameLabel;
    private Line line1;
    private List<Text> listOfAttributes = new ArrayList<>();
    private Line line2;
    private List<Text> listOfOperations = new ArrayList<>();
    private List<RelationGUI> listOfRelations = new ArrayList<>();

    /**setters*/
    public void setXcoord(double xcoord) {this.Xcoord = xcoord;}
    public void setYcoord(double ycoord) {this.Ycoord = ycoord;    }
    public void setName(String name) {this.name = name;}

    /**
     * getters
     * */
    public UMLClassInterfaceTemplate getObject() {return this.object; }
    public double getXcoord() {return this.Xcoord;}
    public double getYcoord() {return  this.Ycoord;}
    public Text getClassNameLabel() {return this.classNameLabel; }
    public Rectangle getClassBorder() {return this.classBorder;}
    public Rectangle getClassBox(){ return this.classBox;}
    public Rectangle getClickableCorner() {return this.clickableCorner;}
    public List<Text> getListOfOperations() {return this.listOfOperations;}
    public List<RelationGUI> getListOfRelations() {return this.listOfRelations;}
    public Line getLine1() { return this.line1; }
    public Line getLine2() { return this.line2; }

    private final Color selectedClassColor = Color.rgb(227, 68, 36);
    private final Color deselectedClassColor = Color.rgb(80, 95, 230);

    public Map<UMLOperation, Text> getMapOfOperations() {
        return mapOfOperations;
    }

    /**
     * constructor for creating the class object
     * @param umlClassInterfaceTemplate instance of UMLclass/UMLinterface
     * */
    public GUIClassInterfaceTemplate(UMLClassInterfaceTemplate umlClassInterfaceTemplate){
        //set the intern object representation
        this.object = umlClassInterfaceTemplate;

        //set necessary information about class/interface
        setName(umlClassInterfaceTemplate.getName());
        this.setXcoord(umlClassInterfaceTemplate.getXcoord());
        this.setYcoord(umlClassInterfaceTemplate.getYcoord());

        //create graphical representation
        this.createClassInterfaceObjectGUI();

        //add operations
        for (UMLOperation umlOperation : umlClassInterfaceTemplate.getUmlOperationList()){
            this.addOperationFromConstructor(umlOperation);
        }
    }

    /**
     * creating all graphical objects for necessary for empty class
     */
    protected void createClassInterfaceObjectGUI(){
        //create border of the object
        Rectangle rectangleBorder = new Rectangle(100,90, Color.BLACK);
        rectangleBorder.setX(getXcoord());
        rectangleBorder.setY(getYcoord());
        rectangleBorder.setCursor(Cursor.CROSSHAIR);
        this.classBorder = rectangleBorder;

        //create overall classbox - of rectangle
        Rectangle rectangle = new Rectangle(90,80,Color.rgb(237, 233, 221, 0.6));
        rectangle.setX(getXcoord() + 5);
        rectangle.setY(getYcoord() + 5);
        this.classBox = rectangle;

        //sets the class name
        Text className = new Text(this.name);
        className.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
        className.setY(rectangle.getY() + 35);
        className.setX(rectangle.getX() + 5);
        this.classNameLabel = className;

        //resizing both rectangle and its border
        rectangle.setWidth(className.getLayoutBounds().getWidth()+ 10);
        rectangleBorder.setWidth(className.getLayoutBounds().getWidth()+ 20);

        //create clickable corner
        Rectangle clickableCorner = new Rectangle(rectangleBorder.getWidth() / 2,20, deselectedClassColor);
        clickableCorner.setX(rectangleBorder.getX() + rectangleBorder.getWidth()/2 - clickableCorner.getWidth()/2 );
        clickableCorner.setY(rectangle.getY());
        clickableCorner.setCursor(Cursor.MOVE);
        this.clickableCorner = clickableCorner;

        //sets the two lines which will divide the space of classbox to three parts
        // 1) class name 2) class attributes 3) class operations
        Line line1 = new Line();
        line1.setStartX(rectangle.getX());
        line1.setStartY(className.getY() + 15);
        line1.setEndX(rectangle.getX() + rectangle.getWidth());
        line1.setEndY(line1.getStartY());
        this.line1 = line1;

        Line line2 = new Line();
        line2.setStartX(rectangle.getX());
        line2.setStartY(line1.getStartY() + 15);
        line2.setEndX(rectangle.getX() + rectangle.getWidth());
        line2.setEndY(line2.getStartY());
        this.line2 = line2;
    }

    /**
     * @param umlOperation UMLOperation object - stores all information about this operation
     * method for adding opperation to class diagram graphical representation
     * @return null if the operation is bad considering other operations (same name-different type or whole the same)
     *         or the Text element representing the operation
     * */
    public Text addOperation(UMLOperation umlOperation){

        //if the operation with the name is already there then it will fail and return null
        if (!this.object.addOperation(umlOperation)) {return null;}

       return addOperationFromConstructor(umlOperation);
    }

    /**
     * method for resizing class width
     * resizing width of class gui iff necessary (according to text width)
     * */
    protected void resizeClassWidth(double width){
        if (width < getClassBox().getWidth() + 5){ return ;}

        //center class name
        this.getClassNameLabel().setX(
                this.getClassBorder().getX() + ((width+20) / 2) - getClassNameLabel().getLayoutBounds().getWidth()/2
        );

        //clickable corner
        this.getClickableCorner().setX(
                this.getClassBorder().getX() + ((width+20) / 2) - getClickableCorner().getWidth()/2
        );

        //box and border
        this.getClassBox().setWidth(width + 10);
        this.getClassBorder().setWidth(width + 20);

        //lines
        this.getLine1().setEndX(getLine1().getStartX() + width + 10);
        this.getLine2().setEndX(getLine2().getStartX() + width + 10);
    }

    /**
     * @param relation relation representing objects in the relation and relations data
     * */
    public void addRelation(RelationGUI relation){
        this.listOfRelations.add(relation);
    }

    /**
     * method which goes through relations and when it detects a generalization it will mark methods, which are overridden*/
    public void markOperationsWhileGeneralization(Pane canvas){
        for (RelationGUI relationGUI : getListOfRelations()){
            //todo
        }
    }

    /**
     * method only for constructor
     * - non-adding operation to operation list of uml class - already there
     * */
    private Text addOperationFromConstructor(UMLOperation umlOperation){
        //set the text of label in graphical representation of class
        StringBuilder textOfOperation = new StringBuilder(umlOperation.getModifier() + umlOperation.getName() + " (");
        for (UMLAttribute umlParam : umlOperation.getParametersOfOperationList()){
            textOfOperation.append(umlParam.getName()).append(" : ").append(umlParam.getType());
        }
        textOfOperation.append(") : ").append(umlOperation.getType());

        //set text of operation (see on canvas)
        Text operation = new Text(textOfOperation.toString());


        if (listOfOperations.isEmpty()){
            operation.setY(this.getLine2().getStartY() + 15);
            operation.setX(this.getClassNameLabel().getX());
        }
        else {
            Text lastOp = listOfOperations.get(listOfOperations.size() -1 );
            operation.setY(lastOp.getY() + 15);
            operation.setX(lastOp.getX());
        }

        //add operation to list of operations
        listOfOperations.add(operation);

        //reset the class height of border and box
        getClassBox().setHeight(getClassBox().getHeight() + 15);
        getClassBorder().setHeight(getClassBorder().getHeight() + 15);

        //resize classbox iff necessary
        resizeClassWidth(operation.getLayoutBounds().getWidth());

        //map operation
        this.mapOfOperations.put(umlOperation, operation);

        return operation;
    }

}
