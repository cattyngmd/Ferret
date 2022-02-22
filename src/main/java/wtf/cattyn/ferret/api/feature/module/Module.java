package wtf.cattyn.ferret.api.feature.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import org.luaj.vm2.LuaValue;
import org.lwjgl.glfw.GLFW;
import wtf.cattyn.ferret.api.event.FerretEventBus;
import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.api.feature.option.Option;
import wtf.cattyn.ferret.common.impl.Pair;
import wtf.cattyn.ferret.common.impl.trait.Json;
import wtf.cattyn.ferret.common.impl.trait.Toggleable;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;
import wtf.cattyn.ferret.core.Ferret;
import wtf.cattyn.ferret.impl.events.ModuleEvent;

public class Module extends Feature.ToggleableFeature implements Json<Module> {
    private boolean toggled;
    private transient final Category category;
    private int key = -1;

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

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    @Override public boolean isToggled() {
        return toggled;
    }

    @Override public void enable() {
        toggled = true;
        onToggle();
        onEnable();
        Ferret.EVENT_BUS.register(this);
        Ferret.EVENT_BUS.post(new ModuleEvent(this, true));
    }

    @Override public void disable() {
        onToggle();
        onDisable();
        Ferret.EVENT_BUS.unregister(this);
        toggled = false;
        Ferret.EVENT_BUS.post(new ModuleEvent(this, false));
    }

    public void onEnable() { }

    public void onDisable() { }

    public void onToggle() { }

    @Override public Pair<String, JsonElement> toJson() {
        JsonObject object = new JsonObject();

        object.addProperty("toggled", toggled);
        object.addProperty("key", key);

        JsonObject options = new JsonObject();
        Option.getForTarget(this).forEach(o -> options.add(o.getName(), o.toJson().value()));
        object.add("options", options);
        return new Pair<>(getName(), object);
    }

    @Override public Module fromJson(JsonObject object) {
        setToggled(object.get("toggled").getAsBoolean());
        setKey(object.get("key").getAsInt());
        Option.getForTarget(this).forEach(o -> {
            o.fromJson(object.get("options").getAsJsonObject());
        });
        return this;
    }

    public enum Category {
        COMBAT,
        VISUAL,
        MOVEMENT,
        PLAYER,
        MISC,
        CLIENT
    }

}
