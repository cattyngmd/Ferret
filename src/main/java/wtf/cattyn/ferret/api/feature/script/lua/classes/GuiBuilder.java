package wtf.cattyn.ferret.api.feature.script.lua.classes;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.lwjgl.glfw.GLFW;
import wtf.cattyn.ferret.common.impl.Vec2d;

public class GuiBuilder {

    private static LuaValue value;
    private final Text title;

    private LuaClosure render;
    private LuaClosure keyPressed;

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

    public void setKeyPressed(LuaClosure keyPressed) {
        this.keyPressed = keyPressed;
    }

    public Screen build() {
        return new Screen(title) {
            @Override public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                super.render(matrices, mouseX, mouseY, delta);
                render.call(
                        CoerceJavaToLua.coerce(matrices),
                        CoerceJavaToLua.coerce(new Vec2d(mouseX, mouseY))
                );
            }

            @Override public boolean isPauseScreen() {
                return isPauseScreen;
            }

            @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                keyPressed.call(
                        LuaValue.valueOf(keyCode),
                        LuaValue.valueOf(scanCode),
                        LuaValue.valueOf(modifiers)
                );
                return super.keyPressed(keyCode, scanCode, modifiers);
            }
        };
    }

    public static LuaValue getLua() {
        if(value == null) {
            LuaTable table = new LuaTable();
            table.set("new", new New());
            table.set("__index", table);
            value = table;
        }
        return value;
    }

    public static class New extends OneArgFunction {

        public LuaValue call(LuaValue name) {
            if(!name.isstring()) throw new IllegalArgumentException("Invalid arguments.");
            GuiBuilder builder = new GuiBuilder(Text.of(name.tojstring()));
            return CoerceJavaToLua.coerce(
                    builder
            );
        }

    }

}
