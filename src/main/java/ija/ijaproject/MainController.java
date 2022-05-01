package ija.ijaproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * controller for handling creating or loading new class diagram from main
 * window
 * 
 * @author xholan11
 */
public class MainController {
    /**
     * btnLoadClassDiagram button from the main view
     * btnCreateNewClassDiagram button from the main view
     * btnLoadSequenceDiagram button from the main view
     * btnCreateNewSequenceDiagram button from the main view
     * toolBar from the main view
     * tabPane from the main view
     */
    @FXML
    public Button btnLoadClassDiagram;
    @FXML
    public Button btnCreateNewClassDiagram;
    @FXML
    public ToolBar toolBarTb;
    @FXML
    public TabPane tabPane;

    /**
     * stage for view which is handling by this controller
     */
    private Stage stage;

    public void initialize() throws IOException {
        // show creating dialog
        FXMLLoader fxmlLoaderWelcome = new FXMLLoader(getClass().getResource("views/welcome_view.fxml"));
        Parent parent = fxmlLoaderWelcome.load();
        WelcomeViewController dlgController = fxmlLoaderWelcome.<WelcomeViewController>getController();

        Scene sceneWelcome = new Scene(parent);
        Stage stageWelcome = new Stage();
        stageWelcome.initModality(Modality.APPLICATION_MODAL);
        stageWelcome.setScene(sceneWelcome);
        stageWelcome.showAndWait();

    }

    /**
     * @param stage
     *              setting the stage for this view
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * handling action when button createNewClassDiagramBtn is pressed
     * calling diagramTabConstructor with sufficient params
     */
    @FXML
    public void createNewClassDiagramBtn() throws Exception {
        diagramTabConstructor(false);
    }

    /**
     * handling action when button createNewClassDiagramBtn is pressed
     * calling diagramTabConstructor with sufficient params
     */
    @FXML
    public void saveAll() throws Exception {
        diagramTabConstructor(false);
    }

    /**
     * handling action when button loadClassDiagramBtn is pressed
     * calling diagramTabConstructor with sufficient params
     */
    @FXML
    public void loadClassDiagramBtn() throws Exception {
        diagramTabConstructor(true);
    }

    /**
     * handling action when button changeSettingsBtn is pressed
     */
    @FXML
    public void changeSettingsBtn() {
        System.out.println("Changing settings...");

    }

    /**
     * @param isLoader for know whether is the diagram loaded from existing file or
     *                 not
     *                 creates blank window for either sequence or class diagram
     */
    private void diagramTabConstructor(Boolean isLoader) throws Exception {
        System.out.println("Loading file diagram...");

        // get the path for loaded diagram - if there is request for load
        String filePath = "";
        if (isLoader) {
            try {
                // open file explorer dialog to choose a file to be load
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open file");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("JSON", "*.json"));
                File file = fileChooser.showOpenDialog(stage);
                System.out.println(filePath);

                // if the dialog has been closed then is necessary to exit the tab
                // raising an alert
                if (file == null) {
                    System.out.println("fail");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("No file has been chosen!");
                    alert.showAndWait();
                    return;
                } else {
                    // if the file has been chosen the set its path to variable
                    filePath = file.getPath();
                }
            } catch (Exception e) {
                System.out.println("Load file failure, exiting...");

            }
        }

        // create new tab
        Tab tab = new Tab("Class diagram");

        // load desired view to the anchorPane and then set the anchorPane as tab
        // content
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/classDiagram_view.fxml"));
        AnchorPane anch = loader.load();

        FXMLLoader loader2 = new FXMLLoader(Main.class.getResource("sequenceDiagram_view.fxml"));
        // AnchorPane anch2 = loader.load();

        // get controller from either sequence or class diagram
        ClassDiagramController classDiagramController = loader.getController();
        SequenceDiagramController sequenceDiagramController = loader2.getController();
        tab.setContent(anch);

        // add tab to tabPane
        this.tabPane.getTabs().add(tab);

        // sets all necessary information for DiagramController
        classDiagramController.setLoadedFilePath(filePath);
        classDiagramController.setTabPane(this.tabPane);
        classDiagramController.setMainController(this);

        // disable buttons for loading and creating class diagram
        this.btnLoadClassDiagram.setDisable(true);
        this.btnCreateNewClassDiagram.setDisable(true);

        classDiagramController.start();
    }
}