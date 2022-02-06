package wtf.cattyn.ferret.api.feature.script.lua.utils;

import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaValue;
import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.api.manager.impl.ScriptManager;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;

public class LuaUtils {

    LuaUtils() {
    }

    public static void safeCall(@Nullable Feature.ToggleableFeature script, @Nullable LuaClosure closure, @Nullable LuaValue... values) {
        if (closure == null) return;
        try {
            if (values == null) closure.invoke();
            else closure.invoke(values);
        } catch (Exception e) {
            if (MinecraftClient.getInstance().inGameHud != null) {
                ChatUtil.sendMessage(
                        (script != null ? script.getName( ) + " -> " : "" ) + e.getMessage()
                );
            }
            e.printStackTrace();
            if (script != null && !ScriptManager.strict) script.setToggled(false);
        }
    }

    public static void safeCall(LuaClosure closure, LuaValue... values) {
        safeCall(null, closure, values);
    }

}
