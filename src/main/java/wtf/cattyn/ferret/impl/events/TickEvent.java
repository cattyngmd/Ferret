package wtf.cattyn.ferret.impl.events;

import wtf.cattyn.ferret.api.event.Event;

public class TickEvent extends Event {

    @Override public String getName() {
        return "tick";
    }

}
