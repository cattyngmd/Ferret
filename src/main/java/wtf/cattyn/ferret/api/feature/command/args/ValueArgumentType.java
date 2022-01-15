package wtf.cattyn.ferret.api.feature.command.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import wtf.cattyn.ferret.api.feature.option.Option;
import wtf.cattyn.ferret.api.feature.option.impl.BooleanOption;
import wtf.cattyn.ferret.api.feature.option.impl.ComboOption;
import wtf.cattyn.ferret.api.feature.option.impl.NumberOption;
import wtf.cattyn.ferret.common.Globals;

import java.util.concurrent.CompletableFuture;

//TODO add script support
public class ValueArgumentType implements ArgumentType<String>, Globals {

    private final String optionArg;
    private final String moduleArg;

    @Override public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Option<?> option;
        try {
            option = OptionArgumentType.getOption(context, moduleArg, optionArg);
        } catch (Exception e) {
            return ArgumentType.super.listSuggestions(context, builder);
        }

        if (option instanceof BooleanOption) {
            return BoolArgumentType.bool().listSuggestions(context, builder);
        } else if (option instanceof ComboOption enumOption) {

            for(String s : enumOption.getCombo()) {
                builder.suggest(s);
            }

            return builder.buildFuture();

        } else if (option instanceof NumberOption) {
            NumberOption numberOption = ( NumberOption ) option;
            Number val = (( NumberOption ) option).getValue();
            if (val instanceof Float || val instanceof Double) {
                return DoubleArgumentType.doubleArg(numberOption.getMin(), numberOption.getMax()).listSuggestions(context, builder);
            } else {
                return IntegerArgumentType.integer(( int ) numberOption.getMin(), ( int ) numberOption.getMax()).listSuggestions(context, builder);
            }
        }

        return ArgumentType.super.listSuggestions(context, builder);
    }

    public ValueArgumentType(String moduleArg, String optionArg) {
        this.optionArg = optionArg;
        this.moduleArg = moduleArg;
    }

    public static ValueArgumentType value(String moduleArg, String optionArg) {
        return new ValueArgumentType(moduleArg, optionArg);
    }

}
