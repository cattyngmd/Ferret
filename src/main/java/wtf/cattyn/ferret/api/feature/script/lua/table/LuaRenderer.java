package wtf.cattyn.ferret.api.feature.script.lua.table;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import wtf.cattyn.ferret.common.Globals;
import wtf.cattyn.ferret.common.impl.Vec2d;

import java.awt.*;

public class LuaRenderer extends DrawableHelper implements Globals {

    private static LuaRenderer instance;

    LuaRenderer() {
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
        this.drawHorizontalLine(stack, x1, x2, y1, color.hashCode());
        this.drawVerticalLine(stack, x2, y1, y2, color.hashCode());
        this.drawHorizontalLine(stack, x1, x2, y2, color.hashCode());
        this.drawVerticalLine(stack, x1, y1, y2, color.hashCode());
    }

    public void rectFilled(MatrixStack stack, Vec2d from, Vec2d to, Color color) {
        DrawableHelper.fill(stack, ( int ) from.x(), ( int ) from.y(), ( int ) to.x(), ( int ) to.y(), color.hashCode());
    }

    public void roundedRectFilled(MatrixStack stack, Vec2d from, Vec2d to, double radius, Color color) {
        if (to.x() - from.x() <= radius * 2 || to.y() - from.y() <= radius * 2) return;
        int col = color.hashCode();
        fill(stack, ( int ) (from.x() + radius), ( int ) from.y(), ( int ) (to.x() - radius), ( int ) to.y(), col);
        fill(stack, ( int ) from.x(), ( int ) (from.y() + radius), ( int ) (from.x() + radius), ( int ) (to.y() - radius), col);
        fill(stack, ( int ) (to.x() - radius), ( int ) (from.y() + radius), ( int ) to.x(), ( int ) (to.y() - radius), col);

        circleFilled(stack, new Vec2d(from.x() + radius, from.y() + radius), radius, 0, color);
        circleFilled(stack, new Vec2d(from.x() + radius, to.y() - radius), radius, 1, color);
        circleFilled(stack, new Vec2d(to.x() - radius, to.y() - radius), radius, 2, color);
        circleFilled(stack, new Vec2d(to.x() - radius, from.y() + radius), radius, 3, color);
    }

    public void rectFilledFade(MatrixStack stack, Vec2d from, Vec2d to, Color color1, Color color2) {
        this.fillGradient(stack, ( int ) from.x(), ( int ) from.y(), ( int ) to.x(), ( int ) to.y(), color1.hashCode(), color2.hashCode());
    }

    public void circleFilled(MatrixStack stack, Vec2d pos, double radius, int part, Color color) {
        int col = color.hashCode();
        float  a = (float)(col >> 24 & 255) / 255.0F,
                r = (float)(col >> 16 & 255) / 255.0F,
                g = (float)(col >> 8 & 255) / 255.0F,
                b = (float)(col & 255) / 255.0F;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex( stack.peek().getPositionMatrix(), ( float ) pos.x(), ( float ) pos.y(), 0 ).color( r, g, b, a ).next( );
        double DOUBLE_PI = Math.PI * 2;
        for ( int i = part == -1 ? 0 : part * 90; i <= (part == -1 ? 360 : part * 90 + 90); i++) {
            double angle = ( DOUBLE_PI * i / 360 ) + Math.toRadians( 180 );
            bufferBuilder.vertex( stack.peek().getPositionMatrix(), ( float ) (pos.x() + Math.sin( angle ) * radius), ( float ) (pos.y() + Math.cos( angle ) * radius), 0 ).color( r, g, b, a ).next( );
        }
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
    }

    public void circle(MatrixStack stack, Vec2d pos, double radius, int part, Color color) {
        int col = color.hashCode();
        float  a = (float)(col >> 24 & 255) / 255.0F,
                r = (float)(col >> 16 & 255) / 255.0F,
                g = (float)(col >> 8 & 255) / 255.0F,
                b = (float)(col & 255) / 255.0F;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
        double DOUBLE_PI = Math.PI * 2;
        for ( int i = part == -1 ? 0 : part * 90; i <= (part == -1 ? 360 : part * 90 + 90); i++) {
            double angle = ( DOUBLE_PI * i / 360 ) + Math.toRadians( 180 );
            bufferBuilder.vertex( stack.peek().getPositionMatrix(), ( float ) (pos.x() + Math.sin( angle ) * radius), ( float ) (pos.y() + Math.cos( angle ) * radius), 0 ).color( r, g, b, a ).next( );
        }
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
    }

