package wtf.cattyn.ferret.impl.events;

import wtf.cattyn.ferret.api.event.Event;

public class StartupEvent extends Event {

    @Override public String getName() {
        return getClass().getSimpleName();
    }

}
