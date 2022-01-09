package wtf.cattyn.ferret.impl.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;
import wtf.cattyn.ferret.api.feature.command.Command;
import wtf.cattyn.ferret.api.feature.command.args.CommandArgumentType;
import wtf.cattyn.ferret.api.feature.command.args.ModuleArgumentType;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.api.manager.impl.CommandManager;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Sends the description of every command", "h");
    }

    @Override
    public void exec(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                literal("command")
                        .executes(context -> {
                            ferret().getCommandManager().forEach(this::sendHelpMsg);
                            return 1;
                        })
                        .then(
                                argument("cmd", CommandArgumentType.command())
                                        .executes(context -> {
                                            sendHelpMsg(CommandArgumentType.getCommand(context, "cmd"));
                                            return 1;
                                        })
                        )
        ).then(
                literal("module")
                        .executes(context -> {
                            ferret().getModuleManager().forEach(this::sendHelpMsg);
                            return 1;
                        })
                        .then(
                                argument("m", ModuleArgumentType.module())
                                        .executes(context -> {
                                            sendHelpMsg(ModuleArgumentType.getModule(context, "m"));
                                            return 1;
                                        })
                        )
        );
    }

    void sendHelpMsg(Command cmd) {
        ChatUtil.sendMessage(Formatting.AQUA + CommandManager.getPrefix() + cmd.getName() + Formatting.WHITE + " - " + cmd.getDesc());
    }

    void sendHelpMsg(Module m) {
        ChatUtil.sendMessage(Formatting.AQUA + m.getName() + Formatting.WHITE + " - " + m.getDesc());
    }
}
