package wtf.cattyn.ferret.api.feature.option.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.api.feature.option.Option;

import java.util.function.Predicate;

public class BooleanOption extends Option<Boolean> {

    private BooleanOption(Feature feature, String name, String desc, Boolean value, Predicate<Boolean> visibility) {
        super(feature, name, desc, value, visibility);
    }

    @Override public void setValue(Boolean value) {
        this.value = value;
    }

    @Override public void setStringValue(String value) {
        this.value = Boolean.parseBoolean(value);
    }

    @Override public Boolean getValue() {
        return value;
    }

    @Override public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("value", value);
        return object;
    }

    @Override public Option<Boolean> fromJson(JsonObject object) {
        JsonElement element = object.get("value");
        if(element.isJsonNull()) return this;
        this.value = element.getAsBoolean();
        return this;
    }

    public static final class Builder extends OptionBuilder<Builder, Boolean, BooleanOption> {

        public Builder(Boolean value) {
            super(value);
        }

        @Override public BooleanOption build(Feature feature) {
            if(validate()) throw new NullPointerException();
            BooleanOption o = new BooleanOption(feature, name, description, value, visibility);
            Option.getOptions().add(o);
            return o;
        }

    }

}
