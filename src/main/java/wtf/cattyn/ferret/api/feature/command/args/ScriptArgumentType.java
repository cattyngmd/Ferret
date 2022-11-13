package wtf.cattyn.ferret.api.feature.command.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.Text;
import wtf.cattyn.ferret.api.feature.script.Script;
import wtf.cattyn.ferret.common.Globals;

import java.util.concurrent.CompletableFuture;

public class ScriptArgumentType implements ArgumentType<Script>, Globals {

    @Override public Script parse(StringReader reader) throws CommandSyntaxException {
        Script script = ferret().getScripts().get(reader.readString());
        if(script == null) throw  new DynamicCommandExceptionType(o ->
                Text.of(o + " doesn't exists")).create(reader.readString());
        return script;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        ferret().getScripts().forEach(script -> builder.suggest(script.getName()));
        return builder.buildFuture();
    }

    public static Script getScript(CommandContext<?> context, String arg) {
        return context.getArgument(arg, Script.class);
    }

    public static ScriptArgumentType script() {
        return new ScriptArgumentType();
    }

}
