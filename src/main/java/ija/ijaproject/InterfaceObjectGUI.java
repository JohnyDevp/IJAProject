package ija.ijaproject;

import ija.ijaproject.cls.UMLInterface;
import javafx.scene.text.Text;

/**
 * gui representation of interface in class diagram
 *
 * @author xzimol04, xholan11
 * @version 1.1
 */
public class InterfaceObjectGUI extends GUIClassInterfaceTemplate {

    private Text interfaceLabel;

    /**
     * getters
     *
     * @return a {@link ija.ijaproject.cls.UMLInterface} object
     */
    public UMLInterface getUmlInterface() {
        return (UMLInterface) this.object;
    }

    /**
     * 
     * getLabelOfInterface.
     * 
     *
     * @return a {@link javafx.scene.text.Text} object
     */
    public Text getLabelOfInterface() {
        return this.interfaceLabel;
    }

    /**
     * constructor for creating the class object
     *
     * @param umlInterface instance of UMLclass/UMLinterface
     */
    public InterfaceObjectGUI(UMLInterface umlInterface) {
        super(umlInterface);

        // set the label of interface
        this.getClassNameLabel().setY(this.getClassNameLabel().getY() - 2);

        Text label = new Text("<<interface>>");
        label.setX(this.getClassBorder().getX() + this.getClassBorder().getWidth() / 2
                - label.getLayoutBounds().getWidth() / 2);
        label.setY(this.getClassNameLabel().getY() + 13);
        this.interfaceLabel = label;
        resizeObjectGUI();

    }

}
