package wtf.cattyn.ferret.impl.features.modules;

import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.api.feature.option.impl.NumberOption;

public class UnfocusedCPU extends Module {
    public NumberOption limit = new NumberOption.Builder(1).name("Limit").setBounds(1, 30).build(this);

    public UnfocusedCPU() {
        super("UnfocusedCPU", "Reduces the fps when the game window is minimized", Category.MISC);
    }
}
