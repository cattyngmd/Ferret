package wtf.cattyn.ferret.impl.features.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;
import wtf.cattyn.ferret.api.feature.command.Command;
import wtf.cattyn.ferret.api.feature.command.args.ModuleArgumentType;
import wtf.cattyn.ferret.api.feature.command.args.OptionArgumentType;
import wtf.cattyn.ferret.api.feature.command.args.ValueArgumentType;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.api.feature.option.Option;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;
import wtf.cattyn.ferret.core.Ferret;

import java.util.Locale;
import java.util.stream.Collectors;

public class ModuleCommand extends Command {

    public ModuleCommand() {
        super("module", "Manages modules", "m");
    }

    @Override public void exec(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                argument("module", ModuleArgumentType.module())
                        .then(
                                literal("option")
                                        .then(
                                                argument("option", OptionArgumentType.option())
                                                        .then(
                                                                argument("value", ValueArgumentType.value("module", "option"))
                                                                        .executes(context -> {
                                                                            Option<?> option = OptionArgumentType.getOption(context, "module", "option");
                                                                            String rawValue = context.getArgument("value", String.class);
                                                                            try {
                                                                                option.setStringValue(rawValue);
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                                return 0;
                                                                            }
                                                                            ChatUtil.sendMessage(option.getName() + " set to " + rawValue.toLowerCase(Locale.ROOT));

                                                                            return 1;
                                                                        })
                                                        ).executes(context -> {
                                                            Option<?> option = OptionArgumentType.getOption(context, "module", "option");
                                                            ChatUtil.sendMessage(option.getName() + " is " + option.getValue());
                                                            return 1;
                                                        })
                                        ).executes(context -> {
                                            Module module = ModuleArgumentType.getModule(context, "module");
                                            String options = Option.getForTarget(module).stream().map(option -> String.format("%s[%s]", option.getName(), option.getValue())).collect(Collectors.joining(", "));
                                            ChatUtil.sendMessage("Options for " + module.getName() + ": " + options);
                                            return 1;
                                        })
                        ).then(
                                literal("state")
                                        .then(
                                                argument("statement", BoolArgumentType.bool())
                                                        .executes(context -> {
                                                            Module module = ModuleArgumentType.getModule(context, "module");
                                                            module.setToggled(
                                                                    BoolArgumentType.getBool(context, "statement")
                                                            );
                                                            return 1;
                                                        })
                                        )
                        )
                        .executes(context -> {
                            Module module = ModuleArgumentType.getModule(context, "module");
                            module.toggle();
                            return 1;
                        })
        ).executes(context -> {
            return 0;
        });
    }

}
