package ija.ijaproject.cls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class UMLOperation extends UMLAttribute{

    /**
     * list of all parameters of this operation
     * */
    private List<UMLAttribute> parametersOfOperationList = new ArrayList<UMLAttribute>();

    /**
     * constructor for creating operation just with name
     * @param name name of operation
     * */
    public UMLOperation(String name) {
        super(name);
    }

    /**
     * constructor for creating operation with name and its type
     * @param name name of operation
     * @param returnType return type of operation
     * */
    public UMLOperation(String name, String returnType) {
        super(name, returnType);
    }

    /**
     * adding parameter for this operation
     * @param param UMLAttribute parameter of this operation
     * @returns success of this operation (true/false)
     * */
    public boolean addOperationParameter(UMLAttribute param){
        for (Iterator<UMLAttribute> itr = parametersOfOperationList.iterator(); itr.hasNext(); ) {
            UMLAttribute umlAttribute = itr.next();

            //if attribute has been find with desired name then fail
            if (umlAttribute.getName() == param.getName()) {
                return false;
            }
        }

        //parameter hasn't been set yet
        this.parametersOfOperationList.add(param);
        return true;
    }

    /**
     * method for get all parameters of this method
     * @return list of params
     * */
    public List<UMLAttribute> getParametersOfOperationList(){
        return Collections.unmodifiableList(this.parametersOfOperationList);
    }

}
