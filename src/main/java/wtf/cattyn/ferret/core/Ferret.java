package wtf.cattyn.ferret.core;

import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wtf.cattyn.ferret.api.event.FerretEventBus;
import wtf.cattyn.ferret.api.manager.impl.*;
import wtf.cattyn.ferret.common.impl.util.thread.FileWatcher;

public class Ferret {

    private static Ferret INSTANCE;
    public static final Logger LOGGER = LogManager.getLogger("Ferret");
    public static final FerretEventBus EVENT_BUS = new FerretEventBus();

    private FileWatcher fileWatcher;

    private TickManager tickManager;
    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private CommandManager commands;
    private ScriptManager scripts;

    public Ferret() {

    }

    public void init() {
        commands = new CommandManager().load();
        tickManager = new TickManager().load();
        moduleManager = new ModuleManager().load();
        scripts = new ScriptManager().load();
        fileWatcher = new FileWatcher();
        fileWatcher.start();
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

    public CommandManager getCommandManager() {
        return commands;
    }

    public TickManager getTickManager() {
        return tickManager;
    }

}
