package ija.ijaproject.cls;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * 
 * UMLClassInterfaceTemplateSeriliazer class.
 * 
 *
 * @author xzimol04, xholan11
 * @version 1.1
 */
public class UMLClassInterfaceTemplateSeriliazer implements JsonSerializer<UMLClassInterfaceTemplate> {
    /** {@inheritDoc} */
    public JsonElement serialize(UMLClassInterfaceTemplate src, Type typeOfSrc, JsonSerializationContext context) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        Gson gson = builder.create();

        JsonElement el = gson.toJsonTree(src, src.getClass());
        el.getAsJsonObject().addProperty("isInterface", src.getClass() == UMLInterface.class);
        return el;
    }
}
