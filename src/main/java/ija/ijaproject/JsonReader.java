package ija.ijaproject;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ija.ijaproject.cls.ClassDiagram;
import ija.ijaproject.cls.SequenceDiagram;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class JsonReader {

    /**
     * method parsing the json file to class diagram representation
     * @param filePath path to json file
     * */
    public ClassDiagram parseJsonClassDiagram(String filePath) {

        ClassDiagram clsDlg = new ClassDiagram("");;

        try {

            // parsing file
            Object obj = new JSONParser().parse(new FileReader(filePath));

            // typecasting obj to JSONObject
            JSONObject jsonObject = (JSONObject) obj;

            //getting fields of all arrays => sequence diagrams, relations
            //and class diagram(not an array)
            //class diagram
            Map classDiagram = ((Map) jsonObject.get("classDiagram"));
            // iterating address Map
            Iterator<Map.Entry> itr1 = classDiagram.entrySet().iterator();
            while (itr1.hasNext()) {
                Map.Entry clsDiagElement = itr1.next();
                if (clsDiagElement.getKey() == "name"){
                    clsDlg.setName((String)clsDiagElement.getValue());
                } else if (clsDiagElement.getKey() == "classes"){
                    JSONArray jarClasses = (JSONArray) clsDiagElement.getValue();
                    Iterator itr2 = jarClasses.iterator();
                    while (itr2.hasNext()) {
                        System.out.println(itr2.ge);
                    }

                } else if (clsDiagElement.getKey() == "interfaces"){
                    JSONArray jarInterfaces = (JSONArray) clsDiagElement.getValue();
                } else if (clsDiagElement.getKey() == "relations"){
                    JSONArray jarRelations = (JSONArray) clsDiagElement.getValue();
                }
            }

            //sequence diagrams
            /*JSONArray sequenceDiagrams = ((JSONArray) jsonObject.get("sequenceDiagrams"));

            Iterator itr2 = sequenceDiagrams.iterator();
            while (itr2.hasNext()) {
                System.out.println(itr2.toString());
            }*/

            //relations
            JSONArray relations = ((JSONArray) jsonObject.get("relations"));



        } catch (Exception e){
            System.out.println("ERROR: Loading json file => bad structure");
            return null;
        }

        return clsDlg;
    }

    public List<SequenceDiagram> parseJsonSequenceDiagrams(String filePath){
        List<SequenceDiagram> listOfSequenceDiagrams = new ArrayList<>();

        return listOfSequenceDiagrams;
    }

    private void addClass(Map jarClasses){

    }

    private void addInterface(Map jarClasses){

    }

    private void addRelation(Map jarClasses){

    }
}
