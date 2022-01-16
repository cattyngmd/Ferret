package wtf.cattyn.ferret.api.feature.script.lua.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;
import wtf.cattyn.ferret.common.Globals;
import wtf.cattyn.ferret.common.impl.Vec2d;

import java.awt.*;

public class LuaRenderer extends DrawableHelper implements Globals {

    private static LuaRenderer instance;

    public LuaRenderer() {
    }

    public void text(MatrixStack stack, String text, Vec2d vec2d, Color color) {
        mc.textRenderer.draw(stack, text, ( float ) vec2d.x(), ( float ) vec2d.y(), color.hashCode());
    }

    public void text(MatrixStack stack, Text text, Vec2d vec2d, Color color) {
        mc.textRenderer.draw(stack, text, ( float ) vec2d.x(), ( float ) vec2d.y(), color.hashCode());
    }

    public void textWithShadow(MatrixStack stack, String text, Vec2d vec2d, Color color) {
        mc.textRenderer.drawWithShadow(stack, text, ( float ) vec2d.x(), ( float ) vec2d.y(), color.hashCode());
    }

    public void textWithShadow(MatrixStack stack, Text text, Vec2d vec2d, Color color) {
        mc.textRenderer.drawWithShadow(stack, text, ( float ) vec2d.x(), ( float ) vec2d.y(), color.hashCode());
    }

    public void rect(MatrixStack stack, Vec2d from, Vec2d to, Color color) {
        int x1 = ( int ) from.x(), x2 = ( int ) to.x(), y1 = ( int ) from.y(), y2 = ( int ) to.y();
        float i2 = (float)(color.hashCode() >> 24 & 0xFF) / 255.0f;
        float f = (float)(color.hashCode() >> 16 & 0xFF) / 255.0f;
        float g = (float)(color.hashCode() >> 8 & 0xFF) / 255.0f;
        float h = (float)(color.hashCode() & 0xFF) / 255.0f;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), x1, y2, 0.0f).color(f, g, h, i2).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), x2, y2, 0.0f).color(f, g, h, i2).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), x2, y1, 0.0f).color(f, g, h, i2).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), x1, y1, 0.0f).color(f, g, h, i2).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public void rectFilled(MatrixStack stack, Vec2d from, Vec2d to, Color color) {
        DrawableHelper.fill(stack, ( int ) from.x(), ( int ) from.y(), ( int ) to.x(), ( int ) to.y(), color.hashCode());
    }

    public void rectFilledFade(MatrixStack stack, Vec2d from, Vec2d to, Color color1, Color color2) {
        this.fillGradient(stack, ( int ) from.x(), ( int ) from.y(), ( int ) to.x(), ( int ) to.y(), color1.hashCode(), color2.hashCode());
    }

    public void line(MatrixStack stack, Vec2d from, Vec2d to, Color color) {
        float i2 = (float)(color.hashCode() >> 24 & 0xFF) / 255.0f;
        float f = (float)(color.hashCode() >> 16 & 0xFF) / 255.0f;
        float g = (float)(color.hashCode() >> 8 & 0xFF) / 255.0f;
        float h = (float)(color.hashCode() & 0xFF) / 255.0f;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), ( float ) from.x(), ( float ) from.y(), 0.0f).color(f, g, h, i2).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), ( float ) to.x(), ( float ) to.y(), 0.0f).color(f, g, h, i2).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public int width(String text) {
        return mc.textRenderer.getWidth(text);
    }

    public int width(Text text) {
        return mc.textRenderer.getWidth(text);
    }

    public double windowWidth() {
        return mc.getWindow().getScaledWidth();
    }

    public double windowHeight() {
        return mc.getWindow().getScaledHeight();
    }

    public static LuaRenderer getDefault() {
        if(instance == null) instance = new LuaRenderer();
        return instance;
    }

}
