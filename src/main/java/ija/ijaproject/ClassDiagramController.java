package ija.ijaproject;


import ija.ijaproject.cls.ClassDiagram;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassDiagramController {


    @FXML
    public Button btnAddClass;
    @FXML
    public Pane pane;

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
     * override method handling action when button close pressed
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

    public ClassObject cls;
    public Double mousePrevX;
    public Double mousePrevY;
    boolean classObjectSelected = false;


    /**
     * overridden method
     * taking control and starting to work when tab is created
     */
    public void start(){

        Pane canvas = this.pane;
        canvas.setCursor(Cursor.CROSSHAIR);


        ClassObject newObj = new ClassObject();
        newObj.createRectangle("New class");
        newObj.addAttribute("name : string");
        newObj.addAttribute("surname : string");
        newObj.addOperation("+ getName()");
        newObj.addOperation("+ getSurname()");
        Rectangle clickableCorner = newObj.getClickableCorner();

        this.cls = newObj;
        System.out.println(newObj.getListOfAttributes().toString());
        System.out.println(newObj.getListOfOperations().toString());

        //adding all objects elements to canvas
//        canvas.getChildren().add(rectangle);
        canvas.getChildren().addAll(newObj.getClassBox(), newObj.getClassName(), newObj.getLine1(), newObj.getLine2(), newObj.getClickableCorner());
        for (Text attr : newObj.getListOfAttributes()) {canvas.getChildren().add(attr);}
        for (Text op : newObj.getListOfOperations()) {canvas.getChildren().add(op);}

        //newObj.getClassName().setOnMouseClicked(mouseEvent -> rectangle.getOnMouseClicked());

        clickableCorner.setOnMouseClicked(mouseEvent -> {
            clickableCorner.setFill(Color.ORANGE);
        });

        clickableCorner.setOnMousePressed(mouseEvent -> {

            this.mousePrevX = mouseEvent.getX();
            this.mousePrevY = mouseEvent.getY();
            this.classObjectSelected = !classObjectSelected;
            System.out.println(this.mousePrevX + " " + this.mousePrevY+" "+classObjectSelected);
        });

        clickableCorner.setOnMouseDragged(event -> {

            //preventing from overdrawing the pane surroundings => which is mysteriously possible
            //if the rectangle is selected => then do actions
            if((clickableCorner.getY() + (event.getY() - this.mousePrevY)) <= 0) return;

            //count the difference between previous and current mouse position for moving objects
            Double diffX = event.getX() - this.mousePrevX;
            Double diffY = event.getY() - this.mousePrevY;

            //previous position of mouse set to current position
            mousePrevX = event.getX();
            mousePrevY = event.getY();

            //changing position of each part of diagram
            clickableCorner.setX(clickableCorner.getX() + diffX);
            clickableCorner.setY(clickableCorner.getY() + diffY);

            newObj.getClassBox().setX(newObj.getClassBox().getX() + diffX);
            newObj.getClassBox().setY(newObj.getClassBox().getY() + diffY);

            newObj.getClassName().setX(newObj.getClassName().getX() + diffX);
            newObj.getClassName().setY(newObj.getClassName().getY() + diffY);

            newObj.getLine1().setStartX(newObj.getLine1().getStartX() + diffX);
            newObj.getLine1().setStartY(newObj.getLine1().getStartY() + diffY);
            newObj.getLine1().setEndX(newObj.getLine1().getEndX() + diffX);
            newObj.getLine1().setEndY(newObj.getLine1().getEndY() + diffY);

            newObj.getLine2().setStartX(newObj.getLine2().getStartX() + diffX);
            newObj.getLine2().setStartY(newObj.getLine2().getStartY() + diffY);
            newObj.getLine2().setEndX(newObj.getLine2().getEndX() + diffX);
            newObj.getLine2().setEndY(newObj.getLine2().getEndY() + diffY);

            for (Text attr : newObj.getListOfAttributes()){
                attr.setX(attr.getX() + diffX);
                attr.setY(attr.getY() + diffY);
            }
            for (Text op : newObj.getListOfOperations()){
                op.setX(op.getX() + diffX);
               op.setY(op.getY() + diffY);
            }

        });

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


    /**
     * method handling clicking on button for adding new class
     * */
    @FXML
    public void btnAddClass(ActionEvent e){
        System.out.println("Creating a class design...");
        System.out.println(this.cls.getListOfAttributes());
        this.pane.getChildren().add(this.cls.addAttribute("new"));

    }

    /**
     * class for creating new graphic representation of class
     * */
    public class ClassObject {
        private Rectangle classBox;
        private Rectangle clickableCorner;
        private Text className;
        private Line line1;
        private List<Text> listOfAttributes = new ArrayList<>();
        private Line line2;
        private List<Text> listOfOperations = new ArrayList<>();

        public Rectangle getClassBox(){ return this.classBox;}
        public Rectangle getClickableCorner() {return this.clickableCorner;}
        public List<Text> getListOfAttributes() {return this.listOfAttributes;}
        public List<Text> getListOfOperations() {return this.listOfOperations;}
        public Text getClassName() {return this.className; }
        public Line getLine1() { return this.line1; }
        public Line getLine2() { return this.line2; }

        public void createRectangle(String name){
            //create overall classbox => of rectangle
            Rectangle rectangle = new Rectangle(100,100,Color.RED);
            this.classBox = rectangle;
            rectangle.setX(0);
            rectangle.setY(0);

            //create clickable corner
            Rectangle clickableCorner = new Rectangle(20,20, Color.BLUE);
            clickableCorner.setX(rectangle.getX() + rectangle.getWidth() - 20);
            clickableCorner.setY(rectangle.getY());
            this.clickableCorner = clickableCorner;

            //sets the class name
            Text className = new Text(name);
            className.setY(rectangle.getY() + 20);
            className.setX(rectangle.getX() + 10);
            this.className = className;

            //sets the two lines which will divide the space of classbox to three parts
            // 1) class name 2) class attributes 3) class operations
            Line line1 = new Line();
            line1.setStartX(rectangle.getX());
            line1.setStartY(rectangle.getY() + 40);
            line1.setEndX(rectangle.getX() + rectangle.getWidth());
            line1.setEndY(line1.getStartY());
            this.line1 = line1;

            Line line2 = new Line();
            line2.setStartX(rectangle.getX());
            line2.setStartY(rectangle.getY() + 60);
            line2.setEndX(rectangle.getX() + rectangle.getWidth());
            line2.setEndY(line2.getStartY());
            this.line2 = line2;
        }

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

    }
}
