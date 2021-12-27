package wtf.cattyn.ferret.impl.features.modules;

import com.google.common.eventbus.Subscribe;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.api.feature.option.impl.BooleanOption;
import wtf.cattyn.ferret.api.manager.impl.ConfigManager;
import wtf.cattyn.ferret.impl.events.TickEvent;

public class Sprint extends Module {

    BooleanOption omni = new BooleanOption.Builder(false).name("omni").build(this);

    public Sprint() {
        super("Sprint", Category.PLAYER);
        setToggled(true);
    }

    @Subscribe public void onTick(TickEvent e) {
        if(mc.player.forwardSpeed != 0 || omni.getValue()) {
            mc.player.setSprinting(true);
        }
    }

}
