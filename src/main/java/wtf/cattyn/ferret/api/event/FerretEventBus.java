package wtf.cattyn.ferret.api.event;

import com.google.common.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import wtf.cattyn.ferret.core.Ferret;

public class FerretEventBus extends EventBus {

     @Override public void post(@NotNull Object event) {
         if(event instanceof Event) {
             Ferret.getDefault().getScripts().runCallback("events", (( Event ) event).toLua());
         }
        super.post(event);
    }

}
