package wtf.cattyn.ferret.impl.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import wtf.cattyn.ferret.api.feature.command.Command;

public class SendCoordsCommand extends Command {

    public SendCoordsCommand() {
        super("sendcoords", "Sends your coords to the other player using server's DM system", "sc");
    }

    @Override
    public void exec(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                argument("name", StringArgumentType.string())
                        .executes(context -> {
                            mc.player.sendChatMessage("/msg " + StringArgumentType.getString(context, "name") +
                                    " x: " + mc.player.getBlockX() +
                                    ", y: " + mc.player.getBlockY() +
                                    ", z: " + mc.player.getBlockZ());
                            return 1;
                        })
        );
    }
}
