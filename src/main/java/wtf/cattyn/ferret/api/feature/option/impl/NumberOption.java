package wtf.cattyn.ferret.api.feature.option.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.api.feature.option.Option;

import java.util.function.Predicate;

public class NumberOption extends Option<Number> {

    private transient final double max, min;

    private NumberOption(Feature feature, String name, String desc, Number value, double max, double min, Predicate<Number> visibility) {
        super(feature, name, desc, value, visibility);
        this.max = max;
        this.min = min;
    }

    @Override public void setValue(Number value) {
        this.value = value;
    }

    @Override public void setStringValue(String value) {
        setValue(Double.parseDouble(value));
    }

    @Override public Number getValue() {
        return value;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    @Override public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("value", value);
        return object;
    }

    @Override public Option<Number> fromJson(JsonObject object) {
        JsonElement element = object.get("value");
        if(element.isJsonNull()) return this;
        this.value = element.getAsDouble();
        return this;
    }

    public static final class Builder extends OptionBuilder<Builder, Number, NumberOption> {

        private double max, min;

        public Builder(Number value) {
            super(value);
        }

        public Builder setBounds(double min, double max) {
            this.max = max;
            this.min = min;
            return this;
        }

        @Override public NumberOption build(Feature feature) {
            if(validate()) throw new NullPointerException();
            NumberOption o = new NumberOption(feature, name, description, value, max, min, visibility);
            Option.getOptions().add(o);
            return o;
        }

    }

    public static final class LuaBuilder extends OneArgFunction {

        @Override public LuaValue call(LuaValue arg) {
            return userdataOf(new NumberOption.Builder(arg.todouble()));
        }

    }

}
