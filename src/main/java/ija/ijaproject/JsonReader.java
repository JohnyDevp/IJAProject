package ija.ijaproject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import ija.ijaproject.cls.*;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// import java.lang.ProcessBuilder.Redirect.Type;

/**
 * class for parsing json file into class and sequence diagram
 * 
 * @author xholan11
 */
public class JsonReader {

    /**
     * this is class diagram which will be
     */
    private ClassDiagram clsDiagram = new ClassDiagram("");
    private ArrayList<SequenceDiagram> sequenceDiagrams = new ArrayList<SequenceDiagram>();
    private ArrayList<UMLRelation> relations = new ArrayList<UMLRelation>();

    /**
     * getters
     */
    public ClassDiagram getClsDiagram() {
        return this.clsDiagram;
    }

    /**
     * method parsing the json file to class diagram representation
     *
     * @param filePath path to json file
     */
    public boolean parseJsonClassDiagram(String filePath) {

        try {

            // parsing file
            JsonObject obj = (JsonObject) new JSONParser().parse(new FileReader(filePath));
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(UMLClassInterfaceTemplate.class,
                    new UMLClassInterfaceTemplateSeriliazer());
            builder.registerTypeAdapter(UMLClassInterfaceTemplate.class,
                    new UMLClassInterfaceTemplateDesirializer());
            Gson gson = builder.create();

            this.clsDiagram = gson.fromJson(obj.get("classDiagram"), ClassDiagram.class);

            System.out.print(("Loaded"));

        } catch (Exception e) {
            System.out.println("ERROR: Loading json file - bad structure");
            return false;
        }

        return true;
    }

