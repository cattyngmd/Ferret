package wtf.cattyn.ferret.impl.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import wtf.cattyn.ferret.api.feature.command.Command;
import wtf.cattyn.ferret.core.Ferret;

public class SaveConfigCommand extends Command {

    public SaveConfigCommand() {
        super("save", "", "saveconfig");
    }

    @Override public void exec(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(c -> {
            try {
                Ferret.getDefault().getConfigManager().start();
            } catch (Exception ignored) { }
            return 0;
        });
    }

}
