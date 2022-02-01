package wtf.cattyn.ferret.impl.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import wtf.cattyn.ferret.api.feature.command.Command;
import wtf.cattyn.ferret.api.manager.impl.ConfigManager;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class GetScriptCommand extends Command {

    public GetScriptCommand() {
        super("getscript", "Downloads a lua script form Ferret-Scripts github repo", "gets");
    }

    @Override
    public void exec(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                argument("name", StringArgumentType.string())
                        .executes(context -> {
                            String content = "";
                            String scriptName = StringArgumentType.getString(context, "name");
                            if (!scriptName.endsWith(".lua")) scriptName += ".lua";

                            try {
                                URL url = new URL("https://raw.githubusercontent.com/cattyngmd/Ferret-Scripts/main/scripts/" + scriptName);
                                URLConnection urlConnection = url.openConnection();
                                HttpURLConnection connection = (HttpURLConnection) urlConnection;
                                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                String currentLine;
                                while ((currentLine = in.readLine()) != null) {
                                    content += currentLine + "\n";
                                }
                            } catch (Exception e) {
                                ChatUtil.sendMessage("Incorrect name or no internet connection!");
                                return 0;
                            }

                            Path scriptPath = Path.of(ConfigManager.SCRIPT_FOLDER.getAbsolutePath(), scriptName);

                            try {
                                if (scriptPath.toFile().exists()) ChatUtil.sendMessage("This script already exists!");
                                else {
                                    Files.createFile(scriptPath);
                                    Files.write(scriptPath, content.getBytes(), StandardOpenOption.WRITE);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return 1;
                        })
        );
    }

}
