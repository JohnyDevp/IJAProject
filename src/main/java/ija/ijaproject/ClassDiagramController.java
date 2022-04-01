package ija.ijaproject;


import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassDiagramController {

    /**
     * variable storing reference for main controller
     * */
    private MainController mainController;

    /**
     * variable storing path of file for load
     * */
    private String path;

    /**
     * variable for tabPane reference from main controller
     * */
    private TabPane tabPane;

    /**
     * variable storing list of all sequence diagrams
     * */
    private List<SequenceDiagramController> sequenceDiagramControllersList = new ArrayList<SequenceDiagramController>();

    /**
     * setter
     * @param tabPane reference of the tab where the diagram has been drawn
     * */
    public final void setTabPane(TabPane tabPane){
        this.tabPane = tabPane;
    }

    /**
     * getter
     * @return reference of the tabPane
     * */
    public final TabPane getTabPane() {return this.tabPane; }

    /**
     * add new sequenceDiagramController to the list of them
     * @param sequenceDiagramController
     * */
    private void addNewSequenceDiagramControllerToList(SequenceDiagramController sequenceDiagramController){
        this.sequenceDiagramControllersList.add(sequenceDiagramController);
    }

    /**
     * getter
     * @return all the list of sequence diagram controllers
     * */
    private List<SequenceDiagramController> getSequenceDiagramControllersList(){
        return Collections.unmodifiableList(this.sequenceDiagramControllersList);
    }

    /**
     * setter
     * @param path path of loaded file, when not loaded it will be empty*/
    public void setLoadedFilePath(String path){
        this.path = path;
    }

    /**
     * getter
     * @return path of loaded file, when no file loaded returns empty string
     * */
    public final String getLoadedFilePath() {return this.path; }

    /**
     * setter
     * @param mainController reference of main controller of whole window
     * */
    public final void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    /**
     * getter
     * @return instance of mainController
     * */
    private MainController getMainController() { return this.mainController;}

    /**
     * override method handling action when button close pressed
     * ensuring properly closed file and closing tab (from another function)
     * */
    public void btnClose(){
        //TODO prompt for exiting

        //closing all sequence diagram tabs
        for (SequenceDiagramController sequenceDiagramController : getSequenceDiagramControllersList()){
            sequenceDiagramController.btnClose();
        }


        //TODO properly close classDiagram file
        //remove first tab => close classDiagram
        getTabPane().getTabs().remove(0);
        getMainController().btnCreateNewClassDiagram.setDisable(false);
        getMainController().btnLoadClassDiagram.setDisable(false);
    }

    /**
     * overridden method
     * taking control and starting to work when tab is created
     */
    public void start(){

    }

    /**
     * button action handling creating new tab and class for sequence diagram
     * */
    public void createNewSequenceDiagramBtn() throws Exception{
        diagramTabConstructor();
    }

    /**
     * overridden method
     * parsing file and loading it into tabPane if file has been set up
     * */
    protected void parseFile(){

    }

    private void diagramTabConstructor() throws Exception{
        System.out.println("Loading sequence diagram...");

        //create new tab
        Tab tab = new Tab("Sequence diagram");

        //load desired view to the anchorPane and then set the anchorPane as tab content
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/sequenceDiagram_view.fxml"));
        AnchorPane anch = loader.load();

        //get controller from either sequence or class diagram
        SequenceDiagramController sequenceDiagramController = loader.getController();
        tab.setContent(anch);

        //add tab to tabPane
        getTabPane().getTabs().add(tab);

        //sets all necessary information for DiagramController
        sequenceDiagramController.setTabPane(this.tabPane);
        sequenceDiagramController.setTab(tab);

        //add reference for this controller to the list
        addNewSequenceDiagramControllerToList(sequenceDiagramController);

        //draw if there is something to draw
        sequenceDiagramController.start();
    }
}
