package ija.ijaproject;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassDiagramController {


    @FXML
    public Button btnAddClass;
    @FXML
    public Canvas canvasId;

    /**
     * variables for graphic
     * */
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;

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
     * overridden method
     * taking control and starting to work when tab is created
     */
    public void start(){
        Canvas canvas = this.canvasId;
        this.createCircle(canvas);

        canvas.setOnMouseClicked(e-> this.select(e));
        canvas.setOnMouseMoved(e -> { if(this.circle_selected) this.move(e, canvas); });

    }
    double mouse_x = 0.0;
    double mouse_y = 0.0;
    double circle_x = 10;
    double circle_y = 14;
    double height = 40;
    double width = 40;
    boolean circle_selected = false;

    //checks whether the mouse location is within the circle or not
    private void select(MouseEvent e) {

        double temp_mouse_x = e.getX();
        double temp_mouse_y = e.getY();
        double x_max = this.circle_x + this.width;
        double y_max = this.circle_y + this.height;
        System.out.println(temp_mouse_x + " >= " + this.circle_x + " " + temp_mouse_x + " <= " + x_max);
        System.out.println(temp_mouse_y + " >= " + this.circle_y + " " + temp_mouse_y + " <= " + y_max);
        boolean selected = temp_mouse_x >= this.circle_x && temp_mouse_x <= x_max // x-area
                &&
                temp_mouse_y >= this.circle_y && temp_mouse_y <= y_max; //y-area

        if(circle_selected && selected) {
            //deselect the circle if already selected
            circle_selected = false;
        }else {
            circle_selected = selected;
        }
        this.mouse_x = temp_mouse_x;
        this.mouse_y = temp_mouse_y;
        System.out.println(circle_selected  );
    }

    //move circle
    public void move(MouseEvent e, Canvas canvas) {
        double change_x = e.getX() - this.mouse_x;
        double change_y = e.getY() - this.mouse_y;
        this.circle_x += change_x;
        this.circle_y += change_y;
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        this.createCircle(canvas);
        this.mouse_x = e.getX();
        this.mouse_y = e.getY();
    }

    public void createCircle(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //outer circle
        Stop[] stops = new Stop[]{new Stop(0, Color.LIGHTSKYBLUE), new Stop(1, Color.BLUE)};
        LinearGradient gradient = new LinearGradient(0.5, 0, 0.5, 1, true, CycleMethod.NO_CYCLE, stops);
        gc.setFill(gradient);
        gc.fillOval(this.circle_x, this.circle_y, this.width, this.height);
        gc.translate(0, 0);
        gc.fill();
        gc.stroke();

        // Inner circle
        stops = new Stop[]{new Stop(0, Color.BLUE), new Stop(1, Color.LIGHTSKYBLUE)};
        gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        gc.setFill(gradient);
        gc.fillOval(this.circle_x + 3, this.circle_y + 3, this.width - 6, this.height - 6);
        gc.fill();
        gc.stroke();

    }


    /**
     * button action handling creating new tab and class for sequence diagram
     * */
    @FXML
    public void createNewSequenceDiagramBtn() throws Exception{
        diagramTabConstructor();
    }

    /**
     * overridden method
     * parsing file and loading it into tabPane if file has been set up
     * */
    protected void parseFile(){

    }

    /**
     * method for adding new tab for new sequence diagram
     * */
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



    /**
     * method handling clicking on button for adding new class
     * */
    @FXML
    public void btnAddClass(ActionEvent e){
        System.out.println("Creating a class design...");
        final Rectangle rectangle = createRectangle();

    }

    //test of creating object
    private Rectangle createRectangle(){
        final Rectangle rectangle = new Rectangle(100,100, Color.BLUEVIOLET);
        rectangle.setOpacity(0.7);
        return rectangle;
    }
}
