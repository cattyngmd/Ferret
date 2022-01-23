package wtf.cattyn.ferret.common.impl;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.nio.file.Path;

public class PathConverter implements JsonDeserializer<Path>, JsonSerializer<Path> {

    @Override public Path deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Path.of(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(Path path, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(path.toString());
    }

}
