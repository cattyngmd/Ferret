package wtf.cattyn.ferret.api.feature.script.lua;

import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaValue;
import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.api.feature.script.lua.table.LuaUtils;

/**
 * @param name callbacks names
 *                        tick, hud, events
 */

public record LuaCallback(String name, LuaClosure callback, Feature.ToggleableFeature script) {

    public void run(LuaValue o) {
        if (script.isToggled()) {
            LuaUtils.safeCall(script, callback, o);
        }
    }

}
