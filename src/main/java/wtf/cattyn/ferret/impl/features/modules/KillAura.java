package wtf.cattyn.ferret.impl.features.modules;

import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.Hand;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.api.feature.option.impl.NumberOption;
import wtf.cattyn.ferret.impl.events.TickEvent;

public class KillAura extends Module {
    // TODO: more options
    NumberOption d = new NumberOption.Builder(4.5f).name("range").setBounds(1f, 10f).build(this);

    public KillAura() {
        super("KillAura", "Automatically attacks players", Category.COMBAT);
    }

    @Subscribe public void onTick(TickEvent e) {
        if (mc.world == null || mc.player.getAttackCooldownProgress(mc.getTickDelta()) != 1.0f) return;

        mc.world.getPlayers().stream()
                .filter(p -> p.isAlive() && mc.player.distanceTo(p) <= d.getValue().floatValue() && p != mc.player).forEach(p -> {
                    boolean s = mc.player.isSprinting();
                    if (s) mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));

                    mc.interactionManager.attackEntity(mc.player, p);
                    mc.player.swingHand(Hand.MAIN_HAND);

                    if (s) mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
                });
    }

}
