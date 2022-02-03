package wtf.cattyn.ferret.api.feature.script.lua.functions;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import wtf.cattyn.ferret.common.impl.util.StopWatch;

public class StopWatchFunction extends ZeroArgFunction {

    @Override public LuaValue call() {
        return CoerceJavaToLua.coerce(new StopWatch());
    }

}
