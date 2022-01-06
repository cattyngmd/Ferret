package wtf.cattyn.ferret.api.event;

import wtf.cattyn.ferret.common.impl.trait.Nameable;

public abstract class Event implements Nameable {

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

}
