package wtf.cattyn.ferret.api.feature.option.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

    @Override public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("value", value);
        return object;
    }

    @Override public Option<String> fromJson(JsonObject object) {
        JsonElement element = object.get("value");
        if(element.isJsonNull()) return this;
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

        @Override public ComboOption build(Feature feature) {
            if(validate()) throw new NullPointerException();
            ComboOption o = new ComboOption(feature, name, description, value, visibility, combo);
            Option.getOptions().add(o);
            return o;
        }

    }

}
