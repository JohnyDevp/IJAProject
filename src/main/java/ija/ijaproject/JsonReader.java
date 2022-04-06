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

    private ClassDiagram classDiagram;
    private List<ClassDiagramController.Relation> listOfRelations = new ArrayList<>();
    /**
     * method parsing the json file to class diagram representation
     * @param filePath path to json file
     * */
    public ClassDiagram parseJsonClassDiagram(String filePath) {

        //creating new class diagram, name will be set when read
        ClassDiagram clsDlg = new ClassDiagram("");;
        this.classDiagram = clsDlg;

        try {

            // parsing file
            Object obj = new JSONParser().parse(new FileReader(filePath));

            // typecasting obj to JSONObject
            JSONObject jsonObject = (JSONObject) obj;

            //getting fields of all arrays => sequence diagrams, relations and class diagram(not an array)
            //class diagram
            Map classDiagram = ((Map) jsonObject.get("classDiagram"));
            // iterating address Map
            Iterator<Map.Entry> itr1 = classDiagram.entrySet().iterator();


            while (itr1.hasNext()) {
                Map.Entry clsDiagramElement = itr1.next();

                //classify part of class diagram definition
                if (clsDiagramElement.getKey().equals("name")){
                    clsDlg.setName((String)clsDiagramElement.getValue());
                } else if (clsDiagramElement.getKey().equals("classes")){

                    //classes array in json => in root
                    //save them as json array
                    JSONArray jarClasses = (JSONArray) clsDiagramElement.getValue();
                    Iterator itrThroughClasses = jarClasses.iterator();
                    while (itrThroughClasses.hasNext()) {
                        //call for add class and check for failure
                        if(!addClass((Map)itrThroughClasses.next())){
                            return null;
                        }
                    }

                } else if (clsDiagramElement.getKey().equals("interfaces")){
                    JSONArray jarInterfaces = (JSONArray) clsDiagramElement.getValue();
                } else if (clsDiagramElement.getKey().equals("relations")){
                    JSONArray jarRelations = (JSONArray) clsDiagramElement.getValue();
                }
            }

            //sequence diagrams
            /*JSONArray sequenceDiagrams = ((JSONArray) jsonObject.get("sequenceDiagrams"));

            Iterator itr2 = sequenceDiagrams.iterator();
            while (itr2.hasNext()) {
                System.out.println(itr2.toString());
            }*/




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

    /**
     * method for adding new class with all its information
     * @param mClasses array of all class definitions(operations, name, attributes, x, y,...)
     * */
    private boolean addClass(Map mClasses){
        Iterator<Map.Entry> itr1 = mClasses.entrySet().iterator();

        //this variable is handling sum, that's pointing out whether were defined all the necessary information in json
        int controlSum = 0;

        while (itr1.hasNext()) {
            Map.Entry classPart = itr1.next();

            switch ((String)classPart.getKey()){
                case "coordX":{
                    controlSum += 1;
                }
                    break;
                case "coordY":{
                    controlSum += 1;

                }

                    break;
                case "operations": {
                    controlSum += 1;
                }
                    break;

                case "attributes":{
                    controlSum += 1;
                }
                    break;

            }
        }

        return (controlSum == 4);
    }

    /**
     * method for adding new interface with all its information
     * @param mInterface array of all interface definitions(operations, name, x, y,...)
     * */
    private void addInterface(Map mInterface){

        Iterator<Map.Entry> itr = mInterface.entrySet().iterator();

        while (itr.hasNext()) {
            Map.Entry classPart = itr.next();

            switch ((String)classPart.getKey()){
                case "name":{

                }
                    break;

                case "coordX": {

                }
                    break;
                case "coordY":{

                }
                    break;
                case "operations":{}
                    break;

            }
        }
    }

    /**
     * method for adding new relation with all its information
     * @param mRelations array of all relation definitions(start class, end class, x, y,...)
     * */
    private void addRelation(Map mRelations){
        Iterator<Map.Entry> itr = mRelations.entrySet().iterator();

        while (itr.hasNext()) {
            Map.Entry classPart = itr.next();

            switch ((String) classPart.getKey()) {
                case "relClassFrom": {

                }
                break;

                case "relClassTo": {

                }
                break;
                case "coordY": {

                }
                break;
                case "operations": {
                }
                break;

            }
        }
    }
}
