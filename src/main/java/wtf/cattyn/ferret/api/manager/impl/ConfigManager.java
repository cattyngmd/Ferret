package wtf.cattyn.ferret.api.manager.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

public final class ConfigManager extends Thread implements Manager<ConfigManager>, Globals {

    public static final File MAIN_FOLDER = new File("ferret");
    public static final File SCRIPT_FOLDER = new File("ferret/scripts");

    @Override public ConfigManager load() {
        String raw;
        try {
            raw = new String(Files.readAllBytes(Path.of(MAIN_FOLDER.getAbsolutePath(), "config.json")));
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
        JsonObject json = JsonParser.parseString(raw).getAsJsonObject();
        loadModules(json.get("modules").getAsJsonObject());
        loadScripts();
        loadPrefix();
        return this;
    }

    @Override public ConfigManager unload() {
        start();
        return this;
    }

    @Override public void run() {
        if (!MAIN_FOLDER.exists() && !MAIN_FOLDER.mkdirs()) System.out.println("Failed to create config folder");
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
            modules.add(m.getName(), m.toJson());
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

    void loadScripts() {
        if (!SCRIPT_FOLDER.exists()) SCRIPT_FOLDER.mkdirs();

        try {
            Files.walk(SCRIPT_FOLDER.toPath()).forEach(f -> {
                if (f.toFile().getName().endsWith(".json")) {
                    try {
                        String raw = new String(Files.readAllBytes(f));
                        JsonObject object = JsonParser.parseString(raw).getAsJsonObject();
                        String name = f.toFile().getName();
                        Script script = new Script(name.substring(0, name.length() - 5), "");
                        script.setToggled(object.get("active").getAsBoolean());

                        for (Option option : Option.getForTarget(script)) {
                            option.fromJson(object.get("options").getAsJsonObject());
                        }

                        loadModules(object.get("modules").getAsJsonObject());

                        ferret().getScripts().add(script);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void saveScripts() {
        for (Script script : ferret().getScripts()) {

            File file = new File(SCRIPT_FOLDER, script.getName() + ".json");

            JsonObject object = new JsonObject();
            object.addProperty("active", script.isToggled());

            JsonObject module = new JsonObject();

            for (Module m : ferret().getModuleManager()) {
                if (!(m instanceof ModuleLua)) continue;
                if ((( ModuleLua ) m).getScript() == script) module.add(m.getName(), m.toJson());
            }

            object.add("modules", module);

            try {
                Files.write(file.toPath(), gson.toJson(object).getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
