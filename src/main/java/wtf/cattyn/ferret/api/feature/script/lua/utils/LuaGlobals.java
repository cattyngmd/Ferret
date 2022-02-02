package wtf.cattyn.ferret.api.feature.script.lua.utils;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import wtf.cattyn.ferret.common.Globals;
import wtf.cattyn.ferret.mixins.ducks.DuckMinecraft;

import java.util.Collections;
import java.util.List;

public class LuaGlobals implements Globals {

    private static LuaGlobals instance;

    LuaGlobals() {
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
        if(mc.player == null || mc.getCurrentServerEntry() == null) return "none";
        return mc.getCurrentServerEntry().address;
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

    public Block getBlock(double x, double y, double z) {
        if(mc.world == null) return Blocks.AIR;
        return mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    public List<Entity> getEntities() {
        if (mc.world == null) return Collections.emptyList();
        return Lists.newArrayList(mc.world.getEntities());
    }

    public static LuaGlobals getDefault() {
        if(instance == null) instance = new LuaGlobals();
        return instance;
    }

}
