package wtf.cattyn.ferret.api.feature.module;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.api.feature.option.Option;
import wtf.cattyn.ferret.common.impl.trait.Json;
import wtf.cattyn.ferret.common.impl.trait.Toggleable;
import wtf.cattyn.ferret.core.Ferret;

public class Module extends Feature implements Toggleable, Json<Module> {

    @Expose private boolean toggled;
    private transient final Category category;

    public Module(String name, Category category) {
        this(name, "No Description provided!", category);
    }

    public Module(String name, String desc, Category category) {
        super(name, desc);
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    @Override public boolean isToggled() {
        return toggled;
    }

    @Override public void enable() {
        toggled = true;
        onToggle();
        onEnable();
        Ferret.EVENT_BUS.register(this);
    }

    @Override public void disable() {
        Ferret.EVENT_BUS.unregister(this);
        onToggle();
        onDisable();
        toggled = false;
    }

    public void onEnable() { }
    public void onDisable() { }
    public void onToggle() { }

    @Override public JsonObject toJson() {
        JsonObject object = JsonParser.parseString(gson.toJson(this)).getAsJsonObject();
        JsonObject options = new JsonObject();
        Option.getForTarget(this).forEach(o -> options.add(o.getName(), o.toJson()));
        object.add("options", options);
        return object;
    }

    @Override public Module fromJson(JsonObject object) {
        setToggled(object.get("toggled").getAsBoolean());
        Option.getForTarget(this).forEach(o -> {
            o.fromJson(object.get("options").getAsJsonObject().get(o.getName()).getAsJsonObject());
        });
        return this;
    }

    public enum Category {
        COMBAT,
        VISUAL,
        PLAYER,
        MISC,
        CLIENT
    }

}
