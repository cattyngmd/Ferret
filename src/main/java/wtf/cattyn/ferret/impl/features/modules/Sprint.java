package wtf.cattyn.ferret.impl.features.modules;

import wtf.cattyn.ferret.api.feature.module.Module;
import wtf.cattyn.ferret.api.feature.option.impl.BooleanOption;
import wtf.cattyn.ferret.api.manager.impl.ConfigManager;

public class Sprint extends Module {

    BooleanOption option = new BooleanOption.Builder(false).name("Option").build(this);

    public Sprint() {
        super("Sprint", Category.PLAYER);
    }

}
