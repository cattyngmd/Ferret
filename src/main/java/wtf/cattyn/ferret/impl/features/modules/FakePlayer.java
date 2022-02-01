package wtf.cattyn.ferret.impl.features.modules;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import wtf.cattyn.ferret.api.feature.module.Module;

import java.util.UUID;

public class FakePlayer extends Module {
    private OtherClientPlayerEntity fakePlayer;

    public FakePlayer() {
        super("FakePlayer", "Summons a fake player entity", Category.MISC);
    }

    @Override public void onEnable() {
        if (mc.world == null) {
            disable();
            return;
        }
        fakePlayer = new OtherClientPlayerEntity(mc.world, new GameProfile(UUID.randomUUID(), "FakePlayer"));
        fakePlayer.copyFrom(mc.player);
        mc.world.addEntity(-100, fakePlayer);
    }

    @Override public void onDisable() {
        if (fakePlayer == null) return;
        mc.world.removeEntity(fakePlayer.getId(), Entity.RemovalReason.DISCARDED);
    }
}
