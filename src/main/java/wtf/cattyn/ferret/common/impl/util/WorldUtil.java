package wtf.cattyn.ferret.common.impl.util;

import net.minecraft.util.math.MathHelper;
import wtf.cattyn.ferret.common.Globals;

public class WorldUtil implements Globals {

    public static Float[] lookAt(double x, double y, double z) {
        float preYaw = mc.player.getYaw();
        float prePitch = mc.player.getPitch();

        double diffX = x - mc.player.getX();
        double diffY = y - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        double diffZ = z - mc.player.getZ();
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

        mc.player.setYaw(mc.player.getYaw() + MathHelper.wrapDegrees(yaw - mc.player.getYaw()));
        mc.player.setPitch(mc.player.getPitch() + MathHelper.wrapDegrees(pitch - mc.player.getPitch()));
        // возвращает старые значения для облегчения ротейта
        return new Float[]{preYaw, prePitch};
    }

}
