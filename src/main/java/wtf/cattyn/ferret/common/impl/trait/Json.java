package wtf.cattyn.ferret.common.impl.trait;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import wtf.cattyn.ferret.common.impl.Pair;

public interface Json<T> {

    Pair<String, JsonElement> toJson();

    T fromJson(JsonObject object);

}
