package wtf.cattyn.ferret.api.feature.script.lua;

import net.minecraft.client.MinecraftClient;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaValue;
import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.api.feature.script.Script;
import wtf.cattyn.ferret.api.feature.script.lua.utils.LuaUtils;
import wtf.cattyn.ferret.api.manager.impl.ScriptManager;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;

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
