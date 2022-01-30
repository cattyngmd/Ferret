package wtf.cattyn.ferret.api.feature.script.lua.classes;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import wtf.cattyn.ferret.api.feature.script.lua.utils.LuaUtils;
import wtf.cattyn.ferret.common.impl.Vec2d;

public class GuiBuilder {

    private static LuaValue value;
    private final Text title;

    private LuaClosure render;
    private LuaClosure keyPressed;
    private LuaClosure charTyped;
    private LuaClosure mouseClicked;
    private LuaClosure mouseReleased;

    private boolean isPauseScreen = true;

    public GuiBuilder(Text title) {
        this.title = title;
    }

    public GuiBuilder setRender(LuaClosure render) {
        this.render = render;
        return this;
    }

    public GuiBuilder isPauseScreen(boolean isPauseScreen) {
        this.isPauseScreen = isPauseScreen;
        return this;
    }

    public GuiBuilder setCharTyped(LuaClosure charTyped) {
        this.charTyped = charTyped;
        return this;
    }

    public GuiBuilder setMouseClicked(LuaClosure mouseClicked) {
        this.mouseClicked = mouseClicked;
        return this;
    }

    public GuiBuilder setMouseReleased(LuaClosure mouseReleased) {
        this.mouseReleased = mouseReleased;
        return this;
    }

    public void setKeyPressed(LuaClosure keyPressed) {
        this.keyPressed = keyPressed;
    }

    public Screen build() {
        return new Screen(title) {
            @Override public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                super.render(matrices, mouseX, mouseY, delta);
                LuaUtils.safeCall(render,
                        CoerceJavaToLua.coerce(matrices),
                        CoerceJavaToLua.coerce(new Vec2d(mouseX, mouseY))
                );
            }

            @Override public boolean isPauseScreen() {
                return isPauseScreen;
            }

            @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                LuaUtils.safeCall(keyPressed,
                        LuaValue.valueOf(keyCode),
                        LuaValue.valueOf(scanCode),
                        LuaValue.valueOf(modifiers)
                );
                return super.keyPressed(keyCode, scanCode, modifiers);
            }

            @Override public boolean charTyped(char chr, int modifiers) {
               LuaUtils.safeCall(charTyped,
                        LuaValue.valueOf(chr),
                        LuaValue.valueOf(modifiers)
                );
                return super.charTyped(chr, modifiers);
            }

            @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
                LuaUtils.safeCall(mouseClicked,
                        CoerceJavaToLua.coerce(new Vec2d(mouseX, mouseY)),
                        LuaValue.valueOf(button)
                );
                return super.mouseClicked(mouseX, mouseY, button);
            }

            @Override public boolean mouseReleased(double mouseX, double mouseY, int button) {
                LuaUtils.safeCall(mouseReleased,
                        CoerceJavaToLua.coerce(new Vec2d(mouseX, mouseY)),
                        LuaValue.valueOf(button)
                );
                return super.mouseReleased(mouseX, mouseY, button);
            }
        };
    }

    public static LuaValue getLua() {
        if (value == null) {
            LuaTable table = new LuaTable();
            table.set("new", new New());
            table.set("__index", table);
            value = table;
        }
        return value;
    }

    public static class New extends OneArgFunction {

        public LuaValue call(LuaValue name) {
            if (!name.isstring()) throw new IllegalArgumentException("Invalid arguments.");
            GuiBuilder builder = new GuiBuilder(Text.of(name.tojstring()));
            return CoerceJavaToLua.coerce(
                    builder
            );
        }

    }

}
