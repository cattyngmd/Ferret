package wtf.cattyn.ferret.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;

public interface Globals {

    MinecraftClient mc = MinecraftClient.getInstance();
    Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

}
