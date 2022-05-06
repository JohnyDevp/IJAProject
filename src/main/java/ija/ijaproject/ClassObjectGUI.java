
package ija.ijaproject;

import ija.ijaproject.cls.UMLAttribute;
import ija.ijaproject.cls.UMLClass;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class for creating new graphic representation of class
 *
 * @author xholan11, xzimol04
 * @version 1.1
 */
public class ClassObjectGUI extends GUIClassInterfaceTemplate {

    /** graphical parts of class object */
    protected List<Text> listOfAttributes = new ArrayList<>();
    protected Map<UMLAttribute, Text> mapOfAttributes = new HashMap<UMLAttribute, Text>();

    /**
     * getters
     *
     * @return a {@link java.util.List} object
     */
    public List<Text> getListOfAttributes() {
        return this.listOfAttributes;
    }

    /**
     * 
     * getUmlClass.
     * 
     *
     * @return a {@link ija.ijaproject.cls.UMLClass} object
     */
    public UMLClass getUmlClass() {
        return (UMLClass) super.object;
    }

    /**
     * 
     * Getter for the field <code>mapOfAttributes</code>.
     * 
     *
     * @return a {@link java.util.Map} object
     */
    public Map<UMLAttribute, Text> getMapOfAttributes() {
        return mapOfAttributes;
    }

    /**
     * constructor for creating the class object
     *
     * @param umlClass name of the class
     */
    public ClassObjectGUI(UMLClass umlClass) {
        super(umlClass);

        // add attributes
        for (UMLAttribute umlAttribute : umlClass.getUmlAttributesList()) {
            this.addAtributeFromConstructor(umlAttribute);
        }

    }

    /**
     * 
     * addAttribute.
     * 
     *
     * @param umlAttribute intern representation of uml attribute
     *                     from that this method extract attribute name and type
     *                     method for adding attribute to class diagram graphical
     *                     representation
     * @return null if the uml attribute either with this name or whole the same
     *         already exists as attribute of this class
     */
    public Text addAttribute(UMLAttribute umlAttribute) {

        // if the attribute with the name is already there then it will fail and return
        // null
        if (!getUmlClass().addAttribute(umlAttribute)) {
            return null;
        }

        return addAtributeFromConstructor(umlAttribute);

    }

    /**
     * function for removing attribute from graphical and all others intern
     * representation
     * remove from canvas HAS TO BE DONE BEFORE !!!!
     *
     * @param umlAttribute a {@link ija.ijaproject.cls.UMLAttribute} object
     */
    public void removeAttribute(UMLAttribute umlAttribute) {
        // remove text representation
        this.listOfAttributes.remove(this.mapOfAttributes.get(umlAttribute));
        // remove map representation
        this.mapOfAttributes.remove(umlAttribute);
        // remove intern representation
        ((UMLClass) this.getUmlObject()).deleteAttribute(umlAttribute.getName());

        // resize class gui iff necessary
        this.resizeObjectGUI();
    }

    /**
     * method without control - has been checked already - called by constructor
     * non-adding umlattribute to umlclass - already there when calling from
     * constructor
     */
    private Text addAtributeFromConstructor(UMLAttribute umlAttribute) {

        Text attribute = new Text(umlAttribute.getModifier() + umlAttribute.getName() + " : " + umlAttribute.getType());
        attribute.setId(umlAttribute.getName());

        // add attributes to map
        this.mapOfAttributes.put(umlAttribute, attribute);

        listOfAttributes.add(attribute);

        // resizing width of class gui if necessary (according to text width)
        this.resizeObjectGUI();

        return attribute;
    }

}
