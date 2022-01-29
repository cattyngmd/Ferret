package wtf.cattyn.ferret.api.feature.script.lua.utils;

import net.minecraft.util.math.Vec3d;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;
import wtf.cattyn.ferret.common.Globals;
import wtf.cattyn.ferret.mixins.ducks.DuckMinecraft;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LuaGlobals implements Globals {

    private static LuaGlobals instance;

    public LuaGlobals() {
    }

    public int getFps() {
        return ((DuckMinecraft) mc).getCurrentFps();
    }

    public double getFrametime() {
        return 1.0 / ((DuckMinecraft) mc).getCurrentFps();
    }

    public Vec3d getPosition() {
        if(mc.player == null) return Vec3d.ZERO;
        return mc.player.getPos();
    }

    public long currentTime() {
        return System.currentTimeMillis();
    }

    public String getServer() {
        if(mc.player == null || mc.player.getServer() == null) return "none";
        return mc.player.getServer().getServerIp();
    }

    public String getUsername() {
        return mc.getSession().getUsername();
    }

    public void setTickMultiplier(float tick) {
        ferret().getTickManager().setMultiplier(tick);
    }

    public float getTickMultiplier() {
        return ferret().getTickManager().getMultiplier();
    }

    public List<Object> sortList(List<Object> list, LuaClosure closure) {
        for (int j = 2; j < list.size(); j++) {

            double key = ( double ) CoerceLuaToJava.coerce(closure.call(CoerceJavaToLua.coerce(list.get(j))), Double.class);
            int i = j - 1;

            while (i > 0 && ( double ) CoerceLuaToJava.coerce(closure.call(CoerceJavaToLua.coerce(list.get(j))), Double.class) > key) {
                list.set(i + 1,  list.get(i));
                i--;
            }
            list.set(i + 1, key);
        }
        return list;
    }

    public static LuaGlobals getDefault() {
        if(instance == null) instance = new LuaGlobals();
        return instance;
    }


}
