package wtf.cattyn.ferret.api.feature.script.lua;

import net.minecraft.client.MinecraftClient;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import wtf.cattyn.ferret.api.feature.script.Script;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;

/**
 * @param name callbacks names
 *                        tick, hud, events
 */

public record LuaCallback(String name, LuaFunction callback, Script script) {

    public void run(LuaValue o) {
        if(script.isToggled()) {
            try {
                callback.call(o);
            } catch (Exception e) {
                if(MinecraftClient.getInstance().world != null)
                    ChatUtil.sendMessage(e.getMessage());
                else
                    System.err.println(e.getMessage());
                if(!LuaApi.strict) script.setToggled(false);
            }
        }
    }

}
