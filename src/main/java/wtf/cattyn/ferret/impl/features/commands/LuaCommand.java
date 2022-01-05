package wtf.cattyn.ferret.impl.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;
import wtf.cattyn.ferret.api.feature.command.Command;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.nio.file.Files;
import java.nio.file.Path;

public class LuaCommand extends Command {

    Globals globals = JsePlatform.standardGlobals();

    public LuaCommand() {
        super("lua", "executes javascript scripts", "luaj");
    }

    @Override public void exec(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                literal("info")
                        .executes(context -> {
                            ChatUtil.sendMessage("TODO make info shit");
                            return 1;
                        })
        ).then(
                literal("file")
                        .then(
                                argument("filepath", StringArgumentType.greedyString())
                                        .executes(context -> {
                                            ScriptEngineManager factory = new ScriptEngineManager();
                                            ScriptEngine engine = factory.getEngineByName("lua");
                                            try {
                                                String lua = Files.readString(Path.of(StringArgumentType.getString(context, "filepath")));
                                                engine.eval(lua);
                                                engine.put("mc", MinecraftClient.getInstance());
                                                ChatUtil.sendMessage(engine.eval("return main()").toString());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            return 1;
                                        })
                        )
        ).then(
                literal("script")
                        .then(
                                argument("script", StringArgumentType.greedyString())
                                        .executes(context -> {
                                            ScriptEngineManager factory = new ScriptEngineManager();
                                            ScriptEngine engine = factory.getEngineByName("lua");
                                            try {
                                                engine.eval(StringArgumentType.getString(context, "script"));
                                                engine.put("mc", MinecraftClient.getInstance());
                                                ChatUtil.sendMessage(engine.eval("return main()").toString());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
//                                            LuaValue luaValue = globals.load(StringArgumentType.getString(context, "script")).call();
//
//                                            System.out.println(luaValue.get("main").invoke().toString());

                                            return 1;
                                        })
                        )
        ).executes(context -> 0);
    }
}
