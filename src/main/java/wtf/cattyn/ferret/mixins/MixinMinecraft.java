package wtf.cattyn.ferret.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.cattyn.ferret.core.Ferret;
import wtf.cattyn.ferret.impl.features.modules.UnfocusedCPU;

@Mixin( MinecraftClient.class )
public class MixinMinecraft {
    @Shadow private boolean windowFocused;

    @Inject(method = "stop", at = @At ("HEAD"))
    public void stop(CallbackInfo info) {
        Ferret.getDefault().getConfigManager().start();
    }

    @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
    public void getFramerateLimit(CallbackInfoReturnable<Integer> cir) {
        UnfocusedCPU module = (UnfocusedCPU) Ferret.getDefault().getModuleManager().get("UnfocusedCPU");
        if (module.isToggled() && !this.windowFocused) cir.setReturnValue(module.limit.getValue().intValue());
    }
}
