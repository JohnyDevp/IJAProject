package ija.ijaproject.cls;

import com.google.gson.*;

import java.lang.reflect.Type;

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
