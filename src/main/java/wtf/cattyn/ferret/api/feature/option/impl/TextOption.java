package wtf.cattyn.ferret.api.feature.option.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.api.feature.option.Option;

import java.util.function.Predicate;

public class TextOption extends Option<String> {

    public TextOption(Feature feature, String name, String desc, String value, Predicate<String> visibility) {
        super(feature, name, desc, value, visibility);
    }

    @Override public void setValue(String value) {
        this.value = value;
    }

    @Override public void setStringValue(String value) {
        this.value = value;
    }

    @Override public String getValue() {
        return value;
    }

    @Override public boolean is(String type) {
        return type.equalsIgnoreCase("text");
    }

    @Override public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("value", value);
        return object;
    }

    @Override public JsonObject toJson(JsonObject object) {
        object.addProperty(getName(), value);
        return object;
    }

    @Override public Option<String> fromJson(JsonObject object) {
        JsonElement element = object.get(getName());
        if(element.isJsonNull()) return this;
        this.value = element.getAsString();
        return this;
    }

    public static final class Builder extends OptionBuilder<TextOption.Builder, String, TextOption> {

        public Builder(String value) {
            super(value);
        }

        @Override public TextOption build(Feature feature) {
            if(validate()) throw new NullPointerException();
            TextOption o = new TextOption(feature, name, description, value, visibility);
            Option.getOptions().add(o);
            return o;
        }

    }

    public static final class LuaBuilder extends OneArgFunction {

        @Override public LuaValue call(LuaValue arg) {
            return CoerceJavaToLua.coerce(new TextOption.Builder(arg.tojstring()));
        }

    }

}
