package ija.ijaproject;

import ija.ijaproject.cls.UMLInterface;

public class InterfaceObjectGUI extends GUIClassInterfaceTemplate{
    private UMLInterface umlInterface;

    /**
     * constructor for creating the class object
     * @param umlInterface instance of UMLclass/UMLinterface
     */
    public InterfaceObjectGUI(UMLInterface umlInterface) {
        super(umlInterface);
        this.umlInterface = umlInterface;
    }
}
