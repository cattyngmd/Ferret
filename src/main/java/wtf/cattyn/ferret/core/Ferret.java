package wtf.cattyn.ferret.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wtf.cattyn.ferret.api.event.FerretEventBus;
import wtf.cattyn.ferret.api.manager.impl.CommandManager;
import wtf.cattyn.ferret.api.manager.impl.ConfigManager;
import wtf.cattyn.ferret.api.manager.impl.ModuleManager;
import wtf.cattyn.ferret.api.manager.impl.ScriptManager;
import wtf.cattyn.ferret.common.impl.util.thread.FileWatcher;

public class Ferret {

    private static Ferret INSTANCE;
    public static final Logger LOGGER = LogManager.getLogger("Ferret");
    public static final FerretEventBus EVENT_BUS = new FerretEventBus();

    private FileWatcher fileWatcher;
    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private CommandManager commands;
    private ScriptManager scripts;

    public Ferret() {

    }

    public void init() {
        commands = new CommandManager().load();
        moduleManager = new ModuleManager().load();
        fileWatcher = new FileWatcher();
        fileWatcher.start();
        scripts = new ScriptManager().load();
        configManager = new ConfigManager();
        configManager.load();
        EVENT_BUS.register(new EventHandler());
    }

    public static Ferret getDefault() {
        if (INSTANCE == null) INSTANCE = new Ferret();
        return INSTANCE;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ScriptManager getScripts() {
        return scripts;
    }

    public FileWatcher getFileWatcher() {
        return fileWatcher;
    }

}
