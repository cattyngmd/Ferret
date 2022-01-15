package wtf.cattyn.ferret.impl.events.render;

import net.minecraft.client.util.math.MatrixStack;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import wtf.cattyn.ferret.api.event.Event;

public class Render2DEvent extends Event {

    private final MatrixStack matrixStack;
    private final float tickDelta;

    public Render2DEvent(MatrixStack matrixStack, float tickDelta) {
        this.matrixStack = matrixStack;
        this.tickDelta = tickDelta;
    }

    public MatrixStack stack() {
        return matrixStack;
    }

    public float tickDelta() {
        return tickDelta;
    }

    @Override public String getName() {
        return "render_2d";
    }

}