    public void line(MatrixStack stack, Vec2d from, Vec2d to, Color color) {
        float i2 = (float)(color.hashCode() >> 24 & 0xFF) / 255.0f;
        float f = (float)(color.hashCode() >> 16 & 0xFF) / 255.0f;
        float g = (float)(color.hashCode() >> 8 & 0xFF) / 255.0f;
        float h = (float)(color.hashCode() & 0xFF) / 255.0f;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), ( float ) from.x(), ( float ) from.y(), 0.0f).color(f, g, h, i2).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), ( float ) to.x(), ( float ) to.y(), 0.0f).color(f, g, h, i2).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
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

    //3D

    public void drawBoxFilled(MatrixStack stack, Box box, Color c) {
        float minX = (float) (box.minX - mc.getEntityRenderDispatcher().camera.getPos().getX());
        float minY = (float) (box.minY - mc.getEntityRenderDispatcher().camera.getPos().getY());
        float minZ = (float) (box.minZ - mc.getEntityRenderDispatcher().camera.getPos().getZ());
        float maxX = (float) (box.maxX - mc.getEntityRenderDispatcher().camera.getPos().getX());
        float maxY = (float) (box.maxY - mc.getEntityRenderDispatcher().camera.getPos().getY());
        float maxZ = (float) (box.maxZ - mc.getEntityRenderDispatcher().camera.getPos().getZ());

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        setup3D();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, minZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, minZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, maxZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, maxZ).color(c.getRGB()).next();

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, minZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, maxZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, maxZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, minZ).color(c.getRGB()).next();

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, minZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, minZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, minZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, minZ).color(c.getRGB()).next();

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, minZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, minZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, maxZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, maxZ).color(c.getRGB()).next();

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, maxZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, maxZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, maxZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, maxZ).color(c.getRGB()).next();

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, minZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, maxZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, maxZ).color(c.getRGB()).next();
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, minZ).color(c.getRGB()).next();

        tessellator.draw();
        clean3D();
    }

    public void drawBoxFilled(MatrixStack stack, Vec3d vec, Color c) {
        drawBoxFilled(stack, Box.from(vec), c);
    }

    public void drawBoxFilled(MatrixStack stack, BlockPos bp, Color c) {
        drawBoxFilled(stack, new Box(bp), c);
    }

    public void drawBox(MatrixStack stack, Box box, Color c, double lineWidth) {
        float minX = (float) (box.minX - mc.getEntityRenderDispatcher().camera.getPos().getX());
        float minY = (float) (box.minY - mc.getEntityRenderDispatcher().camera.getPos().getY());
        float minZ = (float) (box.minZ - mc.getEntityRenderDispatcher().camera.getPos().getZ());
        float maxX = (float) (box.maxX - mc.getEntityRenderDispatcher().camera.getPos().getX());
        float maxY = (float) (box.maxY - mc.getEntityRenderDispatcher().camera.getPos().getY());
        float maxZ = (float) (box.maxZ - mc.getEntityRenderDispatcher().camera.getPos().getZ());

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        setup3D();
        RenderSystem.lineWidth(( float ) lineWidth);
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);

        RenderSystem.defaultBlendFunc();

        bufferBuilder.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);

        WorldRenderer.drawBox(stack, bufferBuilder, minX, minY, minZ, maxX, maxY, maxZ, c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);

        tessellator.draw();
        clean3D();
    }

    public void drawBox(MatrixStack stack, Vec3d vec, Color c, double lineWidth) {
        drawBox(stack, Box.from(vec), c, lineWidth);
    }

    public void drawBox(MatrixStack stack, BlockPos bp, Color c, double lineWidth) {
        drawBox(stack, new Box(bp), c, lineWidth);
    }

    public void drawSemi2dRect(Vec3d pos, Vec2d offset, Vec2d offset2, double scale, Color color) {
        MatrixStack matrices = matrixFrom(pos);
        Camera camera = mc.gameRenderer.getCamera();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        matrices.translate(offset.x(), offset.y(), 0);
        matrices.scale(-0.025f * (float) scale, -0.025f * (float) scale, 1);
        int halfWidth = (int) (offset2.x() / 2);
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        DrawableHelper.fill(matrices, -halfWidth, 0, (int) (offset2.x() - halfWidth), ( int ) offset2.y(), color.getRGB());
    }

    public void drawSemi2dText(String text, Vec3d pos, Vec2d offset, Vec2d offset2, double scale, Color color, boolean shadow) {
        MatrixStack matrices = matrixFrom(pos);
        Camera camera = mc.gameRenderer.getCamera();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        matrices.translate(offset.x(), offset.y(), 0);
        matrices.scale(-0.025f * (float) scale, -0.025f * (float) scale, 1);
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        if (shadow) {
            mc.textRenderer.drawWithShadow(matrices, text, ( float ) offset2.x(), ( float ) offset2.y(), color.getRGB());
        } else {
            mc.textRenderer.draw(matrices, text, ( float ) offset2.x(), ( float ) offset2.y(), color.getRGB());
        }
        immediate.draw();
        RenderSystem.disableBlend();
    }

    public static MatrixStack matrixFrom(Vec3d pos) {
        MatrixStack matrices = new MatrixStack();
        Camera camera = mc.gameRenderer.getCamera();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
        matrices.translate(pos.getX() - camera.getPos().x, pos.getY() - camera.getPos().y, pos.getZ() - camera.getPos().z);
        return matrices;
    }

    public void setup() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }

    public void setup3D() {
        setup();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
    }

    public void clean() {
        RenderSystem.disableBlend();
    }

    public void clean3D() {
        clean();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
    }

    public static LuaRenderer getDefault() {
        if(instance == null) instance = new LuaRenderer();
        return instance;
    }

}
