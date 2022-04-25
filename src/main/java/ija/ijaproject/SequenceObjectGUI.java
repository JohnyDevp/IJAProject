package ija.ijaproject;

import ija.ijaproject.cls.SequenceDiagram;
import ija.ijaproject.cls.UMLClass;
import ija.ijaproject.cls.UMLSeqClass;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SequenceObjectGUI {
    //implicitly set Y for each object
    private static final int YCOORD = 30;

    //pane - canvas reference
    private Pane canvas;

    //intern representation of this object
    private UMLSeqClass umlSeqClass;
    private SequenceDiagram sequenceDiagram;

    //gui representation of this object
    private Text objName;
    private Rectangle objBackground;
    private Line objectTimeLine;
    private List<Rectangle> timeLineActiveRectangleList = new ArrayList<>();

    //list of all gui relations relevant to this object
    private List<SequenceRelationGUI> sequenceRelationGUIList = new ArrayList<>();


    /**getters*/
    public Line getObjectTimeLine() {
        return objectTimeLine;
    }
    /**getter*/
    public Rectangle getObjBackground() {
        return objBackground;
    }
    /**getter*/
    public Text getObjNameText() {
        return objName;
    }
    /**getter*/
    public List<SequenceRelationGUI> getSequenceRelationGUIList() {
        return sequenceRelationGUIList;
    }

    /**constructor*/
    public SequenceObjectGUI(UMLSeqClass umlClass, SequenceDiagram sequenceDiagram, Pane canvas){
        this.umlSeqClass = umlClass;
        this.sequenceDiagram = sequenceDiagram;
        this.canvas = canvas;
    }

    /**method for creating the gui of this object*/
    public void createGUI(){
        //set the graphical representation of text of the object
        Text objName = new Text();
        objName.setText(":"+this.umlSeqClass.getName());

        //compute the width of whole object
        Double objWidth = objName.getLayoutBounds().getWidth() + 10;

        //create objects background
        Rectangle objBackground = new Rectangle();
        objBackground.setWidth(objWidth);
        objBackground.setHeight(60);
        objBackground.setFill(Color.LIGHTCYAN);
        objBackground.setStroke(Color.BLACK);
        //SET X AND Y COORD OF BOX
        objBackground.setX(this.umlSeqClass.getXcoord());
        objBackground.setY(YCOORD);

        //compute half of the background of the object exactly on X axis
        Double halfOnXAxis = objBackground.getWidth() / 2 + objBackground.getX();
        //compute Y position of bottom border of object background
        Double bottomOfBackgroundOnYAxis = objBackground.getY() + objBackground.getHeight();

        //SET TEXT Y,X COORD
        objName.setX(halfOnXAxis - objName.getLayoutBounds().getWidth()/2);
        objName.setY((YCOORD + bottomOfBackgroundOnYAxis) / 2);

        //create object timeline
        Line timeLine = new Line();
        timeLine.setStartX(halfOnXAxis);
        timeLine.setStartY(bottomOfBackgroundOnYAxis);
        timeLine.setEndX(halfOnXAxis);
        timeLine.setEndY(200); //todo - which height of object life-line should be chosen????
        timeLine.getStrokeDashArray().addAll(20d, 10d);

        //set all objects as attributes of this class
        this.objectTimeLine = timeLine;
        this.objBackground = objBackground;
        this.objName = objName;
    }

    public void resizeObject(){

    }
    /**method for adding everything on canvas*/
    public void addObjectOnCanvas(){

    }

    /**method for removing all parts of this object from canvas*/
    public void removeObjectFromCanvas(){

    }

    /**method for activating this object - means creating "active" rectangle on the timeline*/
    public void activateObject(){

    }
}
