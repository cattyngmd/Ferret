package wtf.cattyn.ferret.api.manager.impl;

import com.google.common.io.Files;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import wtf.cattyn.ferret.api.manager.Manager;
import wtf.cattyn.ferret.common.Globals;
import wtf.cattyn.ferret.common.impl.trait.Json;
import wtf.cattyn.ferret.core.Ferret;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public final class ConfigManager extends Thread implements Manager<ConfigManager>, Globals {

    private static final File MAIN_FOLDER = new File("ferret");

    @Override public ConfigManager load() {

        return this;
    }

    @Override public ConfigManager unload() {
        start();
        return this;
    }

    @Override public void run() {
        if (!MAIN_FOLDER.exists() && !MAIN_FOLDER.mkdirs()) System.out.println("Failed to create config folder");
        saveModules();
    }

    void saveModules() {
        JsonObject object = new JsonObject();
        JsonObject modules = new JsonObject();
        Ferret.getDefault().getModuleManager().forEach(m -> {
            modules.add(m.getName(), m.toJson());
        });
        object.add("modules", modules);
        try {
            Files.write(gson.toJson(object).getBytes(), Path.of(MAIN_FOLDER.getAbsolutePath(), "config.json").toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
