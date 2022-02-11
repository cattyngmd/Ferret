package wtf.cattyn.ferret.impl.features.modules;

import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.Hand;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.api.feature.option.impl.BooleanOption;
import wtf.cattyn.ferret.api.feature.option.impl.NumberOption;
import wtf.cattyn.ferret.api.manager.impl.RotationManager;
import wtf.cattyn.ferret.core.Ferret;
import wtf.cattyn.ferret.impl.events.TickEvent;

public class KillAura extends Module {

    NumberOption d = new NumberOption.Builder(4.5f).name("range").setBounds(1f, 10f).build(this);
    BooleanOption r = new BooleanOption.Builder(false).name("rotate").build(this);

    public KillAura() {
        super("KillAura", "Automatically attacks players", Category.COMBAT);
    }

    @Subscribe public void onTick(TickEvent e) {
        if (mc.world == null) return;

        var target = mc.world.getPlayers().stream().filter(p -> p.isAlive() && mc.player.distanceTo(p) <= d.getValue().floatValue() && p != mc.player).findFirst().orElse(null);
        if (target != null) {

            if (r.getValue()) {
                float[] rots = RotationManager.calcRotations(target);
                Ferret.getDefault().getRotationManager().set(rots[ 0 ], rots[ 1 ]);
            }

            if (mc.player.getAttackCooldownProgress(mc.getTickDelta()) != 1.0f) return;

            boolean s = mc.player.isSprinting();

            if (s)
                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));

            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);

            if (s)
                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
        }
    }

}
