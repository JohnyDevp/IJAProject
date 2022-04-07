package ija.ijaproject;

import ija.ijaproject.cls.UMLAttribute;
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

        setName(umlClass.getName());
        this.setXcoord(umlClass.getXcoord());
        this.setYcoord(umlClass.getYcoord());


        //create graphical representation
        this.createClassObjectGUI();

        //add attributes
        for (UMLAttribute umlAttribute : umlClass.getUmlAttributesList()){
            this.addAttribute(umlAttribute);
        }

        //add operations
        for (UMLOperation umlOperation : umlClass.getUmlOperationList()){
            this.addOperation(umlOperation);
        }
    }

    /**
     * @param umlAttribute intern representation of uml attribute
     *                     from that this method extract attribute name and type
     * method for adding attribute to class diagram graphical representation
     * */
    public Text addAttribute(UMLAttribute umlAttribute){

        Text attribute = new Text(umlAttribute.getName() + " : " + umlAttribute.getType());
        attribute.setId(umlAttribute.getName());

        if (listOfAttributes.isEmpty()){
            attribute.setY(this.getLine1().getStartY() + 15);
            attribute.setX(this.getClassNameLabel().getX());

        } else{
            Text lastAttr = listOfAttributes.get(listOfAttributes.size() -1);
            attribute.setY(lastAttr.getY() + 15);
            attribute.setX(lastAttr.getX());

        }

        //reset the class height of border and box
        getClassBox().setHeight(getClassBox().getHeight() + 15);
        getClassBorder().setHeight(getClassBorder().getHeight() + 15);

        //resizing width of class gui if necessary (according to text width)
        this.resizeClassWidth(attribute.getLayoutBounds().getWidth());

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
