package wtf.cattyn.ferret.impl.features.modules;

import com.google.common.eventbus.Subscribe;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.api.feature.option.impl.BooleanOption;
import wtf.cattyn.ferret.api.feature.option.impl.EnumOption;
import wtf.cattyn.ferret.api.feature.option.impl.NumberOption;
import wtf.cattyn.ferret.api.manager.impl.ConfigManager;
import wtf.cattyn.ferret.impl.events.TickEvent;

public class Sprint extends Module {

    BooleanOption omni = new BooleanOption.Builder(false).name("omni").build(this);
    NumberOption integer = new NumberOption.Builder(5).name("int").setBounds(1, 10).build(this);
    NumberOption doubl = new NumberOption.Builder(5f).name("double").setBounds(1, 10).build(this);
    EnumOption enam = new EnumOption.Builder(Shit.OK).name("lel").build(this);

    public Sprint() {
        super("Sprint", Category.PLAYER);
        setToggled(true);
    }

    @Subscribe public void onTick(TickEvent e) {
        if(mc.player.forwardSpeed != 0 || omni.getValue()) {
            mc.player.setSprinting(true);
        }
    }

    public enum Shit {
        OK, FUCK, YOU, LMFAO
    }

}
