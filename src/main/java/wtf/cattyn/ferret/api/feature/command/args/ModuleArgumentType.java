package wtf.cattyn.ferret.api.feature.command.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.LiteralText;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.common.Globals;

import java.util.concurrent.CompletableFuture;

public class ModuleArgumentType implements ArgumentType<Module>, Globals {

    public static Module getModule(CommandContext<?> context, String name) {
        return context.getArgument(name, Module.class);
    }

    public static ModuleArgumentType module() {
        return new ModuleArgumentType();
    }

    @Override public Module parse(StringReader reader) throws CommandSyntaxException {
        Module module = ferret().getModuleManager().get(reader.readString());
        if(module == null) throw  new DynamicCommandExceptionType(o ->
                new LiteralText(o + " doesnt exist")).create(reader.readString());
        return module;
    }

    @Override public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        ferret().getModuleManager().forEach(m -> builder.suggest(m.getName()));
        return builder.buildFuture();
    }

}
