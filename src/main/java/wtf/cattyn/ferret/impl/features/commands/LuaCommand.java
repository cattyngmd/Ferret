package wtf.cattyn.ferret.impl.features.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;
import wtf.cattyn.ferret.api.feature.command.Command;
import wtf.cattyn.ferret.api.feature.command.args.ScriptArgumentType;
import wtf.cattyn.ferret.api.feature.script.Script;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.util.concurrent.CompletableFuture;

public class LuaCommand extends Command {

    Globals globals = JsePlatform.standardGlobals();

    public LuaCommand() {
        super("lua", "Executes javascript scripts", "luaj");
    }

    @Override public void exec(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                literal("info")
                        .executes(context -> {
                            ChatUtil.sendMessage("TODO make info shit");
                            return 1;
                        })
        ).then(
                literal("load")
                        .then(
                                argument("name", StringArgumentType.greedyString())
                                        .executes(context -> {
                                            String name = StringArgumentType.getString(context, "name");
                                            ferret().getScripts().add(new Script(name, ""));

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
                                            return 1;
                                        })
                        )
        ).then(
                    literal("get")
                            .then(
                                    argument("script", ScriptArgumentType.script())
                                            .then(
                                                    literal("statement")
                                                            .then(
                                                                    argument("state", BoolArgumentType.bool())
                                                                        .executes(context -> {
                                                                            Script script = ScriptArgumentType.getScript(context, "script");
                                                                            script.setToggled(BoolArgumentType.getBool(context, "state"));
                                                                            if(script.isToggled()) {
                                                                                ChatUtil.sendMessage(script.getName() + " enabled");
                                                                            } else {
                                                                                ChatUtil.sendMessage(script.getName() + " disabled");
                                                                            }
                                                                            return 1;
                                                                        })
                                                            )
                                            ).then(
                                                    literal("action")
                                                            .then(
                                                                    argument("action",  StringArgumentType.string()).suggests(this::actions)
                                                                            .executes(context -> {
                                                                                Script script = ScriptArgumentType.getScript(context, "script");
                                                                                switch (StringArgumentType.getString(context, "action")) {
                                                                                    case "unload" -> script.unload(true);
                                                                                    case "reload" -> script.reload();
                                                                                }
                                                                                return 1;
                                                                            })
                                                            )
                                            )
                            )
                )
                .executes(context -> 0);
    }

    private CompletableFuture<Suggestions> actions(CommandContext context, SuggestionsBuilder builder) {
        builder.suggest("unload");
        builder.suggest("reload");
        return builder.buildFuture();
    }

}
