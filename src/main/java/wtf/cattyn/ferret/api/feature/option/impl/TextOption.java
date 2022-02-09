package wtf.cattyn.ferret.api.feature.option.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.api.feature.option.Option;
import wtf.cattyn.ferret.common.impl.Pair;

import java.util.function.Predicate;

public class TextOption extends Option<String> {

    public TextOption(Feature feature, String name, String desc, String value, Option<Boolean> parent, Predicate<Option<String>> visibility) {
        super(feature, name, desc, value, parent, visibility);
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

    @Override public Pair<String, JsonElement> toJson() {
        return new Pair<>(getName(), new JsonPrimitive(value));
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
            TextOption o = new TextOption(feature, name, description, value, parent, visibility);
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
