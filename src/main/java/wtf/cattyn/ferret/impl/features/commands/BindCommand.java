package wtf.cattyn.ferret.impl.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;
import wtf.cattyn.ferret.api.feature.command.Command;
import wtf.cattyn.ferret.api.feature.command.args.ModuleArgumentType;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;

public class BindCommand extends Command {

    public BindCommand() {
        super("bind", "", "bd");
    }

    public static Module module;

    @Override
    public void exec(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                argument("module", ModuleArgumentType.module())
                        .then(
                                literal("set")
                                        .executes(context -> {
                                            module = ModuleArgumentType.getModule(context, "module");
                                            ChatUtil.sendMessage("Press a key");
                                            return 1;
                                        })
                        ).then(
                                literal("clear")
                                        .executes(context -> {
                                            Module m = ModuleArgumentType.getModule(context, "module");
                                            m.setKey(-1481058891);
                                            ChatUtil.sendMessage("Cleared bind for " + Formatting.AQUA + m.getName());
                                            return 1;
                                        })
                        )
        );
    }
}
