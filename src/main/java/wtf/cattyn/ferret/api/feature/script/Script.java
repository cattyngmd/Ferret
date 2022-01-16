package wtf.cattyn.ferret.api.feature.script;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaValue;
import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.api.feature.option.Option;
import wtf.cattyn.ferret.api.feature.option.impl.BooleanOption;
import wtf.cattyn.ferret.api.feature.option.impl.NumberOption;
import wtf.cattyn.ferret.api.feature.script.lua.LuaCallback;
import wtf.cattyn.ferret.api.feature.script.lua.classes.ModuleLua;
import wtf.cattyn.ferret.api.feature.script.lua.functions.ColorFunction;
import wtf.cattyn.ferret.api.feature.script.lua.functions.TextOfFunction;
import wtf.cattyn.ferret.api.feature.script.lua.functions.Vec2dFunction;
import wtf.cattyn.ferret.api.feature.script.lua.functions.Vec3dFunction;
import wtf.cattyn.ferret.api.feature.script.lua.utils.LuaGlobals;
import wtf.cattyn.ferret.api.feature.script.lua.utils.LuaRenderer;
import wtf.cattyn.ferret.api.manager.impl.ConfigManager;
import wtf.cattyn.ferret.common.impl.trait.Json;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;
import wtf.cattyn.ferret.core.Ferret;

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

public class Script extends Feature.ToggleableFeature implements  Json<Script> {

    private transient String script;
    private Path path;
    private transient final List<LuaCallback> callbacks = new ArrayList<>();
    private transient final List<ModuleLua> modules = new ArrayList<>();

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
        ferret().getMappingManager().getFieldCache().clear();
        ferret().getMappingManager().getMethodCache().clear();
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("lua");

        try {
            engine.put("mc", MinecraftClient.getInstance());
            engine.put("this", this);
            engine.put("textOf", new TextOfFunction());
            engine.put("vec2d", new Vec2dFunction());
            engine.put("vec3d", new Vec3dFunction());
            engine.put("color", new ColorFunction());
            engine.put("client", Ferret.getDefault());
            engine.put("renderer", LuaRenderer.getDefault());
            engine.put("globals", LuaGlobals.getDefault());
            engine.put("Module", ModuleLua.getLua());

            engine.put("BooleanBuilder", new BooleanOption.LuaBuilder());
            engine.put("NumberBuilder", new NumberOption.LuaBuilder());

            engine.eval(script);
            engine.eval("main()");
        } catch (ScriptException e) {
            ChatUtil.sendMessage(e.getMessage());
        }
    }

    public void unload(boolean remove) {
        if (remove) {
            ferret().getScripts().remove(this);
            ferret().getMappingManager().getFieldCache().clear();
            ferret().getMappingManager().getMethodCache().clear();
        }
        for(ModuleLua lua : modules) {
            Option.getOptions().removeIf(option -> option.getFeature().equals(lua) || option.getFeature().equals(this));
        }
        Ferret.getDefault().getModuleManager().removeAll(modules);
        modules.clear();
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

    public LuaCallback registerCallback(String name, LuaClosure luaFunction) {
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

    public void addModule(ModuleLua lua) {
        modules.add(lua);
        Ferret.getDefault().getModuleManager().add(lua);
    }

    public List<LuaCallback> getCallbacks() {
        return callbacks;
    }

    public Path getPath() {
        return path;
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
