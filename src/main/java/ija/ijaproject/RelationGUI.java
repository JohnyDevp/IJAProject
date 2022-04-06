package ija.ijaproject;

import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class RelationGUI {
    private boolean relationFromSet = false;
    private final ClassDiagramController.relType relationType;

    private ClassDiagramController.ClassObject relClassFrom;
    private ClassDiagramController.ClassObject relClassTo;

    private Line relLine;

    private Polygon relLineEnd;
    //these to lines are here for the option of association relation => which is created of one simple arrow
    private Line line1 = null;
    private Line line2 = null;

    //labels on the relation line
    private Text cardinalityByToClass;
    private Text cardinalityByFromClass;
    private Text nameOfRelation;

    /**
     * getters
     * */
    public Text getNameOfRelation() {return this.nameOfRelation;}
    public Text getCardinalityByToClass() {return this.cardinalityByToClass;}
    public Text getCardinalityByFromClass() {return this.cardinalityByFromClass;}
    public boolean getRelationFromSet() {return this.relationFromSet; }
    public ClassDiagramController.ClassObject getRelClassFrom() {return this.relClassFrom; }
    public ClassDiagramController.ClassObject getRelClassTo() {return this.relClassTo; }
    public Line getRelLine() {return this.relLine; }
    public Polygon getRelLineEnd() {return this.relLineEnd; }

    /**
     * constructor
     * @param type type of the relation
     * creating the line and its event for handling selecting this line
     * setting up the relation type
     * */
    public Relation(ClassDiagramController.relType type){
        //set up the line and the event when click on the relation
        this.relLine = new Line();
        this.relLine.setStrokeWidth(2.5);
        this.relLine.toBack();
        this.relLine.setCursor(Cursor.HAND);
        //set the event when click on the line
        this.relLine.setOnMouseClicked(mouseEvent -> {
            if (relationForChange != null){
                relationForChange.relLine.setStroke(Color.BLACK);
            }
            if (relationForChange == this){
                relationForChange = null;
                this.relLine.setStroke(Color.BLACK);
            } else {
                relationForChange = this;
                this.relLine.setStroke(Color.BLUE);
            }
        });

        //set the type of this relation
        this.relationType = type;
        //create the line from sufficient places
    }


    /**
     * set all information about relation beginning
     * @param relClassFrom class that is at beginning of this relation
     * @param X X coordinate of the point where the relation starts
     * @param Y Y coordinate of the point where the relation starts
     * */
    public void setRelationFrom(ClassDiagramController.ClassObject relClassFrom, double X, double Y){
        this.relLine.setStartX(X);
        this.relLine.setStartY(Y);
        this.relationFromSet = true;
        this.relClassFrom = relClassFrom;
    }

    /**
     * set all information about relation ending
     * also creating sufficient line ending
     * @param relClassTo class that is at end of this relation
     * @param X X coordinate of the point where the relation ends
     * @param Y Y coordinate of the point where the relation ends
     * */
    public void setRelationTo(ClassDiagramController.ClassObject relClassTo, double X, double Y){
        this.relLine.setEndX(X);
        this.relLine.setEndY(Y);
        this.relClassTo = relClassTo;

        //set up the polygon representing the relation line ending
        this.relLineEnd = new Polygon();

        //creates relation line ending
        setNewRelLineEndPosition();

        //fixme => this will be set up after dialog shown
        setNameOfRelation("neco");
        setCardinalityByFromClass("0..1");
        setCardinalityByToClass("0..*");
    }


    public void recomputeRelationDesign(ClassDiagramController.ClassObject classObject, double diffX, double diffY){
        if(getRelClassFrom() == classObject){
            getRelLine().setStartX(getRelLine().getStartX() + diffX);
            getRelLine().setStartY(getRelLine().getStartY() + diffY);
        } else {
            getRelLine().setEndX(getRelLine().getEndX() + diffX);
            getRelLine().setEndY(getRelLine().getEndY() + diffY);
        }

        //things that are moved the same way all time when moving relation
        canvas.getChildren().remove(getRelLineEnd());
        setNewRelLineEndPosition();
        canvas.getChildren().add(getRelLineEnd());

        canvas.getChildren().remove(getNameOfRelation());
        setNameOfRelation("neco");
        canvas.getChildren().remove(getCardinalityByFromClass());
        setCardinalityByFromClass("0..1");
        canvas.getChildren().remove(getCardinalityByToClass());
        setCardinalityByToClass("0..*");
    }
    /**
     * set the ending polygon which sets the type of the relation
     * or arrow which is compound of two lines
     * */
    public void setNewRelLineEndPosition(){
        this.relLineEnd = new Polygon();

        double slope = (this.relLine.getStartY() - this.relLine.getEndY()) / (this.relLine.getStartX() - this.relLine.getEndX());
        double lineAngle = Math.atan(slope);
        double lineLength = Math.sqrt(Math.pow((this.relLine.getStartY() - this.relLine.getEndY()), 2) + Math.pow((this.relLine.getStartX() - this.relLine.getEndX()),2));
        double arrowAngle, arrowLength, arrowWide;

        switch (this.relationType){
            //filled arrow ("big")
            case GENERALIZATION: {
                System.out.println("generalization");
                arrowAngle = this.relLine.getStartX() > this.relLine.getEndX() ? Math.toRadians(45) : -Math.toRadians(225);
                arrowLength = 20;
                this.relLineEnd.getPoints().addAll(
                        //the aim
                        this.relLine.getEndX(), this.relLine.getEndY(),
                        //left corner
                        arrowLength * Math.cos(lineAngle - arrowAngle) + this.relLine.getEndX(), arrowLength * Math.sin(lineAngle - arrowAngle) + this.relLine.getEndY(),
                        //right corner
                        arrowLength * Math.cos(lineAngle + arrowAngle) + this.relLine.getEndX(), arrowLength * Math.sin(lineAngle + arrowAngle) + this.relLine.getEndY()
                );
                this.relLineEnd.setStroke(Color.BLACK);
                this.relLineEnd.setFill(Color.WHITE);
            }
            break;

            //white filled 4-point-polygon
            case AGGREGATION: {
                System.out.println("aggregation");
                arrowAngle = this.relLine.getStartX() > this.relLine.getEndX() ? Math.toRadians(45) : -Math.toRadians(225);
                arrowLength = 15;
                arrowWide = 8;
                double u1 = this.relLine.getEndX() - this.relLine.getStartX();
                double u2 = this.relLine.getEndY() - this.relLine.getStartY();
                double Ax = this.relLine.getEndX();
                double Ay = this.relLine.getEndY();
                double resultX = Ax - u1*(30/lineLength);
                double resultY = Ay - u2*(30/lineLength);

                this.relLineEnd.getPoints().addAll(
                        //the aim
                        this.relLine.getEndX(),this.relLine.getEndY(),
                        //left corner
                        (arrowLength)*Math.cos(lineAngle-arrowAngle) +this.relLine.getEndX(), arrowWide*Math.sin(lineAngle-arrowAngle)+this.relLine.getEndY(),
                        //the last point
                        resultX, resultY,
                        //right corner
                        (arrowLength)*Math.cos(lineAngle+arrowAngle) +this.relLine.getEndX(), arrowWide*Math.sin(lineAngle+arrowAngle)+this.relLine.getEndY()
                );
                this.relLineEnd.setStroke(Color.BLACK);
                this.relLineEnd.setFill(Color.WHITE);
            }
            break;

            //black normal arrow
            case ASSOCIATION: {
                System.out.println("association");
                arrowAngle = this.relLine.getStartX() > this.relLine.getEndX() ? Math.toRadians(45) : -Math.toRadians(225);
                arrowLength = 21;
                arrowWide = 10;
                Line line1 = new Line(
                        (arrowLength)*Math.cos(lineAngle-arrowAngle) +this.relLine.getEndX(),
                        arrowWide*Math.sin(lineAngle-arrowAngle)+this.relLine.getEndY(),
                        this.relLine.getEndX(),
                        this.relLine.getEndY()
                );
                Line line2 = new Line(
                        (arrowLength)*Math.cos(lineAngle+arrowAngle) +this.relLine.getEndX(),
                        arrowWide*Math.sin(lineAngle+arrowAngle)+this.relLine.getEndY(),
                        this.relLine.getEndX(),
                        this.relLine.getEndY()
                );
                line1.setStrokeWidth(2.5);
                line2.setStrokeWidth(2.5);

                //adding both lines to canvas
                //and removing old ones, if exists
                //warning it has to be here
                if (this.line1 != null) canvas.getChildren().remove(this.line1);
                if (this.line2 != null) canvas.getChildren().remove(this.line2);
                canvas.getChildren().add(line1);
                canvas.getChildren().add(line2);
                this.line1 = line1;
                this.line2 = line2;
            }
            break;

            //black filled 4-point-polygon
            case COMPOSITION:{
                System.out.println("composition");
                arrowAngle = this.relLine.getStartX() > this.relLine.getEndX() ? Math.toRadians(45) : -Math.toRadians(225);
                arrowLength = 15;
                arrowWide = 8;
                double u1 = this.relLine.getEndX() - this.relLine.getStartX();
                double u2 = this.relLine.getEndY() - this.relLine.getStartY();
                double Ax = this.relLine.getEndX();
                double Ay = this.relLine.getEndY();
                double resultX = Ax - u1*(30/lineLength);
                double resultY = Ay - u2*(30/lineLength);

                this.relLineEnd.getPoints().addAll(
                        //the aim
                        this.relLine.getEndX(),this.relLine.getEndY(),
                        //left corner
                        (arrowLength)*Math.cos(lineAngle-arrowAngle) +this.relLine.getEndX(), arrowWide*Math.sin(lineAngle-arrowAngle)+this.relLine.getEndY(),
                        //the last point
                        resultX, resultY,
                        //right corner
                        (arrowLength)*Math.cos(lineAngle+arrowAngle) +this.relLine.getEndX(), arrowWide*Math.sin(lineAngle+arrowAngle)+this.relLine.getEndY()
                );
                this.relLineEnd.setStroke(Color.BLACK);
                this.relLineEnd.setFill(Color.BLACK);
            }
            break;
        }

    }


    public void setNameOfRelation(String name){
        Text text = new Text();

        double lineLength = Math.sqrt(Math.pow((this.relLine.getStartY() - this.relLine.getEndY()), 2) + Math.pow((this.relLine.getStartX() - this.relLine.getEndX()),2));
        double u1 = this.relLine.getEndX() - this.relLine.getStartX();
        double u2 = this.relLine.getEndY() - this.relLine.getStartY();
        double Ax = this.relLine.getEndX();
        double Ay = this.relLine.getEndY();
        double resultX = Ax - u1*(0.5);
        double resultY = Ay - u2*(0.5);

        text.setX(resultX);
        text.setY(resultY);
        //text.setFill(Color.WHITE);
        text.setText(name);
        text.setStyle("-fx-background-color: red");
        text.setFont(Font.font("verdana", 15));
        System.out.println(text.toString()+ " "+ lineLength + " " + u1 + " " + u2 + " " +Ax + " " + Ay);
        System.out.println(resultX + " " + resultY);
        text.toFront();
        this.nameOfRelation = text;
        canvas.getChildren().add(text);
    }

    public void setCardinalityByToClass(String cardinality){
        Text text = new Text();

        //computed values for choose the right place on the relation line for the text label
        //used parametric representation of line
        double lineLength = Math.sqrt(Math.pow((this.relLine.getStartY() - this.relLine.getEndY()), 2) + Math.pow((this.relLine.getStartX() - this.relLine.getEndX()),2));
        double u1 = this.relLine.getEndX() - this.relLine.getStartX();
        double u2 = this.relLine.getEndY() - this.relLine.getStartY();
        double Ax = this.relLine.getEndX();
        double Ay = this.relLine.getEndY();
        //this place choosing coordinates at the specific point at the line
        double resultX = Ax - u1*(40/lineLength);
        double resultY = Ay - u2*(20/lineLength);

        //set the label
        text.setX(resultX);
        text.setY(resultY);
        //todo check cardinality format
        text.setText(cardinality);
        text.setFont(Font.font("verdana", 15));
        text.toFront();
        this.cardinalityByToClass = text;
        canvas.getChildren().add(text);
    }

    public void setCardinalityByFromClass(String cardinality){
        Text text = new Text();

        //computed values for choose the right place on the relation line for the text label
        //used parametric representation of line
        double lineLength = Math.sqrt(Math.pow((this.relLine.getStartY() - this.relLine.getEndY()), 2) + Math.pow((this.relLine.getStartX() - this.relLine.getEndX()),2));
        double u1 = this.relLine.getEndX() - this.relLine.getStartX();
        double u2 = this.relLine.getEndY() - this.relLine.getStartY();
        double Ax = this.relLine.getEndX();
        double Ay = this.relLine.getEndY();
        //this place choosing coordinates at the specific point at the line
        double resultX = Ax - u1*((lineLength-20)/lineLength);
        double resultY = Ay - u2*((lineLength-20)/lineLength);

        //set the label
        text.setX(resultX);
        text.setY(resultY);
        //todo check cardinality format
        text.setText(cardinality);
        text.setFont(Font.font("verdana", 15));
        text.toFront();
        this.cardinalityByFromClass = text;
        canvas.getChildren().add(text);
    }
}
