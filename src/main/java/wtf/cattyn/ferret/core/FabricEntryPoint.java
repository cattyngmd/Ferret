package wtf.cattyn.ferret.core;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FabricEntryPoint implements ModInitializer {

	@Override public void onInitialize() {
		Ferret.getDefault().init();
	}
}
