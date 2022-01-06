package wtf.cattyn.ferret.api.manager.impl;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;
import wtf.cattyn.ferret.api.feature.command.Command;
import wtf.cattyn.ferret.api.manager.Manager;
import wtf.cattyn.ferret.impl.features.commands.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CommandManager extends ArrayList<Command> implements Manager<CommandManager> {

    private static String prefix = "$";
    public static final CommandSource COMMAND_SOURCE = new FerretCommandSource(mc);
    public static final CommandDispatcher<CommandSource> DISPATCHER = new CommandDispatcher<>();

    @Override public CommandManager load() {
        addAll(List.of(
                new ToggleCommand(),
                new ModuleCommand(),
                new LuaCommand(),
                new ListCommand(),
                new SetPrefixCommand()
        ));
        return this;
    }

    @Override public CommandManager unload() {
        clear();
        return this;
    }

    @Override public boolean addAll(Collection<? extends Command> c) {
        for(Command command : c) {
            for (String name : command.getAlias()) {
                LiteralArgumentBuilder<CommandSource> argumentBuilder = LiteralArgumentBuilder.literal(name);
                command.exec(argumentBuilder);
                DISPATCHER.register(argumentBuilder);
            }
        }
        return super.addAll(c);
    }

    public static String getPrefix() {
        return prefix;
    }

    public static void setPrefix(String prefix) {
        CommandManager.prefix = prefix;
    }

    private final static class FerretCommandSource extends ClientCommandSource {
        public FerretCommandSource(MinecraftClient client) {
            super(null, client);
        }
    }

}
