package wtf.cattyn.ferret.api.event;

import com.google.common.eventbus.EventBus;

public class FerretEventBus extends EventBus {

     @Override public void post(Object event) {
         if(event instanceof Event);
        super.post(event);
    }

}
