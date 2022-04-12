/**
 * Main class starting the application
 * @author xzimol04
 * */

package ija.ijaproject;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    //main class loading first design window
    @Override
    public void start(Stage stage) throws IOException {
        //load main fxml file and create the scene (stage is created => and passed as parameter)
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/home_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        //load css file
        String css = this.getClass().getResource("/ija/ijaproject/css/home_view.css").toExternalForm();
        scene.getStylesheets().add(css);

        //setting up the main window
        stage.setTitle("IJAProject");
        //set icon of the window
        Image icon = new Image("icon.png");
        stage.getIcons().add(icon);

        //set the current stage for the main controller
        MainController mainController = fxmlLoader.getController();
        mainController.setStage(stage);

        //load the scene to stage and open it up
        stage.setScene(scene);

        stage.show();

    }


    //MAIN METHOD => starting the application
    public static void main(String[] args) {
        //gives control to the design methods
        Application.launch();

    }
}