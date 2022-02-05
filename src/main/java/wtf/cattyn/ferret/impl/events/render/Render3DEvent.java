package wtf.cattyn.ferret.impl.events.render;

import net.minecraft.client.util.math.MatrixStack;
import wtf.cattyn.ferret.api.event.Event;

public class Render3DEvent extends Event {

    private final MatrixStack matrixStack;
    private final float tickDelta;

    private Render3DEvent(MatrixStack matrixStack, float tickDelta) {
        this.matrixStack = matrixStack;
        this.tickDelta = tickDelta;
    }

    public static class Pre extends Render3DEvent{
        public Pre(MatrixStack matrixStack, float tickDelta) {
            super(matrixStack, tickDelta);
        }

        @Override public String getName() {
            return "render_3d_pre";
        }

    }

    public static class Post extends Render3DEvent{
        public Post(MatrixStack matrixStack, float tickDelta) {
            super(matrixStack, tickDelta);
        }

        @Override public String getName() {
            return "render_3d_post";
        }

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
