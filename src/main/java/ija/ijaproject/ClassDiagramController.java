package ija.ijaproject;


import ija.ijaproject.cls.ClassDiagram;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    //defined colors used for class object
    private final Color selectedClassColor = Color.rgb(227, 68, 36);
    private final Color oldSelectedClassColor = Color.rgb(240, 160, 144);
    private final Color deselectedClassColor = Color.rgb(80, 95, 230);

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
     * variable storing class diagram
     * */
    private ClassDiagram classDiagram;

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
    private final String getLoadedFilePath() {return this.path; }

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
     * overridden method
     * taking control and starting to work when tab is created
     */
    public void start(){
//        canvas.setCursor(Cursor.No);
        parseFile();
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
     * overridden method
     * parsing file and loading it into tabPane if file has been set up
     * */
    protected void parseFile(){

    }

    ClassObject oldSelectedClas = null;
    ClassObject selectedClass = null;

    /**
     * method for setting currently selected class
     * @param selectedClass class to be selected
     * */
    private void setSelectedClass(ClassObject selectedClass) {

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
    private void deselectClass(ClassObject deselectedClass){
        //set the color of the class to default
        deselectedClass.getClickableCorner().setFill(deselectedClassColor);
        this.selectedClass = null;
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
    public void btnAddClass(ActionEvent e){
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
        ClassObject classObject = new ClassObject(textInputDialog.getEditor().getText());

        //set var for clickable corner of the class object
        Rectangle clickableCorner = classObject.getClickableCorner();

        //adding all objects elements to canvas
        canvas.getChildren().addAll(classObject.getClassBorder(),classObject.getClassBox(), classObject.getClassName(), classObject.getLine1(), classObject.getLine2(), classObject.getClickableCorner());
        for (Text attr : classObject.getListOfAttributes()) {canvas.getChildren().add(attr);}
        for (Text op : classObject.getListOfOperations()) {canvas.getChildren().add(op);}

        //clicking on class border
        classObject.getClassBorder().setOnMouseClicked(event -> {
            if (!this.createRelation) return; //when relation is not desired to create

            //choose whether is setting the start or end of the relation
            if (!this.relation.getRelationFromSet()){
                //start relation
                this.relation.setRelationFrom(classObject, event.getX(), event.getY());
                classObject.addRelation(this.relation);
                System.out.println(event.getX()+ " " + event.getY());
            } else {
                //end relation
                if (this.relation.getRelClassFrom() == classObject) return; //if the click was twice to the same object

                this.relation.setRelationTo(classObject, event.getX(), event.getY());

                //add reference for this relation to the end class object
                classObject.addRelation(this.relation);

                //disable creating relation and add this line and its end to the canvas
                this.createRelation = false;
                btnAddRelation.setText("ADD RELATION");
                this.canvas.getChildren().addAll(this.relation.getRelLine(), this.relation.getRelLineEnd());
                System.out.println(this.relation.getRelLine().toString());
            }

        });

        //clicking on clickable corner (actually rectangle) => changing if it is marked or not
        clickableCorner.setOnMouseClicked(mouseEvent -> {
            if (classObject == this.selectedClass){
                deselectClass(classObject);
            }else{
                setSelectedClass(classObject);
            }
        });

        //clicking on clickable corner => setting that it is ready to move
        clickableCorner.setOnMousePressed(mouseEvent -> {

            this.mousePrevX = mouseEvent.getX();
            this.mousePrevY = mouseEvent.getY();
        });

        //dragging the clickable corner and with it also the whole object
        clickableCorner.setOnMouseDragged(event -> {

            //preventing from overdrawing the pane surroundings => which is mysteriously possible
            //if the rectangle is selected => then do actions
            if((classObject.getClassBorder().getY() + (event.getY() - this.mousePrevY)) <= 0) return;

            //count the difference between previous and current mouse position for moving objects
            Double diffX = event.getX() - this.mousePrevX;
            Double diffY = event.getY() - this.mousePrevY;

            //previous position of mouse set to current position
            mousePrevX = event.getX();
            mousePrevY = event.getY();

            //changing position of each part of diagram
            //position of clickable corner -> actually rectangle at the top of the object
            clickableCorner.setX(clickableCorner.getX() + diffX);
            clickableCorner.setY(clickableCorner.getY() + diffY);

            //position of border of object
            classObject.getClassBorder().setX(classObject.getClassBorder().getX() + diffX);
            classObject.getClassBorder().setY(classObject.getClassBorder().getY() + diffY);

            //position of the rectangle representing the inner part of object (without border)
            classObject.getClassBox().setX(classObject.getClassBox().getX() + diffX);
            classObject.getClassBox().setY(classObject.getClassBox().getY() + diffY);

            //position of text => class name
            classObject.getClassName().setX(classObject.getClassName().getX() + diffX);
            classObject.getClassName().setY(classObject.getClassName().getY() + diffY);

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

            //position of text => each attribute
            for (Text attr : classObject.getListOfAttributes()){
                attr.setX(attr.getX() + diffX);
                attr.setY(attr.getY() + diffY);
            }

            //position of text => each operation
            for (Text op : classObject.getListOfOperations()){
                op.setX(op.getX() + diffX);
                op.setY(op.getY() + diffY);
            }

            //position of point where relation begins/ends
            //also redrawing relation line end (arrow, etc.)
            for (Relation rel : classObject.getListOfRelations()){
                if(rel.getRelClassFrom() == classObject){
                    rel.getRelLine().setStartX(rel.getRelLine().getStartX() + diffX);
                    rel.getRelLine().setStartY(rel.getRelLine().getStartY() + diffY);
                } else {
                    rel.getRelLine().setEndX(rel.getRelLine().getEndX() + diffX);
                    rel.getRelLine().setEndY(rel.getRelLine().getEndY() + diffY);
                }

                this.canvas.getChildren().remove(rel.getRelLineEnd());
                rel.setNewRelLineEndPosition();
                this.canvas.getChildren().add(rel.getRelLineEnd());
                this.canvas.getChildren().remove(rel.getNameOfRelation());
                rel.setNameOfRelation("neco");
            }

        });

    }

    //todo move this to do right place
    //for knowledge whether clicking on class border means creating new relation
    private boolean createRelation = false;
    //for temporary storing currently creating relation
    private Relation relation;
    private Relation relationForChange;
    private enum relType{ASSOCIATION, AGGREGATION, COMPOSITION, GENERALIZATION}

    /**
     * method handling clicking on button for creating relation
     * */
    public void btnAddRelation(ActionEvent e){
        //show dialog for getting the relation type
        relType rt = null;
        String options[] = {"Association", "Aggregation","Composition", "Generalization"};
        ChoiceDialog dlg = new ChoiceDialog(options[0], options);
        dlg.setHeaderText("Choose type of relation");
        Optional result = dlg.showAndWait();
        if (result.isPresent()){
            switch (dlg.getSelectedItem().toString()){
                case "Association": rt = relType.ASSOCIATION; break;
                case "Aggregation": rt = relType.AGGREGATION; break;
                case "Composition": rt = relType.COMPOSITION; break;
                case "Generalization": rt = relType.GENERALIZATION; break;
            }

        } else {
            //cancel was pressed -> so this cant continue
            return;
        }


        if (!this.createRelation){
            this.createRelation = true;
            //todo combobox dialog for choosing relation type
            this.relation = new Relation(rt);
            btnAddRelation.setText("CANCEL RELATION");
        } else {
            this.createRelation = false;
            this.relation = null;
            btnAddRelation.setText("ADD RELATION");
        }
    }

    /**
     * class for creating new graphic representation of class
     * */
    public class ClassObject {
        private Rectangle classBox;
        private Rectangle classBorder;
        private Rectangle clickableCorner;
        private Text className;
        private Line line1;
        private List<Text> listOfAttributes = new ArrayList<>();
        private Line line2;
        private List<Text> listOfOperations = new ArrayList<>();
        private List<Relation> listOfRelations = new ArrayList<>();

        /**
         * getters
         * */
        public Rectangle getClassBorder() {return this.classBorder;}
        public Rectangle getClassBox(){ return this.classBox;}
        public Rectangle getClickableCorner() {return this.clickableCorner;}
        public List<Text> getListOfAttributes() {return this.listOfAttributes;}
        public List<Text> getListOfOperations() {return this.listOfOperations;}
        public List<Relation> getListOfRelations() {return this.listOfRelations;}
        public Text getClassName() {return this.className; }
        public Line getLine1() { return this.line1; }
        public Line getLine2() { return this.line2; }

        /**
         * constructor for creating the class object
         * creating all graphical objects for necessary for empty class
         * @param name name of the class
         * */
        public ClassObject(String name){

            //create border of the object
            Rectangle rectangleBorder = new Rectangle(100,130, Color.BLACK);
            this.classBorder = rectangleBorder;
            rectangleBorder.setX(0);
            rectangleBorder.setY(0);
            rectangleBorder.setCursor(Cursor.CROSSHAIR);

            //create overall classbox => of rectangle
            Rectangle rectangle = new Rectangle(90,120,Color.rgb(237, 233, 221, 0.6));
            this.classBox = rectangle;
            rectangle.setX(5);
            rectangle.setY(5);

            //sets the class name
            Text className = new Text(name);
            className.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
            className.setY(rectangle.getY() + 35);
            className.setX(rectangle.getX() + 5);
            this.className = className;

            //resizing both rectangle and its border
            rectangle.setWidth(className.getLayoutBounds().getWidth()+ 10);
            rectangleBorder.setWidth(className.getLayoutBounds().getWidth()+ 20);

            //create clickable corner
            Rectangle clickableCorner = new Rectangle(rectangleBorder.getWidth() / 2,20, deselectedClassColor);
            clickableCorner.setX(rectangleBorder.getX() + rectangleBorder.getWidth()/2 - clickableCorner.getWidth()/2 );
            clickableCorner.setY(rectangle.getY());
            clickableCorner.setCursor(Cursor.MOVE);
            this.clickableCorner = clickableCorner;

            //sets the two lines which will divide the space of classbox to three parts
            // 1) class name 2) class attributes 3) class operations
            Line line1 = new Line();
            line1.setStartX(rectangle.getX());
            line1.setStartY(className.getY() + 15);
            line1.setEndX(rectangle.getX() + rectangle.getWidth());
            line1.setEndY(line1.getStartY());
            this.line1 = line1;

            Line line2 = new Line();
            line2.setStartX(rectangle.getX());
            line2.setStartY(line1.getStartY() + 30);
            line2.setEndX(rectangle.getX() + rectangle.getWidth());
            line2.setEndY(line2.getStartY());
            this.line2 = line2;
        }

        /**
         * @param attributeText full text of attribute
         * method for adding attribute to class diagram graphical representation
         * */
        public Text addAttribute(String attributeText){
            Text attribute = new Text(attributeText);

            if (listOfAttributes.isEmpty()){
                attribute.setY(this.getLine1().getStartY() + 15);
                attribute.setX(this.getClassName().getX());

                classBox.setHeight(classBox.getHeight() + 15);
            } else{
                Text lastAttr = listOfAttributes.get(listOfAttributes.size() -1);
                attribute.setY(lastAttr.getY() + 15);
                attribute.setX(lastAttr.getX());

                classBox.setHeight(classBox.getHeight() + 15);
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

        /**
         * @param operationText full text of operation
         * method for adding opperation to class diagram graphical representation
         * */
        public Text addOperation(String operationText){
            Text operation = new Text(operationText);
            if (listOfOperations.isEmpty()){
                operation.setY(this.getLine2().getStartY() + 15);
                operation.setX(this.getClassName().getX());
            }
            else {
                Text lastOp = listOfOperations.get(listOfOperations.size() -1 );
                operation.setY(lastOp.getY() + 15);
                operation.setX(lastOp.getX());
            }

            listOfOperations.add(operation);
            classBox.setHeight(classBox.getHeight() + 15);
            return operation;
        }

        /**
         * @param relation relation representing objects in the relation and relations data
         * */
        public void addRelation(Relation relation){
            this.listOfRelations.add(relation);
        }
    }

    /**
     * class for storing the relation between two classes
     * */
    public class Relation{
        private boolean relationFromSet = false;
        private final relType relationType;

        private ClassObject relClassFrom;
        private ClassObject relClassTo;

        private final Line relLine;

        private Polygon relLineEnd;
        //these to lines are here for the option of association relation => which is created of one simple arrow
        private Line line1 = null;
        private Line line2 = null;

        //labels on the relation line
        private Text cardinalityByToClass;
        private Text cardinalityByFromClass;
        private Text nameOfRelation;

        /**
         * constructor
         * @param type type of the relation
         * creating the line and its event for handling selecting this line
         * setting up the relation type
         * */
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


        /**
         * set all information about relation beginning
         * @param relClassFrom class that is at beginning of this relation
         * @param X X coordinate of the point where the relation starts
         * @param Y Y coordinate of the point where the relation starts
         * */
        public void setRelationFrom(ClassObject relClassFrom, double X, double Y){
            this.relLine.setStartX(X);
            this.relLine.setStartY(Y);
            this.relationFromSet = true;
            this.relClassFrom = relClassFrom;
        }

        /**
         * set all information about relation ending
         * also creating sufficient line ending
         * @param relClassTo class that is at end of this relation
         * @param X X coordinate of the point where the relation ends
         * @param Y Y coordinate of the point where the relation ends
         * */
        public void setRelationTo(ClassObject relClassTo, double X, double Y){
            this.relLine.setEndX(X);
            this.relLine.setEndY(Y);
            this.relClassTo = relClassTo;

            //set up the polygon representing the relation line ending
            this.relLineEnd = new Polygon();

            //creates relation line ending
            setNewRelLineEndPosition();

            setNameOfRelation("neco");
        }

        /**
         * set the ending polygon which sets the type of the relation
         * or arrow which is compound of two lines
         * */
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

        }

        public void setCardinalityByFromClass(String cardinality){

        }

        /**
         * getters
         * */
        public Text getNameOfRelation() {return this.nameOfRelation;}
        public boolean getRelationFromSet() {return this.relationFromSet; }
        public ClassObject getRelClassFrom() {return this.relClassFrom; }
        public ClassObject getRelClassTo() {return this.relClassTo; }
        public Line getRelLine() {return this.relLine; }
        public Polygon getRelLineEnd() {return this.relLineEnd; }
    }
}
