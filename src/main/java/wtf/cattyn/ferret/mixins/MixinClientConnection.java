package wtf.cattyn.ferret.mixins;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.cattyn.ferret.core.Ferret;
import wtf.cattyn.ferret.impl.events.PacketEvent;

import java.util.concurrent.Future;

@Mixin( ClientConnection.class )
public class MixinClientConnection {

    @Shadow private Channel channel;
    @Shadow @Final private NetworkSide side;

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    public void channelRead0(ChannelHandlerContext chc, Packet<?> packet, CallbackInfo ci) {
        if (this.channel.isOpen() && packet != null) {
            try {
                PacketEvent.Receive event = new PacketEvent.Receive(packet);
                Ferret.EVENT_BUS.post(event);
                if (event.isCancelled())
                    ci.cancel();
            } catch (Exception e) {
            }
        }
    }

    @Inject(method = "sendImmediately", at = @At("HEAD"), cancellable = true)
    private void sendImmediately(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
        if (this.side != NetworkSide.CLIENTBOUND) return;
        try {
            PacketEvent.Send event = new PacketEvent.Send(packet);
            Ferret.EVENT_BUS.post(event);
            if (event.isCancelled()) ci.cancel();
        } catch (Exception e) {
        }
    }


}
