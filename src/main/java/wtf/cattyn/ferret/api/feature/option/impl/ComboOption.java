package wtf.cattyn.ferret.api.feature.option.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.api.feature.option.Option;

import java.util.List;
import java.util.function.Predicate;

public class ComboOption extends Option<String> {

    private final List<String> combo;

    public ComboOption(Feature feature, String name, String desc, String value, Predicate visibility, List<String> combo) {
        super(feature, name, desc, value, visibility);
        this.combo = combo;
    }

    public List<String> getCombo() {
        return combo;
    }

    @Override public void setValue(String value) {
        if(!combo.contains(value)) return;
        this.value = value;
    }

    @Override public void setStringValue(String value) {
        setValue(value);
    }

    @Override public String getValue() {
        return value;
    }

    @Override public boolean is(String type) {
        return type.equalsIgnoreCase("combo");
    }

    public void increase() {
        if(combo.indexOf(value) + 1 >= combo.size()) setValue(combo.get(0));
        else setValue(combo.get(combo.indexOf(value) + 1));
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
        if(element.isJsonNull() || !combo.contains(element.getAsString())) return this;
        this.value = element.getAsString();
        return this;
    }

    public static final class Builder extends OptionBuilder<ComboOption.Builder, String, ComboOption> {

        private List<String> combo;

        public Builder(String value) {
            super(value);
        }

        public Builder setCombo(List<String> combo) {
            this.combo = combo;
            return this;
        }

        public Builder setCombo(String... combo) {
            this.combo = List.of(combo);
            return this;
        }

        @Override public ComboOption build(Feature feature) {
            if(validate()) throw new NullPointerException();
            ComboOption o = new ComboOption(feature, name, description, value, visibility, combo);
            Option.getOptions().add(o);
            return o;
        }

    }

    public static final class LuaBuilder extends OneArgFunction {

        @Override public LuaValue call(LuaValue arg) {
            return CoerceJavaToLua.coerce(new ComboOption.Builder(arg.tojstring()));
        }

    }

}
