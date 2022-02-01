package wtf.cattyn.ferret.impl.events.render;

import net.minecraft.client.util.math.MatrixStack;
import wtf.cattyn.ferret.api.event.Event;

public class Render3DEvent extends Event {

    private final MatrixStack matrixStack;
    private final float tickDelta;

    public Render3DEvent(MatrixStack matrixStack, float tickDelta) {
        this.matrixStack = matrixStack;
        this.tickDelta = tickDelta;
    }

    public MatrixStack getStack() {
        return matrixStack;
    }

    public float getDelta() {
        return tickDelta;
    }

    @Override public String getName() {
        return "render_3d";
    }

}
