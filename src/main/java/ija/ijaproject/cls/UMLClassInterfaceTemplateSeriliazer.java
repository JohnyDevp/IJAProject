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

public class UMLClassInterfaceTemplateSeriliazer implements JsonSerializer<UMLClassInterfaceTemplate> {
    public JsonElement serialize(UMLClassInterfaceTemplate src, Type typeOfSrc, JsonSerializationContext context) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        Gson gson = builder.create();

        JsonElement el = gson.toJsonTree(src, src.getClass());
        el.getAsJsonObject().addProperty("isInterface", src.getClass() == UMLInterface.class);
        return el;
    }
}
