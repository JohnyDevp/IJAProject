package ija.ijaproject;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ija.ijaproject.cls.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;


public class JsonReader {
    private ClassDiagram clsDiagram = new ClassDiagram("");
    private List<ClassObjectGUI> classObjectList = new ArrayList<>();

    private List<RelationGUI> listOfRelations = new ArrayList<>();

    /**
     * getters*/
    public ClassDiagram getClsDiagram() {
        return this.clsDiagram;
    }

    public List<RelationGUI> getListOfRelations() {
        return this.listOfRelations;
    }

    public List<ClassObjectGUI> getClassObjectList() {
        return classObjectList;
    }

    /**
     * method parsing the json file to class diagram representation
     * @param filePath path to json file
     */
    public boolean parseJsonClassDiagram(String filePath) {

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

            //read all information in class diagram
            while (itr1.hasNext()) {
                Map.Entry clsDiagramElement = itr1.next();

                //classify each part of class diagram definition

                if (clsDiagramElement.getKey().equals("name")){
                    this.clsDiagram.setName((String)clsDiagramElement.getValue());
                }
                else if (clsDiagramElement.getKey().equals("classes")){

                    //array of classes in json => in root
                    //save them as json array
                    JSONArray jarClasses = (JSONArray) clsDiagramElement.getValue();
                    Iterator itrThroughClasses = jarClasses.iterator();
                    while (itrThroughClasses.hasNext()) {
                        //call for add class and check for failure
                        if(!addClass((Map)itrThroughClasses.next())){
                            return false;
                        }
                    }

                }
                else if (clsDiagramElement.getKey().equals("interfaces")){
                    JSONArray jarInterfaces = (JSONArray) clsDiagramElement.getValue();
                }
                else if (clsDiagramElement.getKey().equals("relations")){
                    JSONArray jarRelations = (JSONArray) clsDiagramElement.getValue();
                }
            }

        } catch (Exception e){
            System.out.println("ERROR: Loading json file => bad structure");
            return false;
        }

        return true;
    }

    public List<SequenceDiagram> parseJsonSequenceDiagrams(String filePath){
        List<SequenceDiagram> listOfSequenceDiagrams = new ArrayList<>();

        return listOfSequenceDiagrams;
    }

    /**
     * parsing class diagram
     * method for adding new class with all its information
     * @param mClasses array of all class definitions(operations, name, attributes, x, y,...)
     */
    private boolean addClass(Map mClasses){
        Iterator<Map.Entry> itr1 = mClasses.entrySet().iterator();

        //create new guiclass object
        UMLClass umlClass = clsDiagram.createClass("tmpName");

        //this variable is handling sum, that's pointing out whether were defined all the necessary information in json
        int controlSum = 0;

        while (itr1.hasNext()) {
            Map.Entry classItem = itr1.next();

            switch ((String)classItem.getKey()){
                case "name":{
                    controlSum +=1;
                    umlClass.setName((String) classItem.getValue());
                }
                    break;
                case "coordX":{
                    controlSum += 1;
                    umlClass.setXcoord((Double) classItem.getValue());
                }
                    break;
                case "coordY":{
                    controlSum += 1;
                    umlClass.setYcoord((Double) classItem.getValue());
                }
                    break;
                case "operations": {
                    controlSum += 1;
                    //array of operations in json => in root
                    //save them as json array
                    JSONArray jarOperations = (JSONArray) classItem.getValue();
                    Iterator itrThroughOperations = jarOperations.iterator();
                    while (itrThroughOperations.hasNext()) {
                        Map mOperations = (Map)itrThroughOperations.next();
                        Iterator<Map.Entry> itrOperations = mOperations.entrySet().iterator();

                        //creating new operation
                        UMLOperation umlOperation = new UMLOperation("");

                        //iterate through items of one operation
                        while(itrOperations.hasNext()){
                            Map.Entry operationItem =itrOperations.next();

                            switch ((String) operationItem.getKey()) {
                                case "name":
                                    umlOperation.setName((String) operationItem.getValue());
                                    break;
                                case "rettype":
                                    umlOperation.setType((String) operationItem.getValue());
                                    break;
                                case "params":
                                    JSONArray jarOperationArg = (JSONArray) operationItem.getValue();
                                    Iterator itrThroughOperationArg = jarOperationArg.iterator();

                                    while (itrThroughOperationArg.hasNext()){
                                        Map mOpArg = (Map)itrThroughOperationArg.next();
                                        Iterator<Map.Entry> itrOpArg = mOpArg.entrySet().iterator();

                                        //creates instance of UMLAttribute to store this parameter
                                        UMLAttribute param = new UMLAttribute("");

                                        while(itrOpArg.hasNext()){
                                            Map.Entry argItem = itrOpArg.next();
                                            switch ((String) argItem.getKey()){
                                                case "name":
                                                    param.setName((String) argItem.getValue());
                                                    break;
                                                case "type":
                                                    param.setType((String) argItem.getValue());
                                                    break;
                                            }
                                        }
                                        //add the parameter to operation
                                        umlOperation.addOperationParameter(param);
                                    }
                                    break;
                            }
                        }

                        //add operation to class
                        umlClass.addOperation(umlOperation);

                    }

                }
                    break;

                case "attributes":{
                    controlSum += 1;
                    //array of attributes in json => in root
                    //save them as json array
                    JSONArray jarOperations = (JSONArray) classItem.getValue();
                    Iterator itrThroughOperations = jarOperations.iterator();
                    while (itrThroughOperations.hasNext()) {
                        Map mOperations = (Map)itrThroughOperations.next();
                        Iterator<Map.Entry> itrOperations = mOperations.entrySet().iterator();

                        UMLAttribute umlAttribute = new UMLAttribute("");

                        //iterate through items of one operation
                        while(itrOperations.hasNext()){
                            Map.Entry operationItem =itrOperations.next();

                            switch ((String) operationItem.getKey()) {
                                case "name":
                                    umlAttribute.setName((String) operationItem.getValue());
                                    break;
                                case "type":
                                    umlAttribute.setType((String) operationItem.getValue());
                                    break;
                            }
                        }
                        umlClass.addAttribute(umlAttribute);

                    }
                }
                    break;
            }
        }

        return (controlSum == 5);
    }

    /**
     * method for adding new interface with all its information
     * @param mInterface array of all interface definitions(operations, name, x, y,...)
     */
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
     */
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
