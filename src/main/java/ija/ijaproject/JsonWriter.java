package ija.ijaproject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ija.ijaproject.cls.ClassDiagram;
import ija.ijaproject.cls.UMLAttribute;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonWriter {

    /** method processing saving class diagram */
    public boolean saveClassDiagramToFile(String pathToFile, ClassDiagram classDiagram) {
        try {

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            UMLAttribute attr = new UMLAttribute('a', "tom", "string");

            String result = gson.toJson(attr);

            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
