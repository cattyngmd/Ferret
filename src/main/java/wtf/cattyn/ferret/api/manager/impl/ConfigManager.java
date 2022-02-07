package wtf.cattyn.ferret.api.manager.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.ast.Str;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.api.feature.option.Option;
import wtf.cattyn.ferret.api.feature.script.Script;
import wtf.cattyn.ferret.api.feature.script.lua.classes.ModuleLua;
import wtf.cattyn.ferret.api.manager.Manager;
import wtf.cattyn.ferret.common.Globals;
import wtf.cattyn.ferret.core.Ferret;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public final class ConfigManager extends Thread implements Manager<ConfigManager>, Globals {

    public static final File MAIN_FOLDER = new File("ferret");
    public static final File SCRIPT_FOLDER = new File("ferret/scripts");
    public static final Path MODULES = Path.of(MAIN_FOLDER.getAbsolutePath(), "config.json");
    public static final Path SCRIPTS = Path.of(MAIN_FOLDER.getAbsolutePath(), "scripts.json");

    @Override public ConfigManager load() {
        String rawModules = "", rawScripts = "";
        try {
            if(MODULES.toFile().exists()) rawModules = new String(Files.readAllBytes(MODULES));
            if(SCRIPTS.toFile().exists()) rawScripts = new String(Files.readAllBytes(SCRIPTS));
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
        if(!rawModules.isBlank()) {
            JsonObject json = JsonParser.parseString(rawModules).getAsJsonObject();
            loadModules(json.get("modules").getAsJsonObject());
        }
        if(!rawScripts.isBlank()) {
            JsonObject json = JsonParser.parseString(rawScripts).getAsJsonObject();
            loadScripts(json);
        }
        loadPrefix();
        return this;
    }

    @Override public ConfigManager unload() {
        start();
        return this;
    }

    @Override public void run() {
        if (!MAIN_FOLDER.exists() && !MAIN_FOLDER.mkdirs()) Ferret.LOGGER.warn("Failed to create config folder");
        saveModules();
        savePrefix();
        saveScripts();
    }

    void loadModules(JsonObject json) {
        for (Module module : ferret().getModuleManager()) {
            if (json.get(module.getName()) != null) {
                try {
                    loadModule(module, json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void loadModule(Module module, JsonObject json) {
        module.fromJson(json.get(module.getName()).getAsJsonObject());
    }

    void loadPrefix() {
        Path path = Paths.get(MAIN_FOLDER.getPath()).resolve("prefix.txt");

        if (path.toFile().exists()) {
            try {
                CommandManager.setPrefix(Files.readString(path));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Files.write(Path.of(MAIN_FOLDER.getAbsolutePath(), "prefix.txt"), CommandManager.getPrefix().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void saveModules() {
        JsonObject object = new JsonObject();
        JsonObject modules = new JsonObject();
        Ferret.getDefault().getModuleManager().forEach(m -> {
            if (m instanceof ModuleLua) return;
            modules.add(m.getName(), m.toJson().value());
        });
        object.add("modules", modules);
        try {
            Files.write(Path.of(MAIN_FOLDER.getAbsolutePath(), "config.json"), gson.toJson(object).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void savePrefix() {
        try {
            Files.write(Path.of(MAIN_FOLDER.getAbsolutePath(), "prefix.txt"), CommandManager.getPrefix().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loadScripts(@Nullable JsonObject object) {
        if(object == null) return;
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            String name = entry.getKey();
            if(!name.endsWith(".lua")) continue;
            Script script;

            try { script = new Script(name, ""); }
            catch (IOException exception) { continue; }

            if (entry.getValue().isJsonObject()) {
                JsonObject scriptJson = entry.getValue().getAsJsonObject();

                if(scriptJson.has("active"))
                    script.setToggled(scriptJson.get("active").getAsBoolean());

                if(scriptJson.has("options")) {
                    for (Option option : Option.getForTarget(script)) {
                        option.fromJson(scriptJson.get("options").getAsJsonObject());
                    }
                }

                if(scriptJson.has("modules"))
                    loadModules(scriptJson.get("modules").getAsJsonObject());

                ferret().getScripts().add(script);
            }
        }
    }

    void saveScripts() {
        if(SCRIPTS.toFile().exists()) {
            try {
                SCRIPTS.toFile().createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JsonObject object = new JsonObject();

        for (Script script : ferret().getScripts()) {

            JsonObject scriptObject = new JsonObject();

            scriptObject.addProperty("active", script.isToggled());

            JsonObject module = new JsonObject();

            for (Module m : ferret().getModuleManager()) {
                if (!(m instanceof ModuleLua)) continue;
                if ((( ModuleLua ) m).getScript() == script) module.add(m.getName(), m.toJson().value());
            }

            scriptObject.add("modules", module);

            object.add(script.getName(), scriptObject);

            try {
                Files.write(SCRIPTS, gson.toJson(object).getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
