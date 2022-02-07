package wtf.cattyn.ferret.impl.ui.scriptmarket;

import com.google.gson.JsonElement;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import wtf.cattyn.ferret.common.impl.util.ScriptUtil;
import wtf.cattyn.ferret.impl.ui.scriptmarket.widget.impl.ScriptComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static wtf.cattyn.ferret.common.Globals.mc;

public class ScriptMarket extends Screen {
    private final ArrayList<ScriptComponent> components = new ArrayList<>();
    private String search = "";
    private TextFieldWidget searchWidget;
    private int offsetScroll = 0;
    private boolean loaded;

    public ScriptMarket() {
        super(new LiteralText("ScriptMarket"));
        new Thread(this::fromGitHub).start();
    }

    @Override public void init() {
        super.init();
        searchWidget = new TextFieldWidget(mc.textRenderer, 7, 10, 150, 15, Text.of(""));
        searchWidget.setSuggestion("Search...");
        searchWidget.setMaxLength(100);
        searchWidget.setEditable(true);
        addDrawableChild(searchWidget);
        addDrawableChild(new ButtonWidget(163, 8, 50, 17, Text.of("Search"), (button) -> setSearch(searchWidget.getText())));
        addDrawableChild(new ButtonWidget(216, 8, 40, 17, Text.of("Clear"), (button -> setSearch(""))));
    }

    private void fromGitHub() {
        for (JsonElement obj : ScriptUtil.getAllScripts().get("tree").getAsJsonArray()) {
            if (ScriptUtil.getScriptName(obj.getAsJsonObject()).contains(".lua")) {
                components.add(new ScriptComponent(obj.getAsJsonObject()));
            }
        }
        loaded = true;
    }

    @Override public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrices);
        mc.textRenderer.drawWithShadow(matrices, "Script Market",
                mc.getWindow().getScaledWidth() / 2f - mc.textRenderer.getWidth("Script Market") / 2f, 10, Color.WHITE.getRGB());
        int offset = 30;
        if (!loaded) {
            mc.textRenderer.drawWithShadow(matrices,"Loading...", mc.getWindow().getScaledWidth() / 2f - mc.textRenderer.getWidth("Loading...") / 2f, mc.getWindow().getScaledHeight() / 2f, Color.WHITE.getRGB());
            return;
        }
        if (getMaxElements() >= components.size()) {
            for (ScriptComponent comp : components) {
                if (comp.getScriptName().toLowerCase(Locale.ROOT).contains(search)) {
                    comp.render(matrices);
                    comp.setOffset(offset);
                    offset += 45;
                }
            }
        } else {
            for (int i = offsetScroll; i < getMaxElements() + offsetScroll; i++) {
                if (components.get(i).getScriptName().toLowerCase(Locale.ROOT).contains(search)) {
                    components.get(i).setOffset(offset);
                    components.get(i).render(matrices);
                    components.get(i).updateComponent(mouseX, mouseY);
                    offset += 45;
                }
            }
        }

        components.forEach(c -> c.updateComponent(mouseX, mouseY));
        searchWidget.setSuggestion(searchWidget.getText().equals("") ? "Search..." : "");
        super.render(matrices, mouseX, mouseY, partialTicks);
    }

    @Override public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        components.forEach(c -> c.mouseClicked(mouseX, mouseY, mouseButton));
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override public boolean mouseReleased(double mouseX, double mouseY, int state) {
        components.forEach(c -> c.mouseReleased(mouseX, mouseY, state));
        return super.mouseReleased(mouseX, mouseY, state);
    }

    @Override public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (amount > 0 && offsetScroll > 0) offsetScroll--;
        else if (amount < 0 && offsetScroll < components.size() - getMaxElements()) offsetScroll++;
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void setSearch(String text) {
        search = text.toLowerCase(Locale.ROOT);
    }

    private int getMaxElements() {
        return (mc.getWindow().getScaledHeight() - 100) / 45 + 1;
    }
}
