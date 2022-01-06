package wtf.cattyn.ferret.api.feature.script.lua.functions;

import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import wtf.cattyn.ferret.common.impl.Vec2d;
import wtf.cattyn.ferret.common.impl.trait.Nameable;

public class Vec2dFunction extends TwoArgFunction implements Nameable {

    @Override public LuaValue call(LuaValue arg1, LuaValue arg2) {
        return userdataOf(new Vec2d(arg1.todouble(), arg2.todouble()));
    }

    @Override public String getName() {
        return "vec2d";
    }

}
