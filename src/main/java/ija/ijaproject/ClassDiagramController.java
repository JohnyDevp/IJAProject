package ija.ijaproject;


import ija.ijaproject.cls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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
    private List<ClassObjectGUI> classObjectList = null;

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
        this.classDiagram = new ClassDiagram("test");
        parseFile();
    }

    /**
     * overridden method
     * parsing file and loading it into tabPane if file has been set up
     * */
    protected void parseFile(){
        JsonReader jr = new JsonReader();
        String tmp_file_path = "C:\\Users\\jhola\\IdeaProjects\\IJAProject\\src\\main\\resources\\fake.json";
        if(jr.parseJsonClassDiagram(tmp_file_path)) {
            this.classDiagram = jr.getClsDiagram();
            //add all created objects to canvas and list of them
            for(UMLClassInterfaceTemplate umlObject : this.classDiagram.getUmlObjectsList()){

                //according to class create the right GUI
                //CLASS
                if (umlObject.getClass() == UMLClass.class){
                    ClassObjectGUI clsObjGUI = new ClassObjectGUI((UMLClass) umlObject);
                    addClassOrInterfaceOnCanvasAndSetActions(clsObjGUI);

                } else if (umlObject.getClass() == UMLInterface.class){
                    //INTERFACE
                    InterfaceObjectGUI infObjGUI = new InterfaceObjectGUI((UMLInterface) umlObject);
                    addClassOrInterfaceOnCanvasAndSetActions(infObjGUI);
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
    public void btnAddClass(){
        System.out.println("Creating a class object...");

        //get class name from dialog
        TextInputDialog textInputDialog = new TextInputDialog("Enter class name");
        textInputDialog.setHeaderText("Set up class name");
        Optional<String> result = textInputDialog.showAndWait();
        if (!result.isPresent()){
            System.out.println("Cancel pressed - no class will be created");
            return;
        }

        //if the field is empty
        if (textInputDialog.getEditor().getText().isEmpty()) {
            System.out.println("FAIL");
            return;
        }

        //creating a reference on class object
        //TODO check if the class name doesnt exist

        UMLClass umlClass = this.classDiagram.createClass(textInputDialog.getEditor().getText());
        if (umlClass == null) {
            System.out.println("ERROR: Class with this name already exists");
            return;
        }
        ClassObjectGUI classObject = new ClassObjectGUI(umlClass);
        classObject.createClassObjectGUI();
        addClassOrInterfaceOnCanvasAndSetActions(classObject);
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
                this.relation.setRelationFrom(classObject, event.getX(), event.getY());
                classObject.addRelation(relation);
                System.out.println(event.getX()+ " " + event.getY());
            } else {
                //end relation
                if (this.relation.getRelClassFrom() == classObject) return; //if the click was twice to the same object

                this.relation.setRelationTo(classObject, event.getX(), event.getY());

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
                rel.recomputeRelationDesign(classObject, diffX, diffY);
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

    /**
     * class for storing the relation between two classes
     * */
   /* public class Relation{
        private boolean relationFromSet = false;
        private final relType relationType;

        private ClassObjectGUI relClassFrom;
        private ClassObjectGUI relClassTo;

        private final Line relLine;

        private Polygon relLineEnd;
        //these to lines are here for the option of association relation => which is created of one simple arrow
        private Line line1 = null;
        private Line line2 = null;

        //labels on the relation line
        private Text cardinalityByToClass;
        private Text cardinalityByFromClass;
        private Text nameOfRelation;

        *//**
         * constructor
         * @param type type of the relation
         * creating the line and its event for handling selecting this line
         * setting up the relation type
         * *//*
        public Relation(relType type){
            //set up the line and the event when click on the relation
            this.relLine = new Line();
            this.relLine.setStrokeWidth(2.5);
            this.relLine.toBack();
            this.relLine.setCursor(Cursor.HAND);
            //set the event when click on the line
            this.relLine.setOnMouseClicked(mouseEvent -> {
                if (relationForChange != null){
                    relationForChange.relLine.setStroke(Color.BLACK);
                }
                if (relationForChange == this){
                    relationForChange = null;
                    this.relLine.setStroke(Color.BLACK);
                } else {
                    relationForChange = this;
                    this.relLine.setStroke(Color.BLUE);
                }
            });

            //set the type of this relation
            this.relationType = type;
            //create the line from sufficient places
        }


        *//**
         * set all information about relation beginning
         * @param relClassFrom class that is at beginning of this relation
         * @param X X coordinate of the point where the relation starts
         * @param Y Y coordinate of the point where the relation starts
         * *//*
        public void setRelationFrom(ClassObjectGUI relClassFrom, double X, double Y){
            this.relLine.setStartX(X);
            this.relLine.setStartY(Y);
            this.relationFromSet = true;
            this.relClassFrom = relClassFrom;
        }

        *//**
         * set all information about relation ending
         * also creating sufficient line ending
         * @param relClassTo class that is at end of this relation
         * @param X X coordinate of the point where the relation ends
         * @param Y Y coordinate of the point where the relation ends
         * *//*
        public void setRelationTo(ClassObjectGUI relClassTo, double X, double Y){
            this.relLine.setEndX(X);
            this.relLine.setEndY(Y);
            this.relClassTo = relClassTo;

            //set up the polygon representing the relation line ending
            this.relLineEnd = new Polygon();

            //creates relation line ending
            setNewRelLineEndPosition();

            //fixme => this will be set up after dialog shown
            setNameOfRelation("neco");
            setCardinalityByFromClass("0..1");
            setCardinalityByToClass("0..*");
        }


        public void recomputeRelationDesign(ClassObjectGUI classObject, double diffX, double diffY){
            if(getRelClassFrom() == classObject){
                getRelLine().setStartX(getRelLine().getStartX() + diffX);
                getRelLine().setStartY(getRelLine().getStartY() + diffY);
            } else {
                getRelLine().setEndX(getRelLine().getEndX() + diffX);
                getRelLine().setEndY(getRelLine().getEndY() + diffY);
            }

            //things that are moved the same way all time when moving relation
            canvas.getChildren().remove(getRelLineEnd());
            setNewRelLineEndPosition();
            canvas.getChildren().add(getRelLineEnd());

            canvas.getChildren().remove(getNameOfRelation());
            setNameOfRelation("neco");
            canvas.getChildren().remove(getCardinalityByFromClass());
            setCardinalityByFromClass("0..1");
            canvas.getChildren().remove(getCardinalityByToClass());
            setCardinalityByToClass("0..*");
        }
        *//**
         * set the ending polygon which sets the type of the relation
         * or arrow which is compound of two lines
         * *//*
        public void setNewRelLineEndPosition(){
            this.relLineEnd = new Polygon();

            double slope = (this.relLine.getStartY() - this.relLine.getEndY()) / (this.relLine.getStartX() - this.relLine.getEndX());
            double lineAngle = Math.atan(slope);
            double lineLength = Math.sqrt(Math.pow((this.relLine.getStartY() - this.relLine.getEndY()), 2) + Math.pow((this.relLine.getStartX() - this.relLine.getEndX()),2));
            double arrowAngle, arrowLength, arrowWide;

            switch (this.relationType){
                //filled arrow ("big")
                case GENERALIZATION: {
                    System.out.println("generalization");
                    arrowAngle = this.relLine.getStartX() > this.relLine.getEndX() ? Math.toRadians(45) : -Math.toRadians(225);
                    arrowLength = 20;
                    this.relLineEnd.getPoints().addAll(
                            //the aim
                            this.relLine.getEndX(), this.relLine.getEndY(),
                            //left corner
                            arrowLength * Math.cos(lineAngle - arrowAngle) + this.relLine.getEndX(), arrowLength * Math.sin(lineAngle - arrowAngle) + this.relLine.getEndY(),
                            //right corner
                            arrowLength * Math.cos(lineAngle + arrowAngle) + this.relLine.getEndX(), arrowLength * Math.sin(lineAngle + arrowAngle) + this.relLine.getEndY()
                    );
                    this.relLineEnd.setStroke(Color.BLACK);
                    this.relLineEnd.setFill(Color.WHITE);
                }
                break;

                //white filled 4-point-polygon
                case AGGREGATION: {
                    System.out.println("aggregation");
                    arrowAngle = this.relLine.getStartX() > this.relLine.getEndX() ? Math.toRadians(45) : -Math.toRadians(225);
                    arrowLength = 15;
                    arrowWide = 8;
                    double u1 = this.relLine.getEndX() - this.relLine.getStartX();
                    double u2 = this.relLine.getEndY() - this.relLine.getStartY();
                    double Ax = this.relLine.getEndX();
                    double Ay = this.relLine.getEndY();
                    double resultX = Ax - u1*(30/lineLength);
                    double resultY = Ay - u2*(30/lineLength);

                    this.relLineEnd.getPoints().addAll(
                            //the aim
                            this.relLine.getEndX(),this.relLine.getEndY(),
                            //left corner
                            (arrowLength)*Math.cos(lineAngle-arrowAngle) +this.relLine.getEndX(), arrowWide*Math.sin(lineAngle-arrowAngle)+this.relLine.getEndY(),
                            //the last point
                            resultX, resultY,
                            //right corner
                            (arrowLength)*Math.cos(lineAngle+arrowAngle) +this.relLine.getEndX(), arrowWide*Math.sin(lineAngle+arrowAngle)+this.relLine.getEndY()
                    );
                    this.relLineEnd.setStroke(Color.BLACK);
                    this.relLineEnd.setFill(Color.WHITE);
                }
                break;

                //black normal arrow
                case ASSOCIATION: {
                    System.out.println("association");
                    arrowAngle = this.relLine.getStartX() > this.relLine.getEndX() ? Math.toRadians(45) : -Math.toRadians(225);
                    arrowLength = 21;
                    arrowWide = 10;
                    Line line1 = new Line(
                            (arrowLength)*Math.cos(lineAngle-arrowAngle) +this.relLine.getEndX(),
                            arrowWide*Math.sin(lineAngle-arrowAngle)+this.relLine.getEndY(),
                            this.relLine.getEndX(),
                            this.relLine.getEndY()
                    );
                    Line line2 = new Line(
                            (arrowLength)*Math.cos(lineAngle+arrowAngle) +this.relLine.getEndX(),
                            arrowWide*Math.sin(lineAngle+arrowAngle)+this.relLine.getEndY(),
                            this.relLine.getEndX(),
                            this.relLine.getEndY()
                    );
                    line1.setStrokeWidth(2.5);
                    line2.setStrokeWidth(2.5);

                    //adding both lines to canvas
                    //and removing old ones, if exists
                    //warning it has to be here
                    if (this.line1 != null) canvas.getChildren().remove(this.line1);
                    if (this.line2 != null) canvas.getChildren().remove(this.line2);
                    canvas.getChildren().add(line1);
                    canvas.getChildren().add(line2);
                    this.line1 = line1;
                    this.line2 = line2;
                }
                break;

                //black filled 4-point-polygon
                case COMPOSITION:{
                    System.out.println("composition");
                    arrowAngle = this.relLine.getStartX() > this.relLine.getEndX() ? Math.toRadians(45) : -Math.toRadians(225);
                    arrowLength = 15;
                    arrowWide = 8;
                    double u1 = this.relLine.getEndX() - this.relLine.getStartX();
                    double u2 = this.relLine.getEndY() - this.relLine.getStartY();
                    double Ax = this.relLine.getEndX();
                    double Ay = this.relLine.getEndY();
                    double resultX = Ax - u1*(30/lineLength);
                    double resultY = Ay - u2*(30/lineLength);

                    this.relLineEnd.getPoints().addAll(
                            //the aim
                            this.relLine.getEndX(),this.relLine.getEndY(),
                            //left corner
                            (arrowLength)*Math.cos(lineAngle-arrowAngle) +this.relLine.getEndX(), arrowWide*Math.sin(lineAngle-arrowAngle)+this.relLine.getEndY(),
                            //the last point
                            resultX, resultY,
                            //right corner
                            (arrowLength)*Math.cos(lineAngle+arrowAngle) +this.relLine.getEndX(), arrowWide*Math.sin(lineAngle+arrowAngle)+this.relLine.getEndY()
                    );
                    this.relLineEnd.setStroke(Color.BLACK);
                    this.relLineEnd.setFill(Color.BLACK);
                }
                break;
            }

        }


        public void setNameOfRelation(String name){
            Text text = new Text();

            double lineLength = Math.sqrt(Math.pow((this.relLine.getStartY() - this.relLine.getEndY()), 2) + Math.pow((this.relLine.getStartX() - this.relLine.getEndX()),2));
            double u1 = this.relLine.getEndX() - this.relLine.getStartX();
            double u2 = this.relLine.getEndY() - this.relLine.getStartY();
            double Ax = this.relLine.getEndX();
            double Ay = this.relLine.getEndY();
            double resultX = Ax - u1*(0.5);
            double resultY = Ay - u2*(0.5);

            text.setX(resultX);
            text.setY(resultY);
            //text.setFill(Color.WHITE);
            text.setText(name);
            text.setStyle("-fx-background-color: red");
            text.setFont(Font.font("verdana", 15));
            System.out.println(text.toString()+ " "+ lineLength + " " + u1 + " " + u2 + " " +Ax + " " + Ay);
            System.out.println(resultX + " " + resultY);
            text.toFront();
            this.nameOfRelation = text;
            canvas.getChildren().add(text);
        }

        public void setCardinalityByToClass(String cardinality){
            Text text = new Text();

            //computed values for choose the right place on the relation line for the text label
            //used parametric representation of line
            double lineLength = Math.sqrt(Math.pow((this.relLine.getStartY() - this.relLine.getEndY()), 2) + Math.pow((this.relLine.getStartX() - this.relLine.getEndX()),2));
            double u1 = this.relLine.getEndX() - this.relLine.getStartX();
            double u2 = this.relLine.getEndY() - this.relLine.getStartY();
            double Ax = this.relLine.getEndX();
            double Ay = this.relLine.getEndY();
            //this place choosing coordinates at the specific point at the line
            double resultX = Ax - u1*(40/lineLength);
            double resultY = Ay - u2*(20/lineLength);

            //set the label
            text.setX(resultX);
            text.setY(resultY);
            //todo check cardinality format
            text.setText(cardinality);
            text.setFont(Font.font("verdana", 15));
            text.toFront();
            this.cardinalityByToClass = text;
            canvas.getChildren().add(text);
        }

        public void setCardinalityByFromClass(String cardinality){
            Text text = new Text();

            //computed values for choose the right place on the relation line for the text label
            //used parametric representation of line
            double lineLength = Math.sqrt(Math.pow((this.relLine.getStartY() - this.relLine.getEndY()), 2) + Math.pow((this.relLine.getStartX() - this.relLine.getEndX()),2));
            double u1 = this.relLine.getEndX() - this.relLine.getStartX();
            double u2 = this.relLine.getEndY() - this.relLine.getStartY();
            double Ax = this.relLine.getEndX();
            double Ay = this.relLine.getEndY();
            //this place choosing coordinates at the specific point at the line
            double resultX = Ax - u1*((lineLength-20)/lineLength);
            double resultY = Ay - u2*((lineLength-20)/lineLength);

            //set the label
            text.setX(resultX);
            text.setY(resultY);
            //todo check cardinality format
            text.setText(cardinality);
            text.setFont(Font.font("verdana", 15));
            text.toFront();
            this.cardinalityByFromClass = text;
            canvas.getChildren().add(text);
        }

        *//**
         * getters
         * *//*
        public Text getNameOfRelation() {return this.nameOfRelation;}
        public Text getCardinalityByToClass() {return this.cardinalityByToClass;}
        public Text getCardinalityByFromClass() {return this.cardinalityByFromClass;}
        public boolean getRelationFromSet() {return this.relationFromSet; }
        public ClassObjectGUI getRelClassFrom() {return this.relClassFrom; }
        public ClassObjectGUI getRelClassTo() {return this.relClassTo; }
        public Line getRelLine() {return this.relLine; }
        public Polygon getRelLineEnd() {return this.relLineEnd; }
    }
*/
}
