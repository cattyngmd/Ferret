package wtf.cattyn.ferret.api.feature.script.lua;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import wtf.cattyn.ferret.api.feature.option.impl.BooleanOption;
import wtf.cattyn.ferret.api.feature.option.impl.NumberOption;
import wtf.cattyn.ferret.api.feature.script.Script;
import wtf.cattyn.ferret.api.feature.script.lua.functions.ColorFunction;
import wtf.cattyn.ferret.api.feature.script.lua.functions.TextOfFunction;
import wtf.cattyn.ferret.api.feature.script.lua.functions.Vec2dFunction;
import wtf.cattyn.ferret.api.feature.script.lua.functions.Vec3dFunction;
import wtf.cattyn.ferret.api.feature.script.lua.utils.LuaRenderer;
import wtf.cattyn.ferret.common.Globals;
import wtf.cattyn.ferret.core.Ferret;

import javax.script.ScriptEngine;

//TODO fix somehow luajava lib
public class LuaApi implements Globals {

    private static final String VERSION = "0.1";
    public static boolean strict = false;

    public static void modifyEngine(ScriptEngine engine, Script script) {
        engine.put("mc", MinecraftClient.getInstance());
        engine.put("this", script);
        engine.put("textOf", new TextOfFunction());
        engine.put("vec2d", new Vec2dFunction());
        engine.put("vec3d", new Vec3dFunction());
        engine.put("color", new ColorFunction());
        engine.put("client", Ferret.getDefault());
        engine.put("renderer", LuaRenderer.getDefault());

        //options
        engine.put("BooleanBuilder", new BooleanOption.LuaBuilder());
        engine.put("NumberBuilder", new NumberOption.LuaBuilder());
    }



}
