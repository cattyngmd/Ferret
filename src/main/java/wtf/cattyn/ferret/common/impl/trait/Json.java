package wtf.cattyn.ferret.common.impl.trait;

import com.google.gson.JsonObject;

public interface Json<T> {

    JsonObject toJson();

    default JsonObject toJson(JsonObject object) {
        return toJson();
    }

    T fromJson(JsonObject object);

}
