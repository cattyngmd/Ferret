package wtf.cattyn.ferret.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.cattyn.ferret.api.manager.impl.RotationManager;
import wtf.cattyn.ferret.common.Globals;
import wtf.cattyn.ferret.core.Ferret;
import wtf.cattyn.ferret.impl.events.PlayerMoveEvent;
import wtf.cattyn.ferret.impl.events.TickEvent;
import wtf.cattyn.ferret.mixins.ducks.DuckClientPlayerEntity;

@Mixin( ClientPlayerEntity.class )
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity implements Globals{

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

    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    private void rotations(CallbackInfo info) {
        if (rotations().isValid()) {
            info.cancel();
            boolean mode;
            boolean bl = this.isSprinting();
            if (bl != (( DuckClientPlayerEntity )mc.player).lastSprinting()) {
                ClientCommandC2SPacket.Mode mode2 = bl ? ClientCommandC2SPacket.Mode.START_SPRINTING : ClientCommandC2SPacket.Mode.STOP_SPRINTING;
                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(this, mode2));
                ((DuckClientPlayerEntity)mc.player).lastSprinting(bl);
            }
            if ((mode = this.isSneaking()) != ((DuckClientPlayerEntity)mc.player).lastSneaking()) {
                ClientCommandC2SPacket.Mode mode2 = mode ? ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY : ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY;
                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(this, mode2));
                ((DuckClientPlayerEntity)mc.player).lastSneaking(mode);
            }
            if (mc.player == mc.cameraEntity) {
                boolean bl3;
                double mode2 = this.getX() - ((DuckClientPlayerEntity)mc.player).lastX();
                double d = this.getY() - ((DuckClientPlayerEntity)mc.player).lastBaseY();
                double e = this.getZ() - ((DuckClientPlayerEntity)mc.player).lastZ();
                double f = rotations().getYaw() - ((DuckClientPlayerEntity)mc.player).lastYaw();
                double g = rotations().getPitch() - ((DuckClientPlayerEntity)mc.player).lastPitch();
                ((DuckClientPlayerEntity)mc.player).ticksSinceLastPositionPacketSent(((DuckClientPlayerEntity)mc.player).ticksSinceLastPositionPacketSent() + 1);
                boolean bl2 = mode2 * mode2 + d * d + e * e > 9.0E-4 || ((DuckClientPlayerEntity)mc.player).ticksSinceLastPositionPacketSent() >= 20;
                boolean bl4 = bl3 = f != 0.0 || g != 0.0;
                if (this.hasVehicle()) {
                    Vec3d vec3d = this.getVelocity();
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(vec3d.x, -999.0, vec3d.z, rotations().getYaw(), rotations().getPitch(), this.onGround));
                    bl2 = false;
                } else if (bl2 && bl3) {
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(this.getX(), this.getY(), this.getZ(), rotations().getYaw(), rotations().getPitch(), this.onGround));
                } else if (bl2) {
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(this.getX(), this.getY(), this.getZ(), this.onGround));
                } else if (bl3) {
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(rotations().getYaw(), rotations().getPitch(), this.onGround));
                } else if (((DuckClientPlayerEntity)mc.player).lastOnGround() != this.onGround) {
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(this.onGround));
                }
                if (bl2) {
                    ((DuckClientPlayerEntity)mc.player).lastX(this.getX());
                    ((DuckClientPlayerEntity)mc.player).lastBaseY(this.getY());
                    ((DuckClientPlayerEntity)mc.player).lastZ(this.getZ());
                    ((DuckClientPlayerEntity)mc.player).ticksSinceLastPositionPacketSent(0);
                }
                if (bl3) {
                    ((DuckClientPlayerEntity)mc.player).lastYaw(rotations().getYaw());
                    ((DuckClientPlayerEntity)mc.player).lastPitch(rotations().getPitch());
                }
                ((DuckClientPlayerEntity)mc.player).lastOnGround(this.onGround);
                ((DuckClientPlayerEntity)mc.player).autoJumpEnabled(mc.options.autoJump);
            }
            rotations().setValid(false);
        }
    }

    private RotationManager rotations() {
        return Ferret.getDefault().getRotationManager();
    }

}
