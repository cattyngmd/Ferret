package wtf.cattyn.ferret.api.manager.impl;

import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.api.manager.Manager;
import wtf.cattyn.ferret.common.impl.trait.Enumerable;
import wtf.cattyn.ferret.impl.features.modules.*;

import java.util.ArrayList;
import java.util.List;

public final class ModuleManager extends ArrayList<Module> implements Manager<ModuleManager>, Enumerable<Module> {

    @Override public ModuleManager load() {
        addAll(List.of(
                new ExtraTab(),
                new TablistToggle(),
                new UnfocusedCPU(),
                new CameraClip()
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
