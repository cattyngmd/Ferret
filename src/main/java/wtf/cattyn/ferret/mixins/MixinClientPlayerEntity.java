package wtf.cattyn.ferret.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.cattyn.ferret.common.Globals;
import wtf.cattyn.ferret.core.Ferret;
import wtf.cattyn.ferret.impl.events.TickEvent;

@Mixin( ClientPlayerEntity.class )
public class MixinClientPlayerEntity {

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo info) {
        if (MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().world != null)
            Ferret.EVENT_BUS.post(new TickEvent());
    }


}
