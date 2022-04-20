package ija.ijaproject;

import ija.ijaproject.cls.SequenceDiagram;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.ArrayList;
import java.util.List;

/**
 * controller for gui for sequence diagram
 * @author xzimol04
 * */
public class SequenceDiagramController {

    /**variable storing all the objects taking part in this diagram*/
    private List<SequenceObjectGUI> sequenceObjectGUIList = new ArrayList<>();

    /**variable storing all the relations taking part in this diagram*/
    private List<SequenceRelationGUI> sequenceRelationGUIList = new ArrayList<>();

    /**variable storing the intern representation of this sequence diagram*/
    private SequenceDiagram sequenceDiagram = new SequenceDiagram("");

    /**
     * variable storing tab where this diagram is stored
     * */
    private Tab tab;

    /**
     * variable for tabPane reference from main controller
     * */
    private TabPane tabPane;

    /**
     * setter
     * @param tabPane reference of the tab where the diagram has been drawed
     * */
    public final void setTabPane(TabPane tabPane){
        this.tabPane = tabPane;
    }

    /**
     * setter
     * @param tab reference of the tab where the diagram has been drawed
     * */
    public final void setTab(Tab tab){
        this.tab = tab;
    }

    /**
     * getter
     * @return reference of the tab where diagram has been drawed
     * */
    public final Tab getTab() {return this.tab; }

    /**
     * getter
     * @return reference of the tabPane
     * */
    public final TabPane getTabPane() {return this.tabPane; }

    /**
     * override method handling action when button close pressed
     * ensuring properly closed file and closing tab (from another function)
     * */
    public void btnClose(){
        //TODO prompt for exiting if not saved
        //TODO properly close file

        //remove tab of this diagram from tabPane
        getTabPane().getTabs().remove(getTab());

    }

    /**
     * method for starting drawing diagram from existing file
     * it is called from ClassDiagramController
     * */
    public void init(SequenceDiagram sequenceDiagram){
        this.sequenceDiagram = sequenceDiagram;
    }

}
