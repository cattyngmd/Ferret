package wtf.cattyn.ferret.mixins;

import net.minecraft.client.Keyboard;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.cattyn.ferret.common.impl.trait.Toggleable;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;
import wtf.cattyn.ferret.core.Ferret;
import wtf.cattyn.ferret.impl.features.commands.BindCommand;

@Mixin(Keyboard.class)
public class MixinKeyboard {
    @Inject(method = "onKey", at = @At(value = "INVOKE", target = "net/minecraft/client/util/InputUtil.isKeyPressed(JI)Z", ordinal = 5), cancellable = true)
    private void onKey(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        if (BindCommand.set) {
            BindCommand.module.setKey(key);
            ChatUtil.sendMessage("Changed bind for " + Formatting.AQUA + BindCommand.module.getName());
            BindCommand.set = false;
            BindCommand.module = null;
            ci.cancel();
        } else {
            Ferret.getDefault().getModuleManager().stream().filter(module -> module.getKey() == key).forEach(Toggleable::toggle);
        }
    }

}
