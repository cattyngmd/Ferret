package wtf.cattyn.ferret.api.feature.script.lua;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import wtf.cattyn.ferret.api.feature.script.Script;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;

/**
 * @param name callbacks names
 *                        tick, command, hud
 */

public record LuaCallback(String name, LuaFunction callback, Script script) {

    public void run(LuaValue o) {
        if(script.isToggled()) {
            try {
                callback.call(o);
            } catch (Exception e) {
                ChatUtil.sendMessage(e.getMessage());
                if(!LuaApi.strict) script.setToggled(false);
            }
        }
    }

}
