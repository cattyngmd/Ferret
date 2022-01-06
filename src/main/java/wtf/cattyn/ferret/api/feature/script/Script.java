package wtf.cattyn.ferret.api.feature.script;

import net.minecraft.client.MinecraftClient;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.api.feature.option.Option;
import wtf.cattyn.ferret.api.feature.option.impl.BooleanOption;
import wtf.cattyn.ferret.api.feature.option.impl.NumberOption;
import wtf.cattyn.ferret.api.feature.script.lua.LuaApi;
import wtf.cattyn.ferret.api.feature.script.lua.LuaCallback;
import wtf.cattyn.ferret.api.feature.script.lua.functions.ColorFunction;
import wtf.cattyn.ferret.api.feature.script.lua.functions.TextOfFunction;
import wtf.cattyn.ferret.api.feature.script.lua.functions.Vec2dFunction;
import wtf.cattyn.ferret.api.feature.script.lua.functions.Vec3dFunction;
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

public class Script extends Feature implements Toggleable {

    private boolean active = true;
    private final Path path;
    private String script;
    private final List<LuaCallback> callbacks = new ArrayList<>();

    public Script(String name, String desc, Path path) {
        super(name, desc);
        this.path = path;
        try {
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
        if(remove) ferret().getScripts().remove(this);
        Option.getForTarget(this).clear();
        callbacks.clear();
    }

    public void reload() {
        try {
            this.script = new String(Files.readAllBytes(path));
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
        callbacks.stream().filter(c -> c.name().equalsIgnoreCase(name)).forEach(c -> c.callback().call(arg));
    }

    public void invoke(String name) {
        invoke(name, LuaValue.NONE);
    }

    public List<LuaCallback> getCallbacks() {
        return callbacks;
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

}
