package ija.ijaproject;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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

    //TEST just for preparing purposes
    public static void testJson(){
        ClassDatatype rel = new ClassDatatype();
        rel.setHeader("ahoj");
        ArrayList methods = new ArrayList<String>();
        methods.add("int i");
        rel.setMethods(methods);

        ClassDatatype rel2 = new ClassDatatype();
        rel2.setHeader("ahoj");
        ArrayList methods2 = new ArrayList<String>();
        methods2.add("int i");
        rel2.setMethods(methods2);

        ArrayList<ClassDatatype> classes = new ArrayList<ClassDatatype>();
        classes.add(rel);
        classes.add(rel2);

        Gson gson = new Gson();
        String conv = gson.toJson(classes);

        //Write JSON file
        try (FileWriter file = new FileWriter("src/main/resources/fake.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            System.out.println("Hey there");
            file.write(conv);
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //MAIN METHOD => starting the application
    public static void main(String[] args) {
        //gives control to the design methods

        Application.launch();
//        testJson();

    }
}