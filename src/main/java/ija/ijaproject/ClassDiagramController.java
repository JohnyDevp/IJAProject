package ija.ijaproject;


import ija.ijaproject.cls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class ClassDiagramController {


    @FXML
    public Button btnAddClass;
    @FXML
    public Pane canvas;
    @FXML
    public Button btnAddRelation;

    //for detecting difference when moving any object
    public Double mousePrevX;
    public Double mousePrevY;

    //currently selected class => graphically changed
    GUIClassInterfaceTemplate selectedClass = null;

    //defined colors used for class object
    private final Color selectedClassColor = Color.rgb(227, 68, 36);
    private final Color deselectedClassColor = Color.rgb(80, 95, 230);

    //for knowledge whether clicking on class border means creating new relation
    private boolean createRelation = false;
    //for temporary storing currently creating relation
    private RelationGUI relation;
    private RelationGUI relationForChange;
    public enum relType{ASSOCIATION, AGGREGATION, COMPOSITION, GENERALIZATION}

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
    private List<SequenceDiagram> listOfSequenceDiagrams = new ArrayList<>();

    /**
     * variable storing class diagram
     * */
    private ClassDiagram classDiagram = null;

    /**
     * variable for storing list of graphical representations of classes
     * */
    private List<GUIClassInterfaceTemplate> GUIObjectsList = null;

    /*====================================================================================================================*/
    /*====================================================================================================================*/

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
     * @param sequenceDiagramController instance of new sequence diagram controller
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
    private String getLoadedFilePath() {return this.path; }

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


    /*=============================================================================================================*/
    /*=============================================================================================================*/

    /**
     * setter of class diagram and caller for parsing file => which will work iff file has to be loaded
     * taking control and starting to work when tab is created
     */
    public void start(){
        //todo => get name of class diagram if newly created
        //FIXME warning will there be this name ??
        this.classDiagram = new ClassDiagram("test");

        //if is set file for loading then load it from its path
        if (!getLoadedFilePath().equals("")){
            parseFile();
        }
    }

    /**
     * overridden method
     * parsing file and loading it into tabPane if file has been set up
     * */
    protected void parseFile(){
        JsonReader jr = new JsonReader();
        String tmp_file_path = getLoadedFilePath();

        if(jr.parseJsonClassDiagram(tmp_file_path)) {
            this.classDiagram = jr.getClsDiagram();
            GUIObjectsList = new ArrayList<>();
            //add all created objects to canvas and list of them
            for(UMLClassInterfaceTemplate umlObject : this.classDiagram.getUmlObjectsList()){

                //according to class create the right GUI
                //CLASS
                if (umlObject.getClass() == UMLClass.class){
                    ClassObjectGUI clsObjGUI = new ClassObjectGUI((UMLClass) umlObject);
                    addClassOrInterfaceOnCanvasAndSetActions(clsObjGUI);
                    GUIObjectsList.add(clsObjGUI);

                } else if (umlObject.getClass() == UMLInterface.class){
                    //INTERFACE
                    InterfaceObjectGUI infObjGUI = new InterfaceObjectGUI((UMLInterface) umlObject);
                    addClassOrInterfaceOnCanvasAndSetActions(infObjGUI);
                    GUIObjectsList.add(infObjGUI);
                }
            }

            //add relations to canvas and to classes/interfaces which it is connected to
            for(UMLRelation umlRelation : this.classDiagram.getUmlRelationList()){
                //create graphical representation
                RelationGUI relationGUI = new RelationGUI(umlRelation, this.canvas);
                addRelationOnCanvasAndSetActions(relationGUI);
                //loop through graphical representation of classes and interfaces and when there is match between UMLClass of relation
                // and graphical representation then add this relation to
                for (GUIClassInterfaceTemplate guiObject : GUIObjectsList) {
                    if (guiObject.getObject() == umlRelation.getRelationFromObject() ||
                        guiObject.getObject() == umlRelation.getRelationToObject()) {

                        //add graphical representation of relation to the GUIObject
                        guiObject.addRelation(relationGUI);
                    }
                }
            }
        } else {
            return;
        }

        //todo
        //if (clsDlg == null)

        jr.parseJsonSequenceDiagrams(
                "C:\\Users\\jhola\\IdeaProjects\\IJAProject\\src\\main\\resources\\fake.json"
        );
    }

    /**
     * method for setting currently selected class
     * @param selectedClass class to be selected
     * */
    private void setSelectedClass(GUIClassInterfaceTemplate selectedClass) {

        //change color of previously selected class
        if (this.selectedClass != null){
            this.selectedClass.getClickableCorner().setFill(deselectedClassColor);
        }

        //set new selected class
        this.selectedClass = selectedClass;
        //change the color to selected for the new selected class
        selectedClass.getClickableCorner().setFill(selectedClassColor);

    }

    /**
     * method for setting currently selected class
     * @param deselectedClass class to be deselected
     * */
    private void deselectClass(GUIClassInterfaceTemplate deselectedClass){
        //set the color of the class to default
        deselectedClass.getClickableCorner().setFill(deselectedClassColor);
        this.selectedClass = null;
    }

    /**
     * button action handling creating new tab and class for sequence diagram
     * */
    @FXML
    public void createNewSequenceDiagramBtn() throws Exception{
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

    /**
     * method handling action when button close pressed
     * ensuring properly closed file and closing tab (from another function)
     * */
    @FXML
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
     * method handling clicking on button for adding new class
     * */
    @FXML
    public void btnAddClass() throws IOException {
        //show creating dialog
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/addObjectDialog_view.fxml"));
        Parent parent = fxmlLoader.load();
        AddObjectDialogController dlgController = fxmlLoader.<AddObjectDialogController>getController();
        dlgController.setMainClassDiagram(this.classDiagram);


        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();

        if (dlgController.getCreatedObject() == null) return;

        System.out.println("Creating a class object...");

        //creating a reference on class object
        GUIClassInterfaceTemplate guiObject = null;
        if (dlgController.getCreatedObject().getClass() == UMLClass.class){
            guiObject = new ClassObjectGUI((UMLClass) dlgController.getCreatedObject());
            guiObject.createClassInterfaceObjectGUI();
        } else if (dlgController.getCreatedObject().getClass() == UMLInterface.class){
            guiObject = new InterfaceObjectGUI((UMLInterface) dlgController.getCreatedObject());
            guiObject.createClassInterfaceObjectGUI();
        }

        //add the object on canvas
        if (guiObject != null) addClassOrInterfaceOnCanvasAndSetActions(guiObject);

    }

    /**
     * method handling clicking on button for creating relation
     * */
    @FXML
    public void btnAddRelation(ActionEvent e){
        //show dialog for getting the relation type
        UMLRelation.RelationType rt = null;
        String options[] = {"Association", "Aggregation","Composition", "Generalization"};
        ChoiceDialog dlg = new ChoiceDialog(options[0], options);
        dlg.setHeaderText("Choose type of relation");
        Optional result = dlg.showAndWait();
        if (result.isPresent()){
            switch (dlg.getSelectedItem().toString()){
                case "Association": rt = UMLRelation.RelationType.ASSOCIATION; break;
                case "Aggregation": rt = UMLRelation.RelationType.AGGREGATION; break;
                case "Composition": rt = UMLRelation.RelationType.COMPOSITION; break;
                case "Generalization": rt = UMLRelation.RelationType.GENERALIZATION; break;
            }

        } else {
            //cancel was pressed -> so this cant continue
            return;
        }


        if (!this.createRelation){
            this.createRelation = true;
            //todo combobox dialog for choosing relation type
            this.relation = new RelationGUI(rt, this.canvas);
            btnAddRelation.setText("CANCEL RELATION");
        } else {
            this.createRelation = false;
            this.relation = null;
            btnAddRelation.setText("ADD RELATION");
        }
    }

    @FXML
    public void btnEditObject(ActionEvent e) throws IOException {
        //show creating dialog
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/editObjectDialog_view.fxml"));
        Parent parent = fxmlLoader.load();
        EditObjectDialogController dlgController = fxmlLoader.<EditObjectDialogController>getController();
        dlgController.init(this.selectedClass, this.classDiagram, this.canvas);


        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * method for adding each class object from object list to canvas
     * and adding actions to handler for enabling control of these classes
     * */
    public void addClassOrInterfaceOnCanvasAndSetActions(GUIClassInterfaceTemplate classObject){
        //adding all objects elements to canvas
        canvas.getChildren().addAll(
                classObject.getClassBorder(),
                classObject.getClassBox(),
                classObject.getClassNameLabel(),
                classObject.getLine1(),
                classObject.getLine2(),
                classObject.getClickableCorner()
        );
        //add label interface iff interface
        if (classObject.getClass() == InterfaceObjectGUI.class){
            canvas.getChildren().add(((InterfaceObjectGUI)classObject).getLabelOfInterface());
        }

        //adding attributes iff class
        if (classObject.getClass() == ClassObjectGUI.class){
            for (Text attr : ((ClassObjectGUI)classObject).getListOfAttributes()) {canvas.getChildren().add(attr);}
        }

        //adding operations
        for (Text op : classObject.getListOfOperations()) {canvas.getChildren().add(op);}

        //adding event handlers
        //clicking on class border
        classObject.getClassBorder().setOnMouseClicked(event -> {
            if (!this.createRelation) return; //when relation is not desired to create => return;

            //choose whether is setting the start or end of the relation
            if (!this.relation.getRelationFromSet()){
                //start relation
                this.relation.setRelationFrom(classObject.getObject(), event.getX(), event.getY());
                classObject.addRelation(relation);
                System.out.println(event.getX()+ " " + event.getY());
            } else {
                //end relation
                if (this.relation.getRelClassFrom() == classObject.getObject()) return; //if the click was twice to the same object

                this.relation.setRelationTo(classObject.getObject(), event.getX(), event.getY());

                //add reference for this relation to the end class object
                classObject.addRelation(relation);

                //disable creating relation and add this line and its end to the canvas
                this.createRelation = false;
                this.btnAddRelation.setText("ADD RELATION");

                //todo => place for creating dialog to setup relation params
                addRelationOnCanvasAndSetActions(this.relation);
//                this.canvas.getChildren().addAll(this.relation.getRelLine(), this.relation.getRelLineEnd());
                System.out.println(this.relation.getRelLine().toString());
            }

        });

        //clicking on clickable corner (rather rectangle)
        classObject.getClickableCorner().setOnMouseClicked(mouseEvent -> {
            if (classObject == selectedClass){
                deselectClass(classObject);
            }else{
                setSelectedClass(classObject);
            }
        });

        //clicking on clickable corner => setting that it is ready to move
        classObject.getClickableCorner().setOnMousePressed(mouseEvent -> {

            mousePrevX = mouseEvent.getX();
            mousePrevY = mouseEvent.getY();
        });

        //dragging the clickable corner and with it also the whole object
        classObject.getClickableCorner().setOnMouseDragged(event -> {

            //preventing from overdrawing the pane surroundings => which is mysteriously possible
            //if the rectangle is selected => then do actions
            if((classObject.getClassBorder().getY() + (event.getY() - mousePrevY)) <= 0) return;

            //count the difference between previous and current mouse position for moving objects
            Double diffX = event.getX() - mousePrevX;
            Double diffY = event.getY() - mousePrevY;

            //previous position of mouse set to current position
            mousePrevX = event.getX();
            mousePrevY = event.getY();

            //changing position of each part of diagram
            //position of clickable corner -> actually rectangle at the top of the object
            classObject.getClickableCorner().setX(classObject.getClickableCorner().getX() + diffX);
            classObject.getClickableCorner().setY(classObject.getClickableCorner().getY() + diffY);

            //position of border of object
            classObject.getClassBorder().setX(classObject.getClassBorder().getX() + diffX);
            classObject.getClassBorder().setY(classObject.getClassBorder().getY() + diffY);

            //position of the rectangle representing the inner part of object (without border)
            classObject.getClassBox().setX(classObject.getClassBox().getX() + diffX);
            classObject.getClassBox().setY(classObject.getClassBox().getY() + diffY);

            //position of text => class name
            classObject.getClassNameLabel().setX(classObject.getClassNameLabel().getX() + diffX);
            classObject.getClassNameLabel().setY(classObject.getClassNameLabel().getY() + diffY);

            //position of line dividing space between class name and attributes
            classObject.getLine1().setStartX(classObject.getLine1().getStartX() + diffX);
            classObject.getLine1().setStartY(classObject.getLine1().getStartY() + diffY);
            classObject.getLine1().setEndX(classObject.getLine1().getEndX() + diffX);
            classObject.getLine1().setEndY(classObject.getLine1().getEndY() + diffY);

            //position of line dividing space between class attributes and operation
            classObject.getLine2().setStartX(classObject.getLine2().getStartX() + diffX);
            classObject.getLine2().setStartY(classObject.getLine2().getStartY() + diffY);
            classObject.getLine2().setEndX(classObject.getLine2().getEndX() + diffX);
            classObject.getLine2().setEndY(classObject.getLine2().getEndY() + diffY);

            //position of label <<interface>> iff interface
            if (classObject.getClass() == InterfaceObjectGUI.class){
                ((InterfaceObjectGUI)classObject).getLabelOfInterface().setX(((InterfaceObjectGUI)classObject).getLabelOfInterface().getX() + diffX);
                ((InterfaceObjectGUI)classObject).getLabelOfInterface().setY(((InterfaceObjectGUI)classObject).getLabelOfInterface().getY() + diffY);
            }

            //position of text => each attribute
            //only for class
            if (classObject.getClass() == ClassObjectGUI.class) {

                for (Text attr : ((ClassObjectGUI)classObject).getListOfAttributes()) {
                    attr.setX(attr.getX() + diffX);
                    attr.setY(attr.getY() + diffY);
                }
            }
            //position of text => each operation
            for (Text op : classObject.getListOfOperations()){
                op.setX(op.getX() + diffX);
                op.setY(op.getY() + diffY);
            }

            //position of point where relation begins/ends
            //also redrawing relation line end (arrow, etc.)
            for (RelationGUI rel : classObject.getListOfRelations()){
                rel.recomputeRelationDesign(classObject.getObject(), diffX, diffY);
            }

        });

    }

    /**
     * method for adding relation's graphical parts on canvas and setting up its actions
     * */
    public void addRelationOnCanvasAndSetActions(RelationGUI relation){
        //add relation on canvas
        this.canvas.getChildren().addAll(relation.getRelLine(), relation.getRelLineEnd());

        //set the event when click on the line
        relation.getRelLine().setOnMouseClicked(mouseEvent -> {
            if (relationForChange != null){
                relationForChange.getRelLine().setStroke(Color.BLACK);
            }
            if (relationForChange == relation){
                relationForChange = null;
                relation.getRelLine().setStroke(Color.BLACK);
            } else {
                relationForChange = relation;
                relation.getRelLine().setStroke(Color.BLUE);
            }
        });
    }


}
