package ija.ijaproject;

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

public class ClassObjectGUI {
    /**graphical parts of class object*/
    private Rectangle classBox;
    private Rectangle classBorder;
    private Rectangle clickableCorner;
    private Text className;
    private Line line1;
    private List<Text> listOfAttributes = new ArrayList<>();
    private Line line2;
    private List<Text> listOfOperations = new ArrayList<>();
    private List<ClassDiagramController.Relation> listOfRelations = new ArrayList<>();

    /**
     * getters
     * */
    public Rectangle getClassBorder() {return this.classBorder;}
    public Rectangle getClassBox(){ return this.classBox;}
    public Rectangle getClickableCorner() {return this.clickableCorner;}
    public List<Text> getListOfAttributes() {return this.listOfAttributes;}
    public List<Text> getListOfOperations() {return this.listOfOperations;}
    public List<ClassDiagramController.Relation> getListOfRelations() {return this.listOfRelations;}
    public Text getClassName() {return this.className; }
    public Line getLine1() { return this.line1; }
    public Line getLine2() { return this.line2; }

    private final Color selectedClassColor = Color.rgb(227, 68, 36);
    private final Color deselectedClassColor = Color.rgb(80, 95, 230);

    /**
     * constructor for creating the class object
     * creating all graphical objects for necessary for empty class
     * @param name name of the class
     * */
    public ClassObjectGUI(String name){

        //create border of the object
        Rectangle rectangleBorder = new Rectangle(100,130, Color.BLACK);
        this.classBorder = rectangleBorder;
        rectangleBorder.setX(0);
        rectangleBorder.setY(0);
        rectangleBorder.setCursor(Cursor.CROSSHAIR);

        //create overall classbox => of rectangle
        Rectangle rectangle = new Rectangle(90,120,Color.rgb(237, 233, 221, 0.6));
        this.classBox = rectangle;
        rectangle.setX(5);
        rectangle.setY(5);

        //sets the class name
        Text className = new Text(name);
        className.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
        className.setY(rectangle.getY() + 35);
        className.setX(rectangle.getX() + 5);
        this.className = className;

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
     * @param attributeText full text of attribute
     * method for adding attribute to class diagram graphical representation
     * */
    public Text addAttribute(String attributeText){
        Text attribute = new Text(attributeText);

        if (listOfAttributes.isEmpty()){
            attribute.setY(this.getLine1().getStartY() + 15);
            attribute.setX(this.getClassName().getX());

            classBox.setHeight(classBox.getHeight() + 15);
        } else{
            Text lastAttr = listOfAttributes.get(listOfAttributes.size() -1);
            attribute.setY(lastAttr.getY() + 15);
            attribute.setX(lastAttr.getX());

            classBox.setHeight(classBox.getHeight() + 15);
        }

        //necessary to move all operations under this attributes => operations are under attributes
        //and also move the line dividing space for attributes and operations
        this.getLine2().setStartY(this.getLine2().getStartY() + 15);
        this.getLine2().setEndY(this.getLine2().getEndY() + 15);
        for(Text attr : getListOfOperations()){
            attr.setY(attr.getY() + 15);
        }

        listOfAttributes.add(attribute);
        return attribute;

    }

    /**
     * @param operationText full text of operation
     * method for adding opperation to class diagram graphical representation
     * */
    public Text addOperation(String operationText){
        Text operation = new Text(operationText);
        if (listOfOperations.isEmpty()){
            operation.setY(this.getLine2().getStartY() + 15);
            operation.setX(this.getClassName().getX());
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
    public void addRelation(ClassDiagramController.Relation relation){
        this.listOfRelations.add(relation);
    }

    /**
     * warning this will be in the ClassDiagramController => couldn't be here -> it needs references
     * method for setting up every possible action and its properties
     * */
    /*private void actionsSetup(){

        //clicking on class border
        this.getClassBorder().setOnMouseClicked(event -> {
            if (!createRelation) return; //when relation is not desired to create => return;

            //choose whether is setting the start or end of the relation
            if (!relation.getRelationFromSet()){
                //start relation
                relation.setRelationFrom(this, event.getX(), event.getY());
                this.addRelation(relation);
                System.out.println(event.getX()+ " " + event.getY());
            } else {
                //end relation
                if (relation.getRelClassFrom() == this) return; //if the click was twice to the same object

                relation.setRelationTo(this, event.getX(), event.getY());

                //add reference for this relation to the end class object
                this.addRelation(relation);

                //disable creating relation and add this line and its end to the canvas
                createRelation = false;
                btnAddRelation.setText("ADD RELATION");
                canvas.getChildren().addAll(relation.getRelLine(), relation.getRelLineEnd());
                System.out.println(relation.getRelLine().toString());
            }

        });

        //clicking on clickable corner (rather rectangle)
        clickableCorner.setOnMouseClicked(mouseEvent -> {
            if (this == selectedClass){
                deselectClass(this);
            }else{
                setSelectedClass(this);
            }
        });

        //clicking on clickable corner => setting that it is ready to move
        clickableCorner.setOnMousePressed(mouseEvent -> {

            mousePrevX = mouseEvent.getX();
            mousePrevY = mouseEvent.getY();
        });

        //dragging the clickable corner and with it also the whole object
        clickableCorner.setOnMouseDragged(event -> {

            //preventing from overdrawing the pane surroundings => which is mysteriously possible
            //if the rectangle is selected => then do actions
            if((this.getClassBorder().getY() + (event.getY() - mousePrevY)) <= 0) return;

            //count the difference between previous and current mouse position for moving objects
            Double diffX = event.getX() - mousePrevX;
            Double diffY = event.getY() - mousePrevY;

            //previous position of mouse set to current position
            mousePrevX = event.getX();
            mousePrevY = event.getY();

            //changing position of each part of diagram
            //position of clickable corner -> actually rectangle at the top of the object
            clickableCorner.setX(clickableCorner.getX() + diffX);
            clickableCorner.setY(clickableCorner.getY() + diffY);

            //position of border of object
            this.getClassBorder().setX(this.getClassBorder().getX() + diffX);
            this.getClassBorder().setY(this.getClassBorder().getY() + diffY);

            //position of the rectangle representing the inner part of object (without border)
            this.getClassBox().setX(this.getClassBox().getX() + diffX);
            this.getClassBox().setY(this.getClassBox().getY() + diffY);

            //position of text => class name
            this.getClassName().setX(this.getClassName().getX() + diffX);
            this.getClassName().setY(this.getClassName().getY() + diffY);

            //position of line dividing space between class name and attributes
            this.getLine1().setStartX(this.getLine1().getStartX() + diffX);
            this.getLine1().setStartY(this.getLine1().getStartY() + diffY);
            this.getLine1().setEndX(this.getLine1().getEndX() + diffX);
            this.getLine1().setEndY(this.getLine1().getEndY() + diffY);

            //position of line dividing space between class attributes and operation
            this.getLine2().setStartX(this.getLine2().getStartX() + diffX);
            this.getLine2().setStartY(this.getLine2().getStartY() + diffY);
            this.getLine2().setEndX(this.getLine2().getEndX() + diffX);
            this.getLine2().setEndY(this.getLine2().getEndY() + diffY);

            //position of text => each attribute
            for (Text attr : this.getListOfAttributes()){
                attr.setX(attr.getX() + diffX);
                attr.setY(attr.getY() + diffY);
            }

            //position of text => each operation
            for (Text op : this.getListOfOperations()){
                op.setX(op.getX() + diffX);
                op.setY(op.getY() + diffY);
            }

            //position of point where relation begins/ends
            //also redrawing relation line end (arrow, etc.)
            for (ClassDiagramController.Relation rel : this.getListOfRelations()){
                rel.recomputeRelationDesign(this, diffX, diffY);
            }

        });
    }*/

}
