package wtf.cattyn.ferret.api.feature.script.lua.functions;

import net.minecraft.text.Text;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import wtf.cattyn.ferret.common.impl.trait.Nameable;

public class TextOfFunction extends OneArgFunction implements Nameable {

    @Override public LuaValue call(LuaValue arg) {
        return userdataOf(Text.of(arg.tojstring()));
    }


    @Override public String getName() {
        return "textOf";
    }

}
