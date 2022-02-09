package wtf.cattyn.ferret.api.feature.option;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.common.impl.Pair;
import wtf.cattyn.ferret.common.impl.trait.Json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class Option<T> extends Feature implements Json<Option<T>> {

    private static final ArrayList<Option<?>> options = new ArrayList<>();

    protected T value;
    private transient final Option<Boolean> parent;
    private transient final List<Option<Boolean>> parents = new ArrayList<>();
    private transient final Feature feature;
    private transient final Predicate<Option<T>> visibility;

    public Option(Feature feature, String name, String desc, T value, Option<Boolean> parent, Predicate<Option<T>> visibility) {
        super(name, desc);
        this.value = value;
        this.feature = feature;
        this.parent = parent;
        this.visibility = visibility;
    }

    public abstract void setValue(T value);

    public abstract void setStringValue(String value);

    public Predicate<Option<T>> getVisibility() {
        return visibility;
    }

    public boolean isVisible() {
        return visibility.test(this);
    }

    public Option<Boolean> getParent() {
        return parent;
    }

    public abstract T getValue();

    public abstract boolean is(String type);

    public Feature getFeature() {
        return feature;
    }

    @Override public String toString() {
        return "Option{" +
                "value=" + value +
                '}';
    }

    public static List<Option<?>> getOptions() {
        return options;
    }

    public static List<Option<?>> getForTarget(Feature target) {
        if(target == null) return Collections.emptyList();
        return getOptions().stream().filter(o -> o.getFeature().equals(target)).collect(Collectors.toList());
    }

    @Override abstract public Pair<String, JsonElement> toJson();

    @Override abstract public Option<T> fromJson(JsonObject object);

    protected abstract static class OptionBuilder<B, T, O> {

        protected String name = null, description = "";
        protected T value;
        protected Predicate<Option<T>> visibility;
        protected Option<Boolean> parent;

        protected OptionBuilder(T value) {
            this.value = value;
        }

        public boolean validate() {
            return name == null;
        }

        public B name(String name) {
            this.name = name;
            return ( B ) this;
        }

        public B description(String description) {
            this.description = description;
            return ( B ) this;
        }

        public B visible(Predicate<Option<T>> visibility) {
            this.visibility = visibility;
            return ( B ) this;
        }

        public B parent(Option<Boolean> parent) {
            this.parent = parent;
            return ( B ) this;
        }

        public abstract O build(Feature feature);

    }

}
