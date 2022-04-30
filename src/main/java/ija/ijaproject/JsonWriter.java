package ija.ijaproject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ija.ijaproject.cls.ClassDiagram;
import ija.ijaproject.cls.UMLAttribute;
import ija.ijaproject.cls.UMLClassInterfaceTemplate;
import ija.ijaproject.cls.UMLClassInterfaceTemplateDesirializer;
import ija.ijaproject.cls.UMLClassInterfaceTemplateSeriliazer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;

import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonWriter {

    /** method processing saving class diagram */
    public boolean saveClassDiagramToFile(String pathToFile, ClassDiagram classDiagram) {
        try {

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting().serializeNulls();
            builder.registerTypeAdapter(UMLClassInterfaceTemplate.class,
                    new UMLClassInterfaceTemplateSeriliazer());
            builder.registerTypeAdapter(UMLClassInterfaceTemplate.class,
                    new UMLClassInterfaceTemplateDesirializer());
            Gson gson = builder.create();

            // UMLAttribute attr = new UMLAttribute('a', "tom", "string");

            String result = gson.toJson(classDiagram);

            System.out.println(result);

            BufferedWriter writer = new BufferedWriter(new FileWriter("examples/test1.json"));
            writer.write(result);

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
