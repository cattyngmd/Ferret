package wtf.cattyn.ferret.core;

import com.google.common.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wtf.cattyn.ferret.api.manager.impl.CommandManager;
import wtf.cattyn.ferret.api.manager.impl.ConfigManager;
import wtf.cattyn.ferret.api.manager.impl.ModuleManager;

public class Ferret {

    private static Ferret INSTANCE;
    public static final Logger LOGGER = LogManager.getLogger("Ferret");
    public static final EventBus EVENT_BUS= new EventBus();

    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private CommandManager commands;

    public Ferret() {

    }

    public void init() {
        commands = new CommandManager().load();
        (moduleManager = new ModuleManager()).load();
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

}
