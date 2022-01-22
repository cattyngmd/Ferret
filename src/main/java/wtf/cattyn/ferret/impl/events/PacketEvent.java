package wtf.cattyn.ferret.impl.events;

import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import wtf.cattyn.ferret.api.event.Event;
import wtf.cattyn.ferret.core.Ferret;

public abstract class PacketEvent extends Event {

    private final Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    //TODO add remapper for this method
    public boolean is(String packet) {
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
