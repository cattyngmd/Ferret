package wtf.cattyn.ferret.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.cattyn.ferret.core.Ferret;

@Mixin( MinecraftClient.class )
public class MixinMinecraft {

    @Inject(method = "stop", at = @At ("HEAD"))
    public void stop(CallbackInfo info) {
        Ferret.getDefault().getConfigManager().start();
    }

}
