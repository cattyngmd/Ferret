package wtf.cattyn.ferret.api.feature.script.lua.types;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;
import wtf.cattyn.ferret.api.feature.command.Command;
import wtf.cattyn.ferret.api.feature.command.CommandContainer;
import wtf.cattyn.ferret.api.feature.command.args.ModuleArgumentType;
import wtf.cattyn.ferret.api.feature.command.args.PlayerArgumentType;
import wtf.cattyn.ferret.api.feature.command.args.ScriptArgumentType;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.api.feature.script.Script;
import wtf.cattyn.ferret.api.feature.script.lua.table.LuaUtils;

import java.util.function.Supplier;

public class CommandLua extends Command {

    private static LuaValue value;
    private final Script script;
    private LuaClosure func;

    public CommandLua(String name, String desc, Script script, String[] alias) {
        super(name, desc, alias);
        this.script = script;
    }

    @Override public void exec(LiteralArgumentBuilder<CommandSource> builder) {
        LuaUtils.safeCall(func, CoerceJavaToLua.coerce(new CommandContainer(builder)));
    }

    public void body(LuaClosure func) {
        this.func = func;
        register();
    }

    public void register() {
        script.addCommand(this);
    }

    @Override public String toString() {
        return "CommandLua{" +
                "script=" + script +
                ", func=" + func +
                "} " + super.toString();
    }

    public static LuaValue getLua() {
        if (value == null) {
            LuaValue moduleLua = CoerceJavaToLua.coerce(CommandLua.class);
            LuaTable table = new LuaTable();
            table.set("new", new New());
            for(Argument argument : Argument.values()) {
                table.set(argument.name(), CoerceJavaToLua.coerce(argument));
            }
            table.set("__index", table);
            moduleLua.setmetatable(table);
            value = moduleLua;
        }
        return value;
    }

    static class New extends LibFunction {

        @Override public LuaValue call(LuaValue name, LuaValue description, LuaValue script) {
            if(!name.isstring() || !description.isstring()) throw new IllegalArgumentException("Invalid arguments.");
            return CoerceJavaToLua.coerce(
                    new CommandLua(name.tojstring(), description.tojstring(), ( Script ) CoerceLuaToJava.coerce(script, Script.class), null)
            );
        }

    }

    enum Argument {
        EMPTY(null, null),
        STRING(StringArgumentType::string, String.class),
        GREEDYSTRING(StringArgumentType::greedyString, String.class),
        BOOLEAN(BoolArgumentType::bool, Boolean.class),
        INTEGER(IntegerArgumentType::integer, Integer.class),
        DOUBLE(DoubleArgumentType::doubleArg, Double.class),
        FLOAT(FloatArgumentType::floatArg, Float.class),
        MODULE(ModuleArgumentType::module, Module.class),
        SCRIPT(ScriptArgumentType::script, Script.class),
        PLAYER(PlayerArgumentType::player, String.class);

        final Supplier<ArgumentType<?>> supplier;
        final Class<?> argumentClass;

        Argument(Supplier<ArgumentType<?>> supplier, Class<?> argumentClass) {
            this.supplier = supplier;
            this.argumentClass = argumentClass;
        }

        public Object get(CommandContext<?> context, String name) {
            return context.getArgument(name, argumentClass);
        }

        public CommandContainer createLiteral(String name) {
            return new CommandContainer(LiteralArgumentBuilder.literal(name));
        }

        public CommandContainer create(String name) {
            return new CommandContainer(RequiredArgumentBuilder.argument(name, supplier.get()));
        }

    }

}
