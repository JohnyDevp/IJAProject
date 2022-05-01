
package ija.ijaproject;

import ija.ijaproject.cls.UMLAttribute;
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

        //necessary to initialize attributes list if class takes part
        //this has to be here=> otherwise it will fail when resizing methods will be called
        if (this.object.getClass() == UMLClass.class){
            ((ClassObjectGUI)this).listOfAttributes = new ArrayList<>();
            ((ClassObjectGUI)this).mapOfAttributes = new HashMap<>();
        }

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
        this.classNameLabel = className;

        //create clickable corner
        Rectangle clickableCorner = new Rectangle(rectangleBorder.getWidth() / 2,20, deselectedClassColor);
        clickableCorner.setCursor(Cursor.MOVE);
        this.clickableCorner = clickableCorner;

        //sets the two lines which will divide the space of classbox to three parts
        // 1) class name 2) class attributes 3) class operations
        Line line1 = new Line();
        this.line1 = line1;

        Line line2 = new Line();
        this.line2 = line2;

        //resize class gui iff necessary
        resizeObjectGUI();
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

    /**remove operation and all its references*/
    public void removeOperation(UMLOperation umlOperation){
        //remove text representation
        this.listOfOperations.remove(this.mapOfOperations.get(umlOperation));

        //remove intern representation
        ((UMLClass)this.getObject()).deleteOperation(umlOperation.getName());

        //resize class gui iff necessary
        resizeObjectGUI();
    }

    /**
     * method for resizing class width
     * resizing width of class gui iff necessary (according to text width)
     * */
    protected void resizeObjectGUI(){
        //width of border
        double newWidth = 90;
        double curr_max = 0;
        double height = 105;

        //loop through all items which object consists of and decide whether to get smaller or weider
        if (this.getClassNameLabel().getLayoutBounds().getWidth() > curr_max) curr_max =this.getClassNameLabel().getLayoutBounds().getWidth();

        if (this.getClass() == InterfaceObjectGUI.class && ((InterfaceObjectGUI)this).getLabelOfInterface() != null) {
            if (((InterfaceObjectGUI)this).getLabelOfInterface().getLayoutBounds().getWidth() > curr_max) curr_max= ((InterfaceObjectGUI)this).getLabelOfInterface().getLayoutBounds().getWidth();
            height += 15;
        }
        if (this.getClass() == ClassObjectGUI.class){
            for (Text txtAttribute : ((ClassObjectGUI)this).getListOfAttributes()){
                if (txtAttribute.getLayoutBounds().getWidth() > curr_max) curr_max = txtAttribute.getLayoutBounds().getWidth();
                height += 15;
            }
        }
        for (Text txtOperation : this.getListOfOperations()){
            if (txtOperation.getLayoutBounds().getWidth() > curr_max) curr_max = txtOperation.getLayoutBounds().getWidth();
            height += 15;
        }

        //set desired width
        if (newWidth < curr_max) newWidth = curr_max;
        
        //resize=================================================
        newWidth += 20;
        //  border
        this.getClassBorder().setWidth(newWidth);
        this.getClassBorder().setHeight(height+10);
        double currentHeightFromBordersY=this.getClassBorder().getY();

        // box
        currentHeightFromBordersY += 5;
        this.getClassBox().setWidth(newWidth - 10);
        this.getClassBox().setHeight(height);
        this.getClassBox().setY(currentHeightFromBordersY);

        //clickable corner
        currentHeightFromBordersY += 0;
        this.getClickableCorner().setX(
                this.getClassBorder().getX() + (newWidth / 2) - getClickableCorner().getWidth()/2
        );
        this.getClickableCorner().setY(currentHeightFromBordersY);

        // class name
        currentHeightFromBordersY += 35;
        this.getClassNameLabel().setX(
                this.getClassBorder().getX() + (newWidth/ 2) - getClassNameLabel().getLayoutBounds().getWidth()/2
        );
        this.getClassNameLabel().setY(currentHeightFromBordersY);

        //possible label interface
        if (this.getClass() == InterfaceObjectGUI.class && ((InterfaceObjectGUI)this).getLabelOfInterface() != null) {
            currentHeightFromBordersY += 20;
            ((InterfaceObjectGUI)this).getLabelOfInterface().setY(currentHeightFromBordersY);
            ((InterfaceObjectGUI)this).getLabelOfInterface().setX(
                    this.getClassBorder().getX() + (newWidth/ 2) - ((InterfaceObjectGUI)this).getLabelOfInterface().getLayoutBounds().getWidth()/2
            );
        }

        //line 1
        currentHeightFromBordersY += 20;
        this.getLine1().setStartX(getClassBorder().getX());
        this.getLine1().setEndX(getClassBorder().getX() + newWidth);
        this.getLine1().setStartY(currentHeightFromBordersY);
        this.getLine1().setEndY(currentHeightFromBordersY);

        //attributes
        if (this.getClass() == ClassObjectGUI.class){
            for (Text txtAttribute : ((ClassObjectGUI)this).getListOfAttributes()){
                txtAttribute.setX(getClassBorder().getX() + 10);
                currentHeightFromBordersY += 20;
                txtAttribute.setY(currentHeightFromBordersY);
            }
        }

        //line 2
        currentHeightFromBordersY += 20;
        this.getLine2().setStartX(getClassBorder().getX());
        this.getLine2().setEndX(getClassBorder().getX() + newWidth);
        this.getLine2().setStartY(currentHeightFromBordersY);
        this.getLine2().setEndY(currentHeightFromBordersY);

        //operations
        for (Text txtOperation : this.getListOfOperations()){
            txtOperation.setX(getClassBorder().getX() + 10);
            currentHeightFromBordersY += 20;
            txtOperation.setY(currentHeightFromBordersY);
        }

    }

    /**
     * @param relation relation representing objects in the relation and relations data
     * */
    public void addRelation(RelationGUI relation){
        this.listOfRelations.add(relation);
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
            //operation.setY(this.getLine2().getStartY() + 15);
            //operation.setX(this.getClassNameLabel().getX());
        }
        else {
            //Text lastOp = listOfOperations.get(listOfOperations.size() -1 );
            //operation.setY(lastOp.getY() + 15);
            //operation.setX(lastOp.getX());
        }

        //add operation to list of operations
        listOfOperations.add(operation);

        //reset the class height of border and box
        //getClassBox().setHeight(getClassBox().getHeight() + 15);
        //getClassBorder().setHeight(getClassBorder().getHeight() + 15);

        //resize classbox iff necessary
        resizeObjectGUI();

        //map operation
        this.mapOfOperations.put(umlOperation, operation);

        //notify all relation that there is new operation which is possible to be overridden
        for (RelationGUI relationGUI : this.getListOfRelations()) {
            relationGUI.generalizationStateHandling();
        }

        return operation;
    }

}
