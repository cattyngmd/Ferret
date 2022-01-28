package wtf.cattyn.ferret.api.feature.command.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import wtf.cattyn.ferret.common.Globals;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class PlayerArgumentType implements ArgumentType<String>, Globals {

    @Override public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    @Override public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if(mc.getNetworkHandler() != null) {
            Stream<String> stream = mc.getNetworkHandler().getPlayerList().stream().map(p -> p.getProfile().getName());
            return CommandSource.suggestMatching(stream, builder);
        }

        return ArgumentType.super.listSuggestions(context, builder);
    }

    public static PlayerArgumentType player() {
        return new PlayerArgumentType();
    }

}
