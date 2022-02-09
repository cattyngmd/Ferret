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

public class BooleanOption extends Option<Boolean> {

    private BooleanOption(Feature feature, String name, String desc, Boolean value, Option<Boolean> parent, Predicate<Option<Boolean>> visibility) {
        super(feature, name, desc, value, parent, visibility);
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

    @Override public boolean is(String type) {
        return type.equalsIgnoreCase("bool");
    }

    @Override public Pair<String, JsonElement> toJson() {
        return new Pair<>(getName(), new JsonPrimitive(value));
    }

    @Override public Option<Boolean> fromJson(JsonObject object) {
        JsonElement element = object.get(getName());
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
            BooleanOption o = new BooleanOption(feature, name, description, value, parent, visibility);
            Option.getOptions().add(o);
            return o;
        }

    }

    public static final class LuaBuilder extends OneArgFunction {

        @Override public LuaValue call(LuaValue arg) {
            return CoerceJavaToLua.coerce(new BooleanOption.Builder(arg.toboolean()));
        }

    }

}
