package ija.ijaproject.cls;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;

public class UMLClassInterfaceTemplateDesirializer implements JsonDeserializer<UMLClassInterfaceTemplate> {
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