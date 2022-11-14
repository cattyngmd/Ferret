package wtf.cattyn.ferret.mixins;

import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;
import java.util.function.Supplier;

@Mixin(SimpleOption.class)
public class MixinSimpleOption<T> {

    @Redirect(method = "setValue", at = @At(value = "INVOKE", target = "Ljava/util/Optional;orElseGet(Ljava/util/function/Supplier;)Ljava/lang/Object;"))
    private T setValueHook(Optional instance, Supplier<? extends T> supplier, T value) {
        return value;
    }

}
