package wtf.cattyn.ferret.api.manager.impl;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import wtf.cattyn.ferret.api.manager.Manager;

public final class RotationManager implements Manager<RotationManager> {

    private float yaw, pitch;
    private boolean valid;

    public float getPitch() { return pitch; }

    public float getYaw() { return yaw; }

    public void setPitch(float pitch) {
        this.yaw = mc.player.getYaw();
        this.pitch = pitch;
        valid = true;
    }

    public void setYaw(float yaw) {
        this.pitch = mc.player.getPitch();
        this.yaw = yaw;
        valid = true;
    }

    public void set(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        valid = true;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    public void update() {
        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, mc.player.isOnGround()));
    }

    @Override public RotationManager load() {
        return this;
    }

    @Override public RotationManager unload() { return this; }

    public static float[] calcRotations(@NotNull BlockPos pos) {
        return calcRotations(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }

    public static float[] calcRotations(@NotNull Entity entity) {
        return calcRotations(entity.getBoundingBox().getCenter());
    }

    public static float[] calcRotations(@NotNull Vec3d vec) {
        return calcRotations(mc.player.getCameraPosVec(mc.getTickDelta()), vec);
    }

    public static float[] calcRotations(Vec3d from, Vec3d to) {
        final double difX = to.x - from.x,
            difY = (to.y - from.y) * -1.0F,
            difZ = to.z - from.z,
            dist = Math.sqrt(difX * difX + difZ * difZ);
        return new float[] { ( float ) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0f), ( float ) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist))) };
    }


}
