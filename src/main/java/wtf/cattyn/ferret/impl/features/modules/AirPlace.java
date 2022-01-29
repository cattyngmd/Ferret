package wtf.cattyn.ferret.impl.features.modules;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.api.feature.option.impl.BooleanOption;
import wtf.cattyn.ferret.impl.events.TickEvent;
// TODO: добавить рендер mc.crosshairTarget
public class AirPlace extends Module {

    BooleanOption ns = new BooleanOption.Builder(true).name("normalSpeed").build(this);
    boolean pressed = false;

    public AirPlace() {
        super("AirPlace", "Replace air blocks (minecraft moment)", Category.MISC);
    }

    @Subscribe public void onTick(TickEvent e) {
        if (mc.options.keyUse.isPressed() && (!ns.getValue() || !pressed)) {
            mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, (BlockHitResult) mc.crosshairTarget));
            pressed = true;
        } else if (!mc.options.keyUse.isPressed()) pressed = false;
    }

}
