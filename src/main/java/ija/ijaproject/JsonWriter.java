package ija.ijaproject;

import com.google.gson.Gson;
import ija.ijaproject.cls.ClassDiagram;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;

public class JsonWriter {

    /**method processing saving class diagram*/
    public boolean saveClassDiagramToFile(String pathToFile, ClassDiagram classDiagram){
        try{
            // create new JSONObject
            JSONObject jsonObject = new JSONObject();

            Gson gson = new Gson();
            gson.toJson(classDiagram, new FileWriter(pathToFile));

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
