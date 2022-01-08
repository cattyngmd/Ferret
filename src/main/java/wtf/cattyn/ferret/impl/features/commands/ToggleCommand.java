package wtf.cattyn.ferret.impl.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;
import wtf.cattyn.ferret.api.feature.command.Command;
import wtf.cattyn.ferret.api.feature.command.args.ModuleArgumentType;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;

import static wtf.cattyn.ferret.api.feature.command.args.ModuleArgumentType.module;

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("Toggle", "Toggles module");
    }

    @Override public void exec(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                argument("module", module())
                        .executes(context -> {
                            Module module = ModuleArgumentType.getModule(context, "module");
                            module.toggle();
                            return 1;
                        })
        ).executes(context -> 0);
    }

}
