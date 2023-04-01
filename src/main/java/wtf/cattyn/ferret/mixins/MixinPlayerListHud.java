package wtf.cattyn.ferret.mixins;

import net.minecraft.client.gui.hud.PlayerListHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.core.Ferret;

@Mixin(PlayerListHud.class)
public class MixinPlayerListHud {

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;limit(J)Ljava/util/stream/Stream;"))
    private long renderHook(long maxSize) {
        Module et = Ferret.getDefault().getModuleManager().get("ExtraTab");
        return et.isToggled() ? Long.MAX_VALUE : maxSize;
    }

}
