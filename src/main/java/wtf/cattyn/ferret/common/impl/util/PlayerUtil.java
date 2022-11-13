package wtf.cattyn.ferret.common.impl.util;

import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import wtf.cattyn.ferret.common.Globals;

public class PlayerUtil implements Globals {

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

    public static Integer findInHotbar(Item item) {
        Integer itemSlot = null;
        for (int slot = 0; slot < 9; slot++) {
            if (mc.player.getInventory().getStack(slot).getItem() == item) {
                itemSlot = slot;
                break;
            }
        }
        return itemSlot;
    }

    public static void placeBlock(BlockPos pos, int slot) {
        Vec3d vec = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
        int oldSlot = mc.player.getInventory().selectedSlot;

        mc.player.getInventory().selectedSlot = slot;
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(
                vec, Direction.DOWN, pos, true
        ));
        mc.player.getInventory().selectedSlot = oldSlot;
    }

}
