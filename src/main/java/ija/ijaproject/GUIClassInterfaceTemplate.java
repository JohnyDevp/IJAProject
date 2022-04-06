package ija.ijaproject;

import ija.ijaproject.cls.UMLClass;
import ija.ijaproject.cls.UMLClassInterfaceTemplate;
import ija.ijaproject.cls.UMLOperation;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * class for creating new graphic representation of class
 * */
public abstract class GUIClassInterfaceTemplate {

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

    /**
     * constructor for creating the class object
     * @param ucit instance of UMLclass/UMLinterface
     * */
    public GUIClassInterfaceTemplate(UMLClassInterfaceTemplate ucit){
        this.name = ucit.getName();

    }

    /**
     * creating all graphical objects for necessary for empty class
     */
    public void createClassObjectGUI(){
        //create border of the object
        Rectangle rectangleBorder = new Rectangle(100,130, Color.BLACK);
        rectangleBorder.setX(getXcoord());
        rectangleBorder.setY(getYcoord());
        rectangleBorder.setCursor(Cursor.CROSSHAIR);
        this.classBorder = rectangleBorder;

        //create overall classbox => of rectangle
        Rectangle rectangle = new Rectangle(90,120,Color.rgb(237, 233, 221, 0.6));
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
        line2.setStartY(line1.getStartY() + 30);
        line2.setEndX(rectangle.getX() + rectangle.getWidth());
        line2.setEndY(line2.getStartY());
        this.line2 = line2;
    }

    /**
     * @param umlOperation UMLOperation object => stores all information about this operation
     * method for adding opperation to class diagram graphical representation
     * */
    public Text addOperation(UMLOperation umlOperation){
        //set the text of label in graphical representation of class

        Text operation = new Text(umlOperation.getName());

        if (listOfOperations.isEmpty()){
            operation.setY(this.getLine2().getStartY() + 15);
            operation.setX(this.getClassNameLabel().getX());
        }
        else {
            Text lastOp = listOfOperations.get(listOfOperations.size() -1 );
            operation.setY(lastOp.getY() + 15);
            operation.setX(lastOp.getX());
        }

        listOfOperations.add(operation);
        classBox.setHeight(classBox.getHeight() + 15);
        return operation;
    }

    /**
     * @param relation relation representing objects in the relation and relations data
     * */
    public void addRelation(RelationGUI relation){
        this.listOfRelations.add(relation);
    }

}
