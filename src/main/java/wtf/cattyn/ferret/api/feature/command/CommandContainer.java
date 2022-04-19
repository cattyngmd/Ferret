package wtf.cattyn.ferret.api.feature.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.CommandSource;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public record CommandContainer(ArgumentBuilder<CommandSource, ?> builder) {

    public CommandContainer executes(LuaValue closure) {
        builder.executes(context -> {
            closure.invoke(CoerceJavaToLua.coerce(context));
            return 1;
        });
        return this;
    }

    public CommandContainer next(CommandContainer container) {
        builder.then(container.builder());
        return this;
    }

    public CommandContainer suggest(LuaTable table) {
        if (builder instanceof RequiredArgumentBuilder) {
            (( RequiredArgumentBuilder<?, ?> ) builder).suggests((context, suggestions) -> {
                for (int j = 1; j <= table.length(); j++) {
                    if (table.get(j).isstring()) suggestions.suggest(table.get(j).tojstring());
                }
                return suggestions.buildFuture();
            });
        }
        return this;
    }

}
