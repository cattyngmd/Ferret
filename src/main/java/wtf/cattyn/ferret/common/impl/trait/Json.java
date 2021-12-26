package wtf.cattyn.ferret.common.impl.trait;

import com.google.gson.JsonObject;

public interface Json<T> {

    JsonObject toJson();

    T fromJson(JsonObject object);

}
