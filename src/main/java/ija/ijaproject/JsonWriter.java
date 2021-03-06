package ija.ijaproject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import ija.ijaproject.cls.ClassDiagram;
import ija.ijaproject.cls.SequenceDiagram;
import ija.ijaproject.cls.UMLAttribute;
import ija.ijaproject.cls.UMLClassInterfaceTemplate;
import ija.ijaproject.cls.UMLClassInterfaceTemplateDesirializer;
import ija.ijaproject.cls.UMLClassInterfaceTemplateSeriliazer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonWriter {

    public boolean saveAllToFile(List<SequenceDiagram> seqDia, ClassDiagram clsDia) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        builder.registerTypeAdapter(UMLClassInterfaceTemplate.class,
                new UMLClassInterfaceTemplateSeriliazer());
        builder.registerTypeAdapter(UMLClassInterfaceTemplate.class,
                new UMLClassInterfaceTemplateDesirializer());
        Gson gson = builder.create();

        String clsString = gson.toJson(clsDia, clsDia.getClass());
        Type sequenceListType = new TypeToken<ArrayList<SequenceDiagram>>() {
        }.getType();
        String sequencesString = gson.toJson(seqDia, sequenceListType);

        JsonObject wholeFile = new JsonObject();
        wholeFile.getAsJsonObject().addProperty("classDiagram", clsString);
        wholeFile.getAsJsonObject().addProperty("sequenceDiagrams", sequencesString);

        String result = wholeFile.toString();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("examples/test1.json"));
            writer.write(result);

            writer.close();
            return true;
        } catch (IOException e) {
            // TODO: handle exception
            return false;
        }

    }

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

    /** method processing saving class diagram */
    public boolean saveSequenceDiagram(String pathToFile, SequenceDiagram sequenceDiagram) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting().serializeNulls();
            Gson gson = builder.create();

            // UMLAttribute attr = new UMLAttribute('a', "tom", "string");

            String result = gson.toJson(sequenceDiagram);

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
