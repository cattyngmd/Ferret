package wtf.cattyn.ferret.impl.features.modules;

import net.minecraft.entity.decoration.EndCrystalEntity;
import wtf.cattyn.ferret.api.feature.module.Module;

public class ExtraTab extends Module {
    // code: wtf.cattyn.ferret.mixins.MixinPlayerListHud
    public ExtraTab() {
        super("ExtraTab", "Shows all players in the tablist", Category.MISC);
    }

}
