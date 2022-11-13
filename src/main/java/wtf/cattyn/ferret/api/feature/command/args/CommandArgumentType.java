package wtf.cattyn.ferret.api.feature.command.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.Text;
import wtf.cattyn.ferret.api.feature.command.Command;
import wtf.cattyn.ferret.common.Globals;

import java.util.concurrent.CompletableFuture;

public class CommandArgumentType implements ArgumentType<Command>, Globals {

    public static Command getCommand(CommandContext<?> context, String name) {
        return context.getArgument(name, Command.class);
    }

    public static CommandArgumentType command() {
        return new CommandArgumentType();
    }

    @Override public Command parse(StringReader reader) throws CommandSyntaxException {
        Command cmd = ferret().getCommandManager().get(reader.readString());
        if (cmd == null) throw new DynamicCommandExceptionType(o ->
                Text.of(o + " doesn't exist")).create(reader.readString());
        return cmd;
    }

    @Override public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        ferret().getCommandManager().forEach(cmd -> builder.suggest(cmd.getName()));
        return builder.buildFuture();
    }

}
