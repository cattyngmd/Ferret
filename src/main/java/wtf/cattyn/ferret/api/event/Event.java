package wtf.cattyn.ferret.api.event;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import wtf.cattyn.ferret.common.Globals;
import wtf.cattyn.ferret.common.impl.trait.Nameable;

public abstract class Event implements Nameable, Globals {

    public boolean cancelled;

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        cancelled = true;
    }

    @Override public String toString() {
        return getName();
    }

    public LuaValue toLua() {
        return CoerceJavaToLua.coerce(this);
    }

}
