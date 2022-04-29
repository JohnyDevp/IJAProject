package ija.ijaproject.cls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * class representing template for both interface and class
 * 
 * @author xholan11
 */
public abstract class UMLClassInterfaceTemplate extends Element {
    /**
     * list of operations of class
     */
    protected List<UMLOperation> umlOperationsList = new ArrayList<UMLOperation>();

    /**
     * variables for storing position of this class on pane
     */
    public double Xcoord = 0.0;
    public double Ycoord = 0.0;

    /**
     * constructor
     * Creating an instance of object representing object in UML diagram
     * 
     * @param name name of element
     */
    public UMLClassInterfaceTemplate(String name) {
        super(name);
    }

    /**
     * setter
     * 
     * @param xcoord X coordination of the object on pane
     */
    public void setXcoord(double xcoord) {
        Xcoord = xcoord;
    }

    /**
     * setter
     * 
     * @param ycoord Y coordination of the object on pane
     */
    public void setYcoord(double ycoord) {
        Ycoord = ycoord;
    }

    /**
     * getter
     * 
     * @return X coordination of object
     */
    public double getXcoord() {
        return Xcoord;
    }

    /**
     * getter
     * 
     * @return Y coordination of object
     */
    public double getYcoord() {
        return Ycoord;
    }

    /**
     * adding an operation (which is parameter) of this class
     * 
     * @param operation parameter represent the whole operation
     * @return true or false according successfullness of this method
     */
    public boolean addOperation(UMLOperation operation) {
        // operation can exists with same name but not with same return type
        if (umlOperationsList.contains(operation)) {
            // list already contains the attribute
            return false;
        } else {
            // test each operation already added with operation desired to add
            for (UMLOperation umlOperation : umlOperationsList) {
                // different name -> continue
                if (!umlOperation.getName().equals(operation.getName())) {
                    continue;
                }

                // each param is the same in order (considering adding operation and some of
                // existing operations) - error
                // loop through params in operation and test for conformity

                // test for size of both lists - if different, continue (no error then)
                if (operation.getParametersOfOperationList().size() != umlOperation.getParametersOfOperationList()
                        .size()) {
                    continue;
                }

                boolean error = true;
                for (int index = 0; index < operation.getParametersOfOperationList().size(); index++) {
                    // get first param of adding operation
                    UMLAttribute paramFromOpForAdding = operation.getParametersOfOperationList().get(index);
                    UMLAttribute paramFromOperationFromList = umlOperation.getParametersOfOperationList().get(index);
                    // different type -> continue
                    if (!paramFromOpForAdding.getType().equals(paramFromOperationFromList.getType())) {
                        error = false;
                        break;
                    }
                }

                // same name and same parameters - error
                if (error) {
                    return false;
                }
            }

            // added operation differs at least in one parameter against other operations,
            // or is whole the same as one of others-
            // then adding will fail
            return umlOperationsList.add(operation);
        }
    }

    /**
     * deleting an operation
     * 
     * @param name name of operation for deletion
     */
    public void deleteOperation(String name) {
        UMLOperation umlOperation = findOperation(name);

        // check whether the attribute with entered name exists and remove it
        if (umlOperation == null) {
            return;
        } else {
            this.umlOperationsList.remove(umlOperation);
        }
    }

    /**
     * find and get operation according its name
     * helper method for deleteOperation()
     * 
     * @param name operation name
     * @return UMLOperation or null if not found
     */
    private UMLOperation findOperation(String name) {
        for (Iterator<UMLOperation> itr = umlOperationsList.iterator(); itr.hasNext();) {
            UMLOperation umlOperation = itr.next();

            // if attribute has been find with desired name
            if (umlOperation.getName() == name) {
                return umlOperation;
            }
        }

        // operation hasn't been found
        return null;
    }

    /**
     * method for get list of all operations
     * 
     * @return unmodifiable list of all operations
     */
    public List<UMLOperation> getUmlOperationList() {
        return Collections.unmodifiableList(this.umlOperationsList);
    }
}
