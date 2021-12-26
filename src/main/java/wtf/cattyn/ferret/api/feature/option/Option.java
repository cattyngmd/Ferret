package wtf.cattyn.ferret.api.feature.option;

import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.common.impl.trait.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class Option<T> extends Feature.SerializableFeature implements Json<Option<T>> {

    private static final List<Option<?>> options = new ArrayList<>();

    protected T value;
    private transient final Feature feature;
    private transient final Predicate<T> visibility;

    public Option(Feature feature, String name, String desc, T value, Predicate<T> visibility) {
        super(name, desc);
        this.value = value;
        this.feature = feature;
        this.visibility = visibility;
    }

    public abstract void setValue(T value);

    public Predicate<T> getVisibility() {
        return visibility;
    }

    public boolean isVisible() {
        return visibility.test(value);
    }

    public abstract T getValue();

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
        return getOptions().stream().filter(o -> o.getFeature().equals(target)).collect(Collectors.toList());
    }

    protected abstract static class OptionBuilder<B, T, O> {
        protected String name = null, description = "";
        protected T value;
        protected Predicate<T> visibility;

        protected OptionBuilder(T value) {
            this.value = value;
        }

        public boolean validate() {
            return name == null;
        }

        public B name(String name) {
            this.name = name;
            return (B) this;
        }

        public B description(String description) {
            this.description = description;
            return (B) this;
        }

        public B value(T value) {
            this.value = value;
            return (B) this;
        }

        public B visible(Predicate<T> visibility) {
            this.visibility = visibility;
            return (B) this;
        }

        public abstract O build(Feature feature);
    }

}
