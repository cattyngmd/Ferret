package wtf.cattyn.ferret.api.feature.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.CommandSource;
import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.common.Globals;

import java.util.Arrays;
import java.util.Locale;

public abstract class Command extends Feature implements Globals {

    private final String[] alias;

    public Command(String name, String desc, String... alias) {
        super(name, desc);
        this.alias = Arrays.copyOf(alias, alias.length + 1);
        this.alias[alias.length] = name.toLowerCase(Locale.ROOT);
    }

    public String[] getAlias() {
        return alias;
    }

    public abstract void exec(LiteralArgumentBuilder<CommandSource> builder);

    protected static <T> RequiredArgumentBuilder<CommandSource, T> argument(final String name, final ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

}
