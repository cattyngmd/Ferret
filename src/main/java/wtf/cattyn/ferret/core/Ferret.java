package wtf.cattyn.ferret.core;

import com.google.common.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wtf.cattyn.ferret.api.manager.impl.ConfigManager;
import wtf.cattyn.ferret.api.manager.impl.ModuleManager;

public class Ferret {

    private static Ferret INSTANCE;
    public static final Logger LOGGER = LogManager.getLogger("Ferret");
    public static final EventBus EVENT_BUS= new EventBus();

    private ModuleManager moduleManager;
    private ConfigManager configManager;

    public Ferret() {
        init();
    }

    private void init() {
        (moduleManager = new ModuleManager()).load();
        configManager = new ConfigManager();
        configManager.load();
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
