package ija.ijaproject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import ija.ijaproject.cls.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Class responsible for writting apps content to file
 * 
 *
 * @author xzimol04, xholan11
 * @version 1.1
 */
public class JsonWriter {

    /**
     * 
     * saveAllToFile.
     * 
     *
     * @param seqDia   a {@link java.util.List} object
     * @param clsDia   a {@link ija.ijaproject.cls.ClassDiagram} object
     * @param filePath a {@link java.lang.String} object
     * @return a boolean
     */
    public boolean saveAllToFile(List<SequenceDiagram> seqDia, ClassDiagram clsDia, String filePath) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        builder.registerTypeAdapter(UMLClassInterfaceTemplate.class,
                new UMLClassInterfaceTemplateSeriliazer());
        builder.registerTypeAdapter(UMLClassInterfaceTemplate.class,
                new UMLClassInterfaceTemplateDesirializer());
        Gson gson = builder.create();

        Type sequenceListType = new TypeToken<ArrayList<SequenceDiagram>>() {
        }.getType();

        Program wholeProgram = new Program();

        wholeProgram.classDiagram = clsDia;
        wholeProgram.sequenceDiagrams = seqDia;

        String result = gson.toJson(wholeProgram, wholeProgram.getClass());

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(result);

            writer.close();
            return true;
        } catch (IOException e) {
            // TODO: handle exception
            return false;
        }

    }
}
