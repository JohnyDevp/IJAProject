package ija.ijaproject.cls;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * 
 * UMLClassInterfaceTemplateDesirializer class.
 * 
 *
 * @author xzimol04, xholan11
 * @version 1.1
 */
public class UMLClassInterfaceTemplateDesirializer implements JsonDeserializer<UMLClassInterfaceTemplate> {
    /** {@inheritDoc} */
    public UMLClassInterfaceTemplate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        boolean isInterface = Boolean.parseBoolean(json.getAsJsonObject().get("isInterface").toString());
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        Gson gson = builder.create();
        if (isInterface) {
            return gson.fromJson(json, UMLInterface.class);
        }
        return gson.fromJson(json, UMLClass.class);
    }
}
