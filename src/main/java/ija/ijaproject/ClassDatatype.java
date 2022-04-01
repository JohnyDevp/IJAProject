package ija.ijaproject;

import java.util.ArrayList;

public class ClassDatatype {
    public String name;
    public String header;
    public ArrayList<String> methods;
    public ArrayList<String> attributes;

    public ClassDatatype() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public ArrayList<String> getMethods() {
        return methods;
    }

    public void setMethods(ArrayList<String> methods) {
        this.methods = methods;
    }

    public ArrayList<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<String> attributes) {
        this.attributes = attributes;
    }

}
