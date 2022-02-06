package wtf.cattyn.ferret.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.LiteralText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wtf.cattyn.ferret.api.event.FerretEventBus;
import wtf.cattyn.ferret.api.feature.option.Option;
import wtf.cattyn.ferret.api.manager.impl.*;
import wtf.cattyn.ferret.common.impl.util.thread.FileWatcher;

import java.util.ArrayList;
import java.util.List;

public class Ferret {

    private static Ferret INSTANCE;
    public static final Logger LOGGER = LogManager.getLogger("Ferret");
    public static final FerretEventBus EVENT_BUS = new FerretEventBus();
    private boolean remapped;

    private FileWatcher fileWatcher;

    private MappingManager mappingManager;
    private TickManager tickManager;
    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private CommandManager commands;
    private ScriptManager scripts;

    public Ferret() {
    }

    public void init() {
        try {
           MinecraftClient.class.getDeclaredField("player");
           remapped = false;
        } catch (NoSuchFieldException e) {
            System.out.println(LiteralText.class.getName());
            remapped = true;
        }
        mappingManager = new MappingManager();
        if(remapped) {
            mappingManager.load();
        }
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

    public MappingManager getMappingManager() {
        return mappingManager;
    }

    public List<Option<?>> getOptions() {
        return Option.getOptions();
    }

    public boolean isRemapped() {
        return remapped;
    }

}
