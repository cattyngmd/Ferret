package wtf.cattyn.ferret.api.manager.impl;

import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.api.manager.Manager;
import wtf.cattyn.ferret.impl.features.modules.AirPlace;
import wtf.cattyn.ferret.impl.features.modules.Sprint;

import java.util.ArrayList;
import java.util.List;

public final class ModuleManager extends ArrayList<Module> implements Manager<ModuleManager> {

    @Override public ModuleManager load() {
        addAll(List.of(
                new Sprint(),
                new AirPlace()
        ));
        return this;
    }

    @Override public ModuleManager unload() {
        clear();
        return this;
    }

    public Module get(String name) {
        return stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
