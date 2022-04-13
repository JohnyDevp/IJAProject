package ija.ijaproject;

import ija.ijaproject.cls.ClassDiagram;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonWriter {

    /**method processing saving class diagram*/
    public boolean saveClassDiagramToFile(String pathToFile, ClassDiagram classDiagram){
        try{
            // create new JSONObject
            JSONObject jsonObject = new JSONObject();

            //delete potentional previously created diagram
            Map jsonClsDlg = (Map)jsonObject.get("classDiagram");
            if (jsonClsDlg != null) { jsonObject.remove("classDiagram"); }

            //create the class diagram itself
            //basic information about class diagram
            Map mRoot = new LinkedHashMap(4);
            //add name to root
            mRoot.put("name", classDiagram.getName());

            //process classes==========================
            JSONArray jarClasses = new JSONArray();

            //put classes to root
            mRoot.put("classes",jarClasses);

            //process interfaces=======================
            JSONArray jarInterfaces = new JSONArray();
            //put interfaces to root
            mRoot.put("interfaces", jarInterfaces);

            //process relations==============================
            JSONArray jarRelations = new JSONArray();
            //put relations to root
            mRoot.put("relations", jarRelations);


            //final put whole class diagram to json
            mRoot.put("classDiagram", mRoot);



        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
