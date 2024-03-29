package wtf.cattyn.ferret.api.feature.script.lua.table;

import com.google.common.collect.Lists;
import fuck.you.yarnparser.entry.ClassEntry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;
import wtf.cattyn.ferret.common.Globals;
import wtf.cattyn.ferret.mixins.ducks.DuckMinecraft;

import java.util.Collections;
import java.util.List;

public class LuaGlobals implements Globals {

    private static LuaGlobals instance;

    LuaGlobals() {
    }

    public int getFps() {
        return DuckMinecraft.getCurrentFps();
    }

    public double getFrametime() {
        return 1.0 / DuckMinecraft.getCurrentFps();
    }

    public Vec3d getPosition() {
        if(mc.player == null) return Vec3d.ZERO;
        return mc.player.getPos();
    }

    public long currentTime() {
        return System.currentTimeMillis();
    }

    public String getServer() {
        if(mc.player == null || mc.getCurrentServerEntry() == null) return "none";
        return mc.getCurrentServerEntry().address;
    }

    public String getUsername() {
        return mc.getSession().getUsername();
    }

    public int getPing() {
        if (mc.getNetworkHandler() == null || mc.player == null) return 0;
        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
        if (playerListEntry != null) return playerListEntry.getLatency();
        return 0;
    }

    public void setTickMultiplier(float tick) {
        ferret().getTickManager().setMultiplier(tick);
    }

    public float getTickMultiplier() {
        return ferret().getTickManager().getMultiplier();
    }

    public Block getBlock(double x, double y, double z) {
        if(mc.world == null) return Blocks.AIR;
        return mc.world.getBlockState(BlockPos.ofFloored(x, y, z)).getBlock();
    }

    public List<Entity> getEntities() {
        if (mc.world == null) return Collections.emptyList();
        return Lists.newArrayList(mc.world.getEntities());
    }

    public int[] intArray(LuaTable table) {
        int[] ints = new int[table.length()];
        for (int j = 1; j <= table.length(); j++) {
            ints[j - 1] = table.get(j).toint();
        }
        return ints;
    }

    public float[] floatArray(LuaTable table) {
        float[] ints = new float[table.length()];
        for (int j = 1; j <= table.length(); j++) {
            ints[j - 1] = table.get(j).tofloat();
        }
        return ints;
    }

    public double[] doubleArray(LuaTable table) {
        double[] ints = new double[table.length()];
        for (int j = 1; j <= table.length(); j++) {
            ints[j - 1] = table.get(j).todouble();
        }
        return ints;
    }

    public char[] charArray(LuaTable table) {
        char[] ints = new char[table.length()];
        for (int j = 1; j <= table.length(); j++) {
            ints[j - 1] = table.get(j).tochar();
        }
        return ints;
    }

    public boolean instanceOf(Object o, String clazz) {
        Class<?> oClazz = o.getClass();
        if (ferret().isRemapped()) {
            ClassEntry classEntry = ferret().getMappingManager().remapClass(clazz.replace(".", "/"), "NAMED", false);
            if (classEntry != null) clazz = classEntry.intermediary.replace("/", ".");
        }
        while (oClazz != null) {
            if (oClazz.getName().equals(clazz)) return true;
            oClazz = oClazz.getSuperclass();
        }
        return false;
    }

    public <T> T[] customArray(LuaTable table, Class<T> type) {
        Object[] ints = new Object[table.length()];
        for (int j = 1; j <= table.length(); j++) {
            ints[j - 1] = CoerceLuaToJava.coerce(table.get(j), type);
        }
        return ( T[] ) ints;
    }

    public static LuaGlobals getDefault() {
        if(instance == null) instance = new LuaGlobals();
        return instance;
    }

}
