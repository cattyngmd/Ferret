package wtf.cattyn.ferret.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;
import wtf.cattyn.ferret.core.Ferret;

public interface Globals {

    MinecraftClient mc = MinecraftClient.getInstance();
    Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

    default Ferret ferret() {
        return Ferret.getDefault();
    }

}
