package wtf.cattyn.ferret.impl.events;

import fuck.you.yarnparser.V1Parser;
import fuck.you.yarnparser.entry.ClassEntry;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import wtf.cattyn.ferret.api.event.Event;
import wtf.cattyn.ferret.common.impl.util.PacketUtil;
import wtf.cattyn.ferret.core.Ferret;

import java.lang.reflect.Method;
import java.util.HashMap;

public abstract class PacketEvent extends Event {

    private final Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public boolean is(String packet) {
        if(Ferret.getDefault().isRemapped()) {
            return PacketUtil.getCache().containsKey(packet) && PacketUtil.getCache().get(packet).getSimpleName().equalsIgnoreCase(getPacket().getClass().getSimpleName());
        }
        return getPacket().getClass().getSimpleName().equalsIgnoreCase(packet);
    }

    public static class Receive extends PacketEvent {
        public Receive(Packet<?> packet) {
            super(packet);
        }

        @Override public String getName() {
            return "packet_receive";
        }

    }

    public static class Send extends PacketEvent {
        public Send(Packet<?> packet) {
            super(packet);
        }

        @Override public String getName() {
            return "packet_send";
        }

    }

}