    /**
     * method parsing the json file to sequence diagrams representation
     *
     * @param filePath path to json file
     */
    public boolean parseJsonSequenceDiagrams(String filePath) {

        // parsing file

        try {
            JsonObject obj = (JsonObject) new JSONParser().parse(new FileReader(filePath));
            GsonBuilder builder = new GsonBuilder();

            Gson gson = builder.create();

            Type sequenceListType = new TypeToken<ArrayList<SequenceDiagram>>() {
            }.getType();
            this.sequenceDiagrams = gson.fromJson(obj.get("sequenceDiagrams"), sequenceListType);

            return true;
        } catch (JsonSyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public boolean parseJsonRelationships(String filePath) {

        try {
            JsonObject obj = (JsonObject) new JSONParser().parse(new FileReader(filePath));
            GsonBuilder builder = new GsonBuilder();

            Gson gson = builder.create();

            Type relationListType = new TypeToken<ArrayList<UMLRelation>>() {
            }.getType();
            this.sequenceDiagrams = gson.fromJson(obj.get("relationships"), relationListType);

            return true;
        } catch (JsonSyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    /**
     * parsing class diagram
     * method for adding new class with all its information
     *
     * @param mClasses array of all class definitions(operations, name, attributes,
     *                 x, y,...)
     */
    private boolean addClass(Map mClasses) {
        // create new uml class - intern representation (non-graphical)
        UMLClass umlClass;

        /**/
        // get name of class
        String name = (String) mClasses.get("name");
        if (name != null) {
            // create new class and handle potential inconsistency
            umlClass = clsDiagram.createClass(name);
            if (umlClass == null) {
                System.out.println("ERROR: Two classes of the same name, the second one has been removed");
            }
        } else {
            System.out.println("ERROR: Currently created class has no class name: removed.");
            return false;
        }

        // get the xcoord
        Double xcoord = (Double) mClasses.get("Xcoord");
        if (xcoord != null) {
            // add xcoord to representation of new uml class
            umlClass.setXcoord(xcoord);
        } else {
            System.out.println("ERROR: Currently created class has no xcoord: removed.");
            return false;
        }

        // get the ycoord
        Double ycoord = (Double) mClasses.get("Ycoord");
        if (ycoord != null) {
            // add ycoord to representation of new uml class
            umlClass.setYcoord(ycoord);
        } else {
            System.out.println("ERROR: Currently created class has no ycoord: removed.");
            return false;
        }

        // get the operations
        JSONArray jarOperations = (JSONArray) mClasses.get("operations");
        if (jarOperations != null) {
            // loop through array of operations, add them to the umlCLass
            Iterator itrThroughOperations = jarOperations.iterator();
            while (itrThroughOperations.hasNext()) {

                // create new umlOperation
                UMLOperation umlOperation;
                Map mOperation = (Map) itrThroughOperations.next();

                // get operation name and return type and create operation (call its
                // constructor)
                String operationName = (String) mOperation.get("name");
                String operationModifier = (String) mOperation.get("modifier");
                String returnType = (String) mOperation.get("rettype");

                if (operationName != null && returnType != null && operationModifier != null) {
                    umlOperation = new UMLOperation(operationName, returnType, operationModifier.toCharArray()[0]);
                } else {
                    return false;
                }

                // get operations params, loop through them and add them to umlOperation
                JSONArray jarAttributes = (JSONArray) mOperation.get("params");
                if (jarAttributes != null) {
                    // loop through params
                    Iterator itrThroughParams = jarAttributes.iterator();
                    while (itrThroughParams.hasNext()) {
                        Map mParam = (Map) itrThroughParams.next();

                        // create new umlAttribute (as param of operation) and add values to it
                        UMLAttribute umlAttribute;
                        String paramName = (String) mParam.get("name");
                        String type = (String) mParam.get("type");
                        if (paramName != null && type != null) {
                            umlAttribute = new UMLAttribute(paramName, type);
                        } else {
                            return false;
                        }

                        // add the param to operation
                        umlOperation.addOperationParameter(umlAttribute);
                    }
                }

                // add operation to umlClass
                umlClass.addOperation(umlOperation);
            }
        } else {
            System.out.println("ERROR: Currently created class has no operations array: removed.");
            return false;
        }

        // get the attributes
        JSONArray jarAttributes = (JSONArray) mClasses.get("attributes");
        if (jarAttributes != null) {
            // loop through attributes and add them to the umlClass
            Iterator itrThroughAttributes = jarAttributes.iterator();
            while (itrThroughAttributes.hasNext()) {
                Map mAttributes = (Map) itrThroughAttributes.next();

                // create new umlAttribute
                UMLAttribute umlAttribute;

                String attrModifier = (String) mAttributes.get("modifier");
                String attrName = (String) mAttributes.get("name");
                String attrType = (String) mAttributes.get("type");

                // if name and type are set then called constructor of umlAttribute
                if (attrModifier != null && attrName != null && attrType != null) {
                    if (!"#~+-".contains(attrModifier))
                        return false; // attribute has bad modifier
                    umlAttribute = new UMLAttribute(attrModifier.toCharArray()[0], attrName, attrType);
                } else {
                    return false;
                }

                // add umlAttribute to the class
                umlClass.addAttribute(umlAttribute);
            }
        } else {
            System.out.println("ERROR: Currently created class has no attributes array: removed.");
            return false;
        }
        /**/

        return true;
    }

    /**
     * method for adding new interface with all its information
     *
     * @param mInterfaces array of all interface definitions(operations, name, x,
     *                    y,...)
     */
    private boolean addInterface(Map mInterfaces) {

        // create new uml class - intern representation (non-graphical)
        UMLInterface umlInterface;

        /**/
        // get name of class
        String name = (String) mInterfaces.get("name");
        if (name != null) {
            // create new class and handle potential inconsistency
            umlInterface = clsDiagram.createInterface(name);
            if (umlInterface == null) {
                System.out.println("ERROR: Two classes of the same name, the second one has been removed");
            }
        } else {
            System.out.println("ERROR: Currently created class has no class name: removed.");
            return false;
        }

        // get the xcoord
        Double xcoord = (Double) mInterfaces.get("Xcoord");
        if (xcoord != null) {
            // add xcoord to representation of new uml class
            umlInterface.setXcoord(xcoord);
        } else {
            System.out.println("ERROR: Currently created class has no xcoord: removed.");
            return false;
        }

        // get the ycoord
        Double ycoord = (Double) mInterfaces.get("Ycoord");
        if (ycoord != null) {
            // add ycoord to representation of new uml class
            umlInterface.setXcoord(xcoord);
        } else {
            System.out.println("ERROR: Currently created class has no ycoord: removed.");
            return false;
        }

        // get the operations
        JSONArray jarOperations = (JSONArray) mInterfaces.get("operations");
        if (jarOperations != null) {
            // loop through array of operations, add them to the umlCLass
            Iterator itrThroughOperations = jarOperations.iterator();
            while (itrThroughOperations.hasNext()) {

                // create new umlOperation
                UMLOperation umlOperation;
                Map mOperation = (Map) itrThroughOperations.next();

                // get operation name and return type and create operation (call its
                // constructor)
                String operationName = (String) mOperation.get("name");
                String operationModifier = (String) mOperation.get("modifier");
                String returnType = (String) mOperation.get("rettype");

                if (operationName != null && returnType != null && operationModifier != null) {
                    umlOperation = new UMLOperation(operationName, returnType, operationModifier.toCharArray()[0]);
                } else {
                    return false;
                }

                // get operations params, loop through them and add them to umlOperation
                JSONArray jarAttributes = (JSONArray) mOperation.get("params");
                if (jarAttributes != null) {
                    // loop through params
                    Iterator itrThroughParams = jarAttributes.iterator();
                    while (itrThroughParams.hasNext()) {
                        Map mParam = (Map) itrThroughParams.next();

                        // create new umlAttribute (as param of operation) and add values to it
                        UMLAttribute umlAttribute;
                        String paramName = (String) mParam.get("name");
                        String type = (String) mParam.get("type");
                        if (paramName != null && type != null) {
                            umlAttribute = new UMLAttribute(paramName, type);
                        } else {
                            return false;
                        }

                        // add the param to operation
                        umlOperation.addOperationParameter(umlAttribute);
                    }
                }

                // add operation to umlClass
                umlInterface.addOperation(umlOperation);
            }
        } else {
            System.out.println("ERROR: Currently created class has no operations array: removed.");
            return false;
        }

        return true;
    }

    /**
     * method for adding new relation with all its information
     *
     * @param mRelation array of all relation definitions(start class, end class, x,
     *                  y,...)
     */
    private boolean addRelation(Map mRelation) {
        // create new uml class - intern representation (non-graphical)
        UMLRelation umlRelation;

        /**/
        // get name of class
        String name = (String) mRelation.get("name");
        // get type of relation
        String relType = (String) mRelation.get("relType");
        // get classes of relation
        String relClassFrom = (String) mRelation.get("relClassFrom");
        String relClassTo = (String) mRelation.get("relClassTo");
        // get cardinality
        String cardinalityByFromCls = (String) mRelation.get("cardinalityByFromClass");
        String cardinalityByToCls = (String) mRelation.get("cardinalityByToClass");
        // get the coords
        Double startXcoord = (Double) mRelation.get("startXcoord");
        Double startYcoord = (Double) mRelation.get("startYcoord");
        Double endXcoord = (Double) mRelation.get("endXcoord");
        Double endYcoord = (Double) mRelation.get("endYcoord");

        // check for null
        if (name == null || relType == null || relClassFrom == null || relClassTo == null ||
                cardinalityByFromCls == null || cardinalityByToCls == null ||
                startXcoord == null || startYcoord == null || endXcoord == null || endYcoord == null) {
            System.out.println("Error: bad relation definition: removed");
            return false;
        }

        // create new relation
        umlRelation = clsDiagram.createRelation(name);

        // chane reltyp to upper case
        relType = relType.toUpperCase();
        // set relation type
        switch (relType) {
            case "ASSOCIATION":
                umlRelation.setRelationType(UMLRelation.RelationType.ASSOCIATION);
                break;
            case "COMPOSITION":
                umlRelation.setRelationType(UMLRelation.RelationType.COMPOSITION);
                break;
            case "AGGREGATION":
                umlRelation.setRelationType(UMLRelation.RelationType.AGGREGATION);
                break;
            case "GENERALIZATION":
                umlRelation.setRelationType(UMLRelation.RelationType.GENERALIZATION);
                break;
            default:
                System.out.println("Bad relation type: removed");
                return false;
        }

        // set classes - they have to already exist
        if (clsDiagram.findObject(relClassFrom) != null && clsDiagram.findObject(relClassTo) != null) {
            umlRelation.setRelationFromObject(clsDiagram.findObject(relClassFrom));
            umlRelation.setRelationToObject(clsDiagram.findObject(relClassTo));
        } else {
            System.out.println("Relation classes dont exist");
            return false;
        }

        // set relation cardinality
        if ((cardinalityByFromCls.matches("[0-9]+[\\.][\\.][0-9]+|[0-9]+[\\.][\\.][\\*]|[\\*]|[0-9]+")
                || cardinalityByFromCls == "") &&
                (cardinalityByToCls.matches("[0-9]+[\\.][\\.][0-9]+|[0-9]+[\\.][\\.][\\*]|[\\*]|[0-9]+")
                        || cardinalityByToCls == "")) {
            umlRelation.setCardinalityByFromClass(cardinalityByFromCls);
            umlRelation.setCardinalityByToClass(cardinalityByToCls);
        } else {
            System.out.println("Bad cardinality");
            return false;
        }
        // sets coords
        umlRelation.setStartX(startXcoord);
        umlRelation.setStartY(startYcoord);
        umlRelation.setEndX(endXcoord);
        umlRelation.setEndY(endYcoord);

        return true;
    }
}
