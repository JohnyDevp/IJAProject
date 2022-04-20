package ija.ijaproject;

import ija.ijaproject.cls.SequenceDiagram;
import ija.ijaproject.cls.UMLClass;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SequenceObjectGUI {
    //gui representation of this object
    private Text objName;
    private Rectangle objBackground;
    private Line objectTimeLine;
    private List<Rectangle> timeLineActiveRectangleList = new ArrayList<>();

    //list of all gui relations relevant to this object
    private List<SequenceRelationGUI> sequenceRelationGUIList = new ArrayList<>();

    //intern representation of this object
    private UMLClass umlClass;
    private SequenceDiagram sequenceDiagram;

    /**getters*/
    public Line getObjectTimeLine() {
        return objectTimeLine;
    }
    /**getter*/
    public Rectangle getObjBackground() {
        return objBackground;
    }
    /**getter*/
    public Text getObjName() {
        return objName;
    }

    /**constructor*/
    public SequenceObjectGUI(UMLClass umlClass, SequenceDiagram sequenceDiagram){
        this.umlClass = umlClass;
        this.sequenceDiagram = sequenceDiagram;
    }

    /**method for creating the gui of this object*/
    public void createGUI(Pane canvas){
        //set the graphical representation of text of the object
        Text objName = new Text();
        objName.setText(":"+this.umlClass.getName());

        //compute the width of whole object
        Double objWidth = objName.getLayoutBounds().getWidth() + 10;

        //create objects background
        Rectangle objBackground = new Rectangle();
        objBackground.setWidth(objWidth);
        objBackground.setHeight(60);
        objBackground.setFill(Color.LIGHTCYAN);
        objBackground.setStroke(Color.BLACK);

        //compute half of the background of the object exactly on X axis
        Double halfOnXAxis = objBackground.getWidth() / 2 + objBackground.getX();
        //compute Y position of bottom border of object background
        Double bottomOfBackgroundOnYAxis = objBackground.getY() + objBackground.getHeight();

        //create object timeline
        Line timeLine = new Line();
        timeLine.setStartX(halfOnXAxis);
        timeLine.setStartY(bottomOfBackgroundOnYAxis);
        timeLine.setEndX(halfOnXAxis);
        timeLine.setEndY(200); //todo - which height should be chosen????
        timeLine.getStrokeDashArray().addAll(20d, 10d);

        //set all objects as attributes of this class
        this.objectTimeLine = timeLine;
        this.objBackground = objBackground;
        this.objName = objName;

        //add object on gui
        canvas.getChildren().addAll(objName,objBackground,timeLine);
    }

    /**method for adding everything on canvas*/
    public void addObjectOnCanvas(){

    }

    /**method for removing all parts of this object from canvas*/
    public void removeObjectFromCanvas(){

    }

    /**method for activating this object - means creating "active" rectangle on the timeline*/
    public void activateObject(Pane canvas){

    }
}
