package wtf.cattyn.ferret.impl.events;

import net.minecraft.network.packet.Packet;
import wtf.cattyn.ferret.api.event.Event;
import wtf.cattyn.ferret.common.impl.util.PacketUtil;
import wtf.cattyn.ferret.core.Ferret;

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
