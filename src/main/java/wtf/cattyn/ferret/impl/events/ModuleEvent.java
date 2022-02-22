package wtf.cattyn.ferret.impl.events;

import wtf.cattyn.ferret.api.event.Event;
import wtf.cattyn.ferret.api.feature.module.Module;

public class ModuleEvent extends Event {

    private final Module module;
    private final boolean state;

    public ModuleEvent(Module module, boolean state) {
        this.module = module;
        this.state = state;
    }

    public Module getModule() {
        return module;
    }

    public boolean getState() {
        return state;
    }

    @Override public String getName() {
        return "module";
    }

}
