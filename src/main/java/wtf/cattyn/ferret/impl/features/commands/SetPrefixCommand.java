package wtf.cattyn.ferret.impl.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;
import wtf.cattyn.ferret.api.feature.command.Command;
import wtf.cattyn.ferret.api.manager.impl.CommandManager;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;

public class SetPrefixCommand extends Command {

    public SetPrefixCommand() {
        super("setprefix", "", "setp");
    }

    @Override public void exec(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            // обезьяны из mojang не читают некоторые символы как String, приходится добавлять ""
            ChatUtil.sendMessage("Current prefix: " + Formatting.AQUA + CommandManager.getPrefix() + Formatting.WHITE
                    + "\nChange prefix: " + Formatting.AQUA + CommandManager.getPrefix() + "setprefix \"<new prefix>\"");
            ChatUtil.sendMessage("Example: " + Formatting.AQUA + CommandManager.getPrefix() + "setprefix \"^\"");
            return 1;
        }).then(
                argument("newPrefix", StringArgumentType.string())
                        .executes(context -> {
                            String prefix = StringArgumentType.getString(context, "newPrefix");
                            CommandManager.setPrefix(prefix);
                            ChatUtil.sendMessage("Changed command prefix to " + Formatting.AQUA + prefix);
                            return 1;
                        })
        );
    }

}
