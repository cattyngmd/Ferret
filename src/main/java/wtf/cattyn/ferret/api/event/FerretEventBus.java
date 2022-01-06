package wtf.cattyn.ferret.api.event;

import com.google.common.eventbus.EventBus;

//TODO make events callback in catlua
public class FerretEventBus extends EventBus {

     @Override public void post(Object event) {
         if(event instanceof Event);
        super.post(event);
    }

}
