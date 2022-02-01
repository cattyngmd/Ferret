package wtf.cattyn.ferret.api.manager.impl;

import org.luaj.vm2.LuaValue;
import wtf.cattyn.ferret.api.feature.script.lua.LuaCallback;
import wtf.cattyn.ferret.api.feature.script.Script;
import wtf.cattyn.ferret.api.manager.Manager;

import java.util.ArrayList;

public final class ScriptManager extends ArrayList<Script> implements Manager<ScriptManager> {

    public static boolean strict = false;

    @Override public ScriptManager load() {
        return this;
    }

    @Override public ScriptManager unload() {
        clear();
        return this;
    }

    public void runCallback(String callback, LuaValue o) {
        for(Script script : this) {
            if(!script.isToggled() || !script.isLoaded()) continue;
            script.invoke(callback, o);
        }
    }

    public void runCallback(String callback) {
        runCallback(callback, LuaValue.NIL);
    }

    public Script get(String name) {
        return stream().filter(script -> script.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
