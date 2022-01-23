package wtf.cattyn.ferret.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;
import wtf.cattyn.ferret.api.manager.impl.MappingManager;
import wtf.cattyn.ferret.common.impl.PathConverter;
import wtf.cattyn.ferret.core.Ferret;

import java.nio.file.Path;

public interface Globals {

    MinecraftClient mc = MinecraftClient.getInstance();

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeHierarchyAdapter(Path.class, new PathConverter())
            .create();

    default Ferret ferret() {
        return Ferret.getDefault();
    }

    default MappingManager mappings() { return Ferret.getDefault().getMappingManager(); }

    static MinecraftClient mc() {
        return MinecraftClient.getInstance();
    }

}
