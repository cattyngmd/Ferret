package wtf.cattyn.ferret.api.feature.module;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import net.minecraft.util.Formatting;
import org.luaj.vm2.LuaValue;
import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.api.feature.option.Option;
import wtf.cattyn.ferret.common.impl.trait.Json;
import wtf.cattyn.ferret.common.impl.trait.Toggleable;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;
import wtf.cattyn.ferret.core.Ferret;

public class Module extends Feature.ToggleableFeature implements Json<Module> {
    @Expose private boolean toggled;
    private transient final Category category;
    @Expose private int key = -1481058891;

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
        if (mc.world != null) ChatUtil.sendMessage(getName() + Formatting.GREEN + " enabled");
    }

    @Override public void disable() {
        onToggle();
        onDisable();
        Ferret.EVENT_BUS.unregister(this);
        toggled = false;
        if (mc.world != null) ChatUtil.sendMessage(getName() + Formatting.RED + " disabled");
    }

    public void onEnable() { }

    public void onDisable() { }

    public void onToggle() { }

    public void registerLuaBody(LuaValue luaValue) {

    }

    @Override public JsonObject toJson() {
        JsonObject object = JsonParser.parseString(gson.toJson(this)).getAsJsonObject();
        JsonObject options = new JsonObject();
        Option.getForTarget(this).forEach(o -> options.add(o.getName(), o.toJson()));
        object.add("options", options);
        return object;
    }

    @Override public Module fromJson(JsonObject object) {
        setToggled(object.get("toggled").getAsBoolean());
        setKey(object.get("key").getAsInt());
        Option.getForTarget(this).forEach(o -> {
            o.fromJson(object.get("options").getAsJsonObject().get(o.getName()).getAsJsonObject());
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
