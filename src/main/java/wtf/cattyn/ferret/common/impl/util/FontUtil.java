package wtf.cattyn.ferret.common.impl.util;

import net.minecraft.client.util.math.MatrixStack;
import wtf.cattyn.ferret.common.Globals;
import wtf.cattyn.ferret.common.impl.Vec2d;

import java.awt.*;

public class FontUtil implements Globals {

    public static void draw(MatrixStack stack, Vec2d vec, String text, Color color) {
        mc.textRenderer.draw(stack, text, ( float ) vec.x(), ( float ) vec.y(), color.hashCode());
    }

    public static double width(String text) {
        return mc.textRenderer.getWidth(text);
    }

}
