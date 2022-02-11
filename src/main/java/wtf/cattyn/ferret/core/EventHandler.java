package wtf.cattyn.ferret.core;

import com.google.common.eventbus.Subscribe;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.apache.logging.log4j.core.util.FileWatcher;
import wtf.cattyn.ferret.api.feature.command.Command;
import wtf.cattyn.ferret.api.manager.impl.CommandManager;
import wtf.cattyn.ferret.api.manager.impl.RotationManager;
import wtf.cattyn.ferret.common.Globals;
import wtf.cattyn.ferret.impl.events.PacketEvent;
import wtf.cattyn.ferret.impl.events.PlayerMotionUpdateEvent;
import wtf.cattyn.ferret.impl.events.TickEvent;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public final class EventHandler implements Globals {

    private final RotationManager rotations = Ferret.getDefault().getRotationManager();

    @Subscribe public void onTick(TickEvent event) {
        ferret().getScripts().runCallback("tick");
    }

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

    @Subscribe public void onPlayerMotionUpdatePost(PlayerMotionUpdateEvent.Post event) {
        if (rotations.isValid()) {
            rotations.update();
            rotations.setValid(false);
        }
    }

}
