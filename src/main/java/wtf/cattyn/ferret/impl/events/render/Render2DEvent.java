package wtf.cattyn.ferret.impl.events.render;

import net.minecraft.client.util.math.MatrixStack;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import wtf.cattyn.ferret.api.event.Event;

public class Render2DEvent extends Event {

    private final MatrixStack matrixStack;

    public Render2DEvent(MatrixStack matrixStack) {
        this.matrixStack = matrixStack;
    }

    public MatrixStack getStack() {
        return matrixStack;
    }

    @Override public String getName() {
        return "render_2d";
    }

}
