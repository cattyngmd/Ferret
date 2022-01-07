package wtf.cattyn.ferret.api.event;

import com.google.common.eventbus.EventBus;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;
import wtf.cattyn.ferret.core.Ferret;

//TODO make events callback in catlua
public class FerretEventBus extends EventBus {

     @Override public void post(Object event) {
         if(event instanceof Event) Ferret.getDefault().getScripts().runCallback("events", (( Event ) event).toLua());
        super.post(event);
    }

}
