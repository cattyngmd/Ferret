package wtf.cattyn.ferret.api.manager.impl;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;
import wtf.cattyn.ferret.api.feature.command.Command;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.api.manager.Manager;
import wtf.cattyn.ferret.common.impl.trait.Enumerable;
import wtf.cattyn.ferret.impl.features.commands.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class CommandManager extends ArrayList<Command> implements Manager<CommandManager>, Enumerable<Command> {

    private static String prefix = "$";
    public static final CommandSource COMMAND_SOURCE = new FerretCommandSource(mc);
    public static final CommandDispatcher<CommandSource> DISPATCHER = new CommandDispatcher<>();

    @Override public CommandManager load() {
        addAll(List.of(
                new ToggleCommand(),
                new ModuleCommand(),
                new LuaCommand(),
                new ListCommand(),
                new SetPrefixCommand(),
                new BindCommand(),
                new HelpCommand(),
                new SendCoordsCommand(),
                new GetScriptCommand(),
                new SaveConfigCommand()
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

    @Override public boolean add(Command command) {
        LiteralArgumentBuilder<CommandSource> argumentBuilder = LiteralArgumentBuilder.literal(command.getName());
        command.exec(argumentBuilder);
        DISPATCHER.register(argumentBuilder);
        return super.add(command);
    }

    @Override public boolean removeAll(Collection<?> c) {
        for (Object o : c) {
            Command command = (Command) o;
            //I need better way to do this
            try {
                Field field = CommandNode.class.getDeclaredField("literals");
                if (!field.canAccess(DISPATCHER.getRoot())) {
                    field.setAccessible(true);
                }
                Map<String, LiteralCommandNode<?>> map = (( Map<String, LiteralCommandNode<?>> ) field.get(DISPATCHER.getRoot()));
                map.remove(command.getName());

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            DISPATCHER.getRoot().getChildren().remove(DISPATCHER.getRoot().getChild(command.getName()));
        }
        return super.removeAll(c);
    }

    @Override public boolean remove(Object o) {
        Command command = (Command) o;
        //I need better way to do this
        try {
            Field field = CommandNode.class.getDeclaredField("literals");
            if (!field.canAccess(DISPATCHER.getRoot())) {
                field.setAccessible(true);
            }
            Map<String, LiteralCommandNode<?>> map = (( Map<String, LiteralCommandNode<?>> ) field.get(DISPATCHER.getRoot()));
            map.remove(command.getName());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        DISPATCHER.getRoot().getChildren().remove(DISPATCHER.getRoot().getChild(command.getName()));
        return super.remove(o);
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

    public Command get(String name) {
        return stream().filter(cmd -> cmd.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
