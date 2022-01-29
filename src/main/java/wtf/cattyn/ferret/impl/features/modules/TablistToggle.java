package wtf.cattyn.ferret.impl.features.modules;

import com.google.common.eventbus.Subscribe;
import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.impl.events.TickEvent;

public class TablistToggle extends Module {

    public static boolean tablistToggled = false;

    public TablistToggle() {
        super("TablistToggle", "Toggle tablist using tab key", Category.VISUAL);
    }

    @Subscribe
    public void onTick(TickEvent e) {
        if (tablistToggled) mc.options.keyPlayerList.setPressed(true);
    }

}
