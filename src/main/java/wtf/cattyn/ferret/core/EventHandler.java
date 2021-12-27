package wtf.cattyn.ferret.core;

import com.google.common.eventbus.Subscribe;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import wtf.cattyn.ferret.api.feature.command.Command;
import wtf.cattyn.ferret.api.manager.impl.CommandManager;
import wtf.cattyn.ferret.common.Globals;
import wtf.cattyn.ferret.impl.events.PacketEvent;

public final class EventHandler implements Globals {

    @Subscribe public void onPacketSend(PacketEvent.Send event) {
        if(event.getPacket() instanceof ChatMessageC2SPacket) {
            ChatMessageC2SPacket packet = ( ChatMessageC2SPacket ) event.getPacket();
            if(packet.getChatMessage().startsWith(CommandManager.getPrefix())) {
                try {
                    CommandManager.DISPATCHER.execute(CommandManager.DISPATCHER.parse(packet.getChatMessage().substring(CommandManager.getPrefix().length()), CommandManager.COMMAND_SOURCE));
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                } finally {
                    event.cancel();
                }
            }
        }
    }

}
