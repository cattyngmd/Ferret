package wtf.cattyn.ferret.api.manager.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.api.manager.Manager;
import wtf.cattyn.ferret.common.Globals;
import wtf.cattyn.ferret.core.Ferret;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ConfigManager extends Thread implements Manager<ConfigManager>, Globals {

    private static final File MAIN_FOLDER = new File("ferret");

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
    }

    void loadModules(JsonObject json) {
        for (Module module : ferret().getModuleManager()) {
            if(json.get(module.getName()) != null) {
                try {
                    module.fromJson(json.get(module.getName()).getAsJsonObject());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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

}
