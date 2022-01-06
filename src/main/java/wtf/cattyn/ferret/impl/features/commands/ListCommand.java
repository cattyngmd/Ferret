package wtf.cattyn.ferret.impl.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import wtf.cattyn.ferret.api.feature.command.Command;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;

import java.util.List;

public class ListCommand extends Command {

    public ListCommand() {
        super("list", "", "l");
    }

    @Override public void exec(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            sendModules(ferret().getModuleManager().stream().filter(Module::isToggled).toList(), true);
            sendModules(ferret().getModuleManager().stream().filter(module -> !module.isToggled()).toList(), false);
            return 1;
        });
    }

    public static void sendModules(List<Module> list, boolean enabled) {
        StringBuffer sb = new StringBuffer((enabled ?  "Enabled" : "Disabled") + " modules: ");
        list.forEach(module -> sb.append(module.getName()).append(", "));
        if (sb.toString().contains(",")) sb.delete(sb.length() - 2, sb.length());
        ChatUtil.sendMessage(sb.toString());
    }

}
