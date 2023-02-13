package wtf.cattyn.ferret.mixins.ducks;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin( MinecraftClient.class )
public interface DuckMinecraft {

    @Accessor("currentFps") static int getCurrentFps() {
        return 0;
    }

}
