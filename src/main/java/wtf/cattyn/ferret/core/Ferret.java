package wtf.cattyn.ferret.core;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wtf.cattyn.ferret.api.event.FerretEventBus;
import wtf.cattyn.ferret.api.feature.option.Option;
import wtf.cattyn.ferret.api.manager.impl.*;
import wtf.cattyn.ferret.common.impl.util.thread.FileWatcher;

import java.util.List;

public class Ferret {

    private static Ferret INSTANCE;
    public static final Logger LOGGER = LogManager.getLogger("Ferret");
    public static final FerretEventBus EVENT_BUS = new FerretEventBus();
    private boolean remapped;

    private FileWatcher fileWatcher;

    private TickManager tickManager;
    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private CommandManager commands;
    private ScriptManager scripts;
    private RotationManager rotationManager;

    public Ferret() {
    }

    public void init() {
        remapped = !FabricLoader.getInstance().isDevelopmentEnvironment();
        rotationManager = new RotationManager().load();
        commands = new CommandManager().load();
        tickManager = new TickManager().load();
        scripts = new ScriptManager().load();
        moduleManager = new ModuleManager().load();
        fileWatcher = new FileWatcher();
        fileWatcher.start();
        configManager = new ConfigManager().load();
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

    public CommandManager getCommandManager() {
        return commands;
    }

    public TickManager getTickManager() {
        return tickManager;
    }

    public MappingManager getMappingManager() {
        return MappingManager.getInstance( );
    }

    public RotationManager getRotationManager() {
        return rotationManager;
    }

    public List<Option<?>> getOptions() {
        return Option.getOptions();
    }

    public boolean isRemapped() {
        return remapped;
    }

}
