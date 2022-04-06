package ija.ijaproject;

import ija.ijaproject.cls.UMLClass;
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
public class ClassObjectGUI extends GUIClassInterfaceTemplate{
    /**
     * logical and intern representation of class
     * */
    private UMLClass umlClass;

    /**
     * starting position on canvas
     * */
    private double Xcoord = 0.0, Ycoord = 0.0;
    private String className;

    /**graphical parts of class object*/
    private List<Text> listOfAttributes = new ArrayList<>();


    /**
     * getters
     * */
    public List<Text> getListOfAttributes() {return this.listOfAttributes;}

    /**
     * constructor for creating the class object
     * @param umlClass name of the class
     * */
    public ClassObjectGUI(UMLClass umlClass){
        super(umlClass);
        this.umlClass = umlClass;

    }

    /**
     * @param attributeText full text of attribute
     * method for adding attribute to class diagram graphical representation
     * */
    public Text addAttribute(String attributeText){
        Text attribute = new Text(attributeText);

        if (listOfAttributes.isEmpty()){
            attribute.setY(this.getLine1().getStartY() + 15);
            attribute.setX(this.getClassNameLabel().getX());

            getClassBox().setHeight(getClassBox().getHeight() + 15);
        } else{
            Text lastAttr = listOfAttributes.get(listOfAttributes.size() -1);
            attribute.setY(lastAttr.getY() + 15);
            attribute.setX(lastAttr.getX());

            getClassBox().setHeight(getClassBox().getHeight() + 15);
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


}
