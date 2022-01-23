package wtf.cattyn.ferret.impl.features.modules;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.common.impl.util.PlayerUtil;
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

                PlayerUtil.placeBlock(pos, itemSlot);
            }
        }
    }

}
