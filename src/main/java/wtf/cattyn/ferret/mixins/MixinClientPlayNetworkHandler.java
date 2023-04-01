package wtf.cattyn.ferret.mixins;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.cattyn.ferret.api.manager.impl.CommandManager;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void sendChatMessageHook(String content, CallbackInfo ci) {
        if(content.startsWith(CommandManager.getPrefix())) {
            try {
                CommandManager.DISPATCHER.execute(CommandManager.DISPATCHER.parse(content.substring(CommandManager.getPrefix().length()), CommandManager.COMMAND_SOURCE));
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            } finally {
                ci.cancel();
            }
        }
    }
}
