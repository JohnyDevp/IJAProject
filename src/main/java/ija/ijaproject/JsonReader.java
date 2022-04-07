package ija.ijaproject;

import java.io.FileReader;
import java.util.*;

import ija.ijaproject.cls.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;


public class JsonReader {

    /**
     * this is class diagram which will be*/
    private ClassDiagram clsDiagram = new ClassDiagram("");

    /**
     * getters*/
    public ClassDiagram getClsDiagram() {
        return this.clsDiagram;
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
                else if (clsDiagramElement.getKey().equals("interfaces")) {
                    //array of interfaces in json => in root
                    //save them as json array
                    JSONArray jarInterfaces = (JSONArray) clsDiagramElement.getValue();
                    Iterator itrThroughInterface = jarInterfaces.iterator();
                    while (itrThroughInterface.hasNext()) {
                        //call for add class and check for failure
                        if (!addInterface((Map) itrThroughInterface.next())) {
                            return false;
                        }
                    }
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

    /**
     * method parsing the json file to sequence diagrams representation
     * @param filePath path to json file
     */
    public boolean parseJsonSequenceDiagrams(String filePath){
        List<SequenceDiagram> listOfSequenceDiagrams = new ArrayList<>();

        return true;
    }

    /**
     * parsing class diagram
     * method for adding new class with all its information
     * @param mClasses array of all class definitions(operations, name, attributes, x, y,...)
     */
    private boolean addClass(Map mClasses){
        //create new uml class => intern representation (non-graphical)
        UMLClass umlClass;


        /**/
        //get name of class
        String name = (String)mClasses.get("name");
        if (name != null) {
            //create new class and handle potential inconsistency
            umlClass = clsDiagram.createClass(name);
            if (umlClass == null ) { System.out.println("ERROR: Two classes of the same name, the second one has been removed"); }
        }
        else { System.out.println("ERROR: Currently created class has no class name: removed.");return false;}

        //get the xcoord
        Double xcoord = (Double)mClasses.get("Xcoord");
        if (xcoord != null) {
            //add xcoord to representation of new uml class
            umlClass.setXcoord(xcoord);
        }
        else { System.out.println("ERROR: Currently created class has no xcoord: removed.");return false;}

        //get the ycoord
        Double ycoord = (Double)mClasses.get("Ycoord");
        if (ycoord != null) {
            //add ycoord to representation of new uml class
            umlClass.setXcoord(xcoord);
        }
        else { System.out.println("ERROR: Currently created class has no ycoord: removed.");return false;}

        //get the operations
        JSONArray jarOperations = (JSONArray) mClasses.get("operations");
        if (jarOperations != null){
            //loop through array of operations, add them to the umlCLass
            Iterator itrThroughOperations = jarOperations.iterator();
            while (itrThroughOperations.hasNext()) {

                //create new umlOperation
                UMLOperation umlOperation;
                Map mOperation = (Map)itrThroughOperations.next();

                //get operation name and return type and create operation (call its constructor)
                String operationName = (String)mOperation.get("name");
                String operationModifier = (String) mOperation.get("modifier");
                String returnType = (String)mOperation.get("rettype");

                if (operationName != null && returnType != null && operationModifier != null) {
                    umlOperation = new UMLOperation(operationName, returnType, operationModifier.toCharArray()[0]);
                }
                else {return false;}

                //get operations params, loop through them and add them to umlOperation
                JSONArray jarAttributes = (JSONArray) mOperation.get("params");
                if (jarAttributes != null){
                    //loop through params
                    Iterator itrThroughParams = jarAttributes.iterator();
                    while(itrThroughParams.hasNext()){
                        Map mParam = (Map)itrThroughParams.next();

                        //create new umlAttribute (as param of operation) and add values to it
                        UMLAttribute umlAttribute;
                        String paramName = (String)mParam.get("name");
                        String type = (String)mParam.get("type");
                        if (paramName != null && type != null){
                            umlAttribute = new UMLAttribute(paramName, type);
                        } else {return false;}

                        //add the param to operation
                        umlOperation.addOperationParameter(umlAttribute);
                    }
                }

                //add operation to umlClass
                umlClass.addOperation(umlOperation);
            }
        }
        else {System.out.println("ERROR: Currently created class has no operations array: removed.");return false;}

        //get the attributes
        JSONArray jarAttributes = (JSONArray) mClasses.get("attributes");
        if (jarAttributes != null){
            //loop through attributes and add them to the umlClass
            Iterator itrThroughAttributes = jarAttributes.iterator();
            while(itrThroughAttributes.hasNext()){
                Map mAttributes = (Map) itrThroughAttributes.next();

                //create new umlAttribtue
                UMLAttribute umlAttribute;

                String attrName = (String) mAttributes.get("name");
                String attrType = (String) mAttributes.get("type");

                //if name and type are set then called constructor of umlAttribute
                if(attrName != null && attrType != null){
                    umlAttribute = new UMLAttribute(attrName, attrType);
                } else {return false;}

                //add umlAttribute to the class
                umlClass.addAttribute(umlAttribute);
            }
        }
        else {System.out.println("ERROR: Currently created class has no attributes array: removed.");return false;}
        /**/

        return true;
    }

    /**
     * method for adding new interface with all its information
     * @param mInterfaces array of all interface definitions(operations, name, x, y,...)
     */
    private boolean addInterface(Map mInterfaces){

        //create new uml class => intern representation (non-graphical)
        UMLInterface umlInterface;

        /**/
        //get name of class
        String name = (String)mInterfaces.get("name");
        if (name != null) {
            //create new class and handle potential inconsistency
            umlInterface = clsDiagram.createInterface(name);
            if (umlInterface == null ) { System.out.println("ERROR: Two classes of the same name, the second one has been removed"); }
        }
        else { System.out.println("ERROR: Currently created class has no class name: removed.");return false;}

        //get the xcoord
        Double xcoord = (Double)mInterfaces.get("Xcoord");
        if (xcoord != null) {
            //add xcoord to representation of new uml class
            umlInterface.setXcoord(xcoord);
        }
        else { System.out.println("ERROR: Currently created class has no xcoord: removed.");return false;}

        //get the ycoord
        Double ycoord = (Double)mInterfaces.get("Ycoord");
        if (ycoord != null) {
            //add ycoord to representation of new uml class
            umlInterface.setXcoord(xcoord);
        }
        else { System.out.println("ERROR: Currently created class has no ycoord: removed.");return false;}

        //get the operations
        JSONArray jarOperations = (JSONArray) mInterfaces.get("operations");
        if (jarOperations != null){
            //loop through array of operations, add them to the umlCLass
            Iterator itrThroughOperations = jarOperations.iterator();
            while (itrThroughOperations.hasNext()) {

                //create new umlOperation
                UMLOperation umlOperation;
                Map mOperation = (Map)itrThroughOperations.next();

                //get operation name and return type and create operation (call its constructor)
                String operationName = (String)mOperation.get("name");
                String operationModifier = (String) mOperation.get("modifier");
                String returnType = (String)mOperation.get("rettype");

                if (operationName != null && returnType != null && operationModifier != null) {
                    umlOperation = new UMLOperation(operationName, returnType, operationModifier.toCharArray()[0]);
                }
                else {return false;}

                //get operations params, loop through them and add them to umlOperation
                JSONArray jarAttributes = (JSONArray) mOperation.get("params");
                if (jarAttributes != null){
                    //loop through params
                    Iterator itrThroughParams = jarAttributes.iterator();
                    while(itrThroughParams.hasNext()){
                        Map mParam = (Map)itrThroughParams.next();

                        //create new umlAttribute (as param of operation) and add values to it
                        UMLAttribute umlAttribute;
                        String paramName = (String)mParam.get("name");
                        String type = (String)mParam.get("type");
                        if (paramName != null && type != null){
                            umlAttribute = new UMLAttribute(paramName, type);
                        } else {return false;}

                        //add the param to operation
                        umlOperation.addOperationParameter(umlAttribute);
                    }
                }

                //add operation to umlClass
                umlInterface.addOperation(umlOperation);
            }
        }
        else {System.out.println("ERROR: Currently created class has no operations array: removed.");return false;}

        return true;
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
