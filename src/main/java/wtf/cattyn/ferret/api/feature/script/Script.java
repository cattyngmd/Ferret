package wtf.cattyn.ferret.api.feature.script;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.api.feature.option.Option;
import wtf.cattyn.ferret.api.feature.script.lua.LuaApi;
import wtf.cattyn.ferret.api.feature.script.lua.LuaCallback;
import wtf.cattyn.ferret.api.manager.impl.ConfigManager;
import wtf.cattyn.ferret.common.impl.trait.Json;
import wtf.cattyn.ferret.common.impl.trait.Toggleable;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cattyn
 * @since 06/1/22
 */

public class Script extends Feature implements Toggleable, Json<Script> {

    @Expose private boolean active = true;
    private transient String script;
    private Path path;
    private transient final List<LuaCallback> callbacks = new ArrayList<>();

    public Script(String name, String desc) {
        super(name, desc);
        try {
            this.path = Path.of(ConfigManager.SCRIPT_FOLDER.toString(), name);
            this.script = new String(Files.readAllBytes(path));
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("lua");

        try {
            LuaApi.modifyEngine(engine, this);

            engine.eval(script);
            engine.eval("main()");
        } catch (ScriptException e) {
            ChatUtil.sendMessage(e.getMessage());
        }
    }

    public void unload(boolean remove) {
        if (remove) ferret().getScripts().remove(this);
        Option.getForTarget(this).clear();
        callbacks.clear();
    }

    public void reload() {
        try {
            this.script = new String(Files.readAllBytes(Path.of(ConfigManager.SCRIPT_FOLDER.toString(), getName())));
            unload(false);
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LuaCallback registerCallback(String name, LuaFunction luaFunction) {
        LuaCallback callback = new LuaCallback(name, luaFunction, this);
        callbacks.add(callback);
        return callback;
    }

    public void invoke(String name, LuaValue arg) {
        if(callbacks == null) return;
        callbacks.stream().filter(c -> c.name().equalsIgnoreCase(name)).forEach(c -> c.run(arg));
    }

    public void invoke(String name) {
        invoke(name, LuaValue.NONE);
    }

    public List<LuaCallback> getCallbacks() {
        return callbacks;
    }

    public Path getPath() {
        return path;
    }

    @Override public boolean isToggled() {
        return active;
    }

    @Override public void enable() {
        active = true;
    }

    @Override public void disable() {
        active = false;
    }

    @Override public JsonObject toJson() {
        JsonObject object = JsonParser.parseString(gson.toJson(this)).getAsJsonObject();
        JsonObject options = new JsonObject();
        Option.getForTarget(this).forEach(o -> options.add(o.getName(), o.toJson()));
        object.add("options", options);
        return object;
    }

    @Override public Script fromJson(JsonObject object) {
        return this;
    }

}
