package wtf.cattyn.ferret.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.cattyn.ferret.api.feature.script.lua.argument.HudArgument;
import wtf.cattyn.ferret.core.Ferret;
import wtf.cattyn.ferret.impl.events.render.Render2DEvent;

@Mixin( InGameHud.class )
public class MixinInGameHud {

    @Inject(method = "render", at = @At("RETURN"))
    public void render(MatrixStack matrixStack, float float_1, CallbackInfo ci) {
        RenderSystem.setShaderColor(1, 1, 1, 1);

        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        RenderSystem.disableCull();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        RenderSystem.disableTexture();

        Ferret.getDefault().getScripts().runCallback("hud", CoerceJavaToLua.coerce(new HudArgument(matrixStack, float_1)));
        Render2DEvent event = new Render2DEvent(matrixStack);
        Ferret.EVENT_BUS.post(event);

        RenderSystem.enableDepthTest();
        RenderSystem.enableTexture();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);

    }

}
