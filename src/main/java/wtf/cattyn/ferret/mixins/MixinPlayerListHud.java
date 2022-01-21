package wtf.cattyn.ferret.mixins;

import net.minecraft.client.gui.hud.PlayerListHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.core.Ferret;

import java.util.List;

@Mixin(PlayerListHud.class)
public class MixinPlayerListHud {

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/List;subList(II)Ljava/util/List;"))
    public <E> List<E> subList(List<E> list, int fromIndex, int toIndex) {
        Module et = Ferret.getDefault().getModuleManager().get("ExtraTab");
        return list.subList(fromIndex, et.isToggled() ? list.size() : toIndex);
    }

}
