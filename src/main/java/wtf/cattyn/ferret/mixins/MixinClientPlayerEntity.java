package wtf.cattyn.ferret.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.cattyn.ferret.common.Globals;
import wtf.cattyn.ferret.core.Ferret;
import wtf.cattyn.ferret.impl.events.PlayerMoveEvent;
import wtf.cattyn.ferret.impl.events.TickEvent;

@Mixin( ClientPlayerEntity.class )
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {

    @Shadow protected abstract void autoJump(float dx, float dz);

    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo info) {
        if (MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().world != null)
            Ferret.EVENT_BUS.post(new TickEvent());
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    private void move(MovementType type, Vec3d velocity, CallbackInfo info) {
        PlayerMoveEvent event = new PlayerMoveEvent(velocity, type);
        Ferret.EVENT_BUS.post(event);
        if(event.isCancelled()) {
            info.cancel();
        } else if(!type.equals(event.getType()) || !velocity.equals(event.getVelocity())) {
            double x = this.getX();
            double y = this.getZ();
            super.move(event.getType(), event.getVelocity());
            this.autoJump((float) (this.getX() - x), (float) (this.getZ() - y));
            info.cancel();
        }
    }

}
