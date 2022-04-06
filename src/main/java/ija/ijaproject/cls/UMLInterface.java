package ija.ijaproject.cls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class UMLInterface extends Element{

    /**
     * list of operations of class
     * */
    protected List<UMLOperation> umlOperationsList = new ArrayList<UMLOperation>();

    /**
     * constructor
     * Creating an instance of class representing class in UML diagram
     * @param name name of class
     */
    public UMLInterface(String name){
        super(name);
    }

    /**
     * adding an operation (which is parameter) of this class
     * @param operation parameter represent the whole operation
     * @return true or false according successfullness of this method
     * */
    public boolean addOperation(UMLOperation operation){
        if (umlOperationsList.contains(operation)) {
            //list already contains the attribute
            return false;
        } else {
            return umlOperationsList.add(operation);
        }
    }

    /**
     * deleting an operation
     * @param name name of operation for deletion
     * */
    public void deleteOperation(String name){
        UMLOperation umlOperation = findOperation(name);

        //check whether the attribute with entered name exists and remove it
        if (umlOperation == null){
            return;
        } else {
            this.umlOperationsList.remove(umlOperation);
        }
    }

    /**
     * find and get operation according its name
     * helper method for deleteOperation()
     * @param name operation name
     * @return UMLOperation or null if not found
     * */
    private UMLOperation findOperation(String name) {
        for (Iterator<UMLOperation> itr = umlOperationsList.iterator(); itr.hasNext(); ) {
            UMLOperation umlOperation = itr.next();

            //if attribute has been find with desired name
            if (umlOperation.getName() == name) {
                return umlOperation;
            }
        }

        //operation hasn't been found
        return null;
    }

    /**
     * method for get list of all operations
     * @return unmodifiable list of all operations
     * */
    public List<UMLOperation> getUmlOperationList(){
        return Collections.unmodifiableList(this.umlOperationsList);
    }
}
