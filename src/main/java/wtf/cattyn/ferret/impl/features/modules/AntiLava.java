package wtf.cattyn.ferret.impl.features.modules;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.impl.events.TickEvent;

public class AntiLava extends Module {

    public AntiLava() {
        super("AntiLava", "Replaces lava over and under you", Category.MISC);
    }

    @Subscribe public void onTick(TickEvent e) {
        if (mc.world == null) return;
        BlockPos playerPos = mc.player.getBlockPos();
        BlockPos[] poses = new BlockPos[]{playerPos.up(2), playerPos.down()};

        for (BlockPos pos : poses) {
            if (mc.world.getBlockState(pos).getBlock() == Blocks.LAVA) {
                Integer itemSlot = null;
                for (int slot = 0; slot < 9; slot++) {
                    if (mc.player.getInventory().getStack(slot).getItem() instanceof BlockItem) {
                        itemSlot = slot;
                        break;
                    }
                }
                if (itemSlot == null) return;

                Vec3d vec = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
                int oldSlot = mc.player.getInventory().selectedSlot;
                mc.player.getInventory().selectedSlot = itemSlot;
                mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(
                        vec, Direction.DOWN, pos, true
                ));
                mc.player.getInventory().selectedSlot = oldSlot;
            }
        }
    }

}
