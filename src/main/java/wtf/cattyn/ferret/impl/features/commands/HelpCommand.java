package wtf.cattyn.ferret.impl.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;
import wtf.cattyn.ferret.api.feature.command.Command;
import wtf.cattyn.ferret.api.feature.command.args.CommandArgumentType;
import wtf.cattyn.ferret.api.manager.impl.CommandManager;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Sends the description of every command", "h");
    }

    // TODO: add help for modules
    @Override
    public void exec(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            ferret().getCommandManager().forEach(this::sendHelpMsg);
            return 1;
        }).then(
                argument("cmd", CommandArgumentType.command())
                        .executes(context -> {
                            sendHelpMsg(CommandArgumentType.getCommand(context, "cmd"));
                            return 1;
                        })
        );
    }

    // TODO: add command syntax to the help message
    void sendHelpMsg(Command cmd) {
        ChatUtil.sendMessage(Formatting.AQUA + CommandManager.getPrefix() + cmd.getName() + Formatting.WHITE + " - " + cmd.getDesc());
    }
}
