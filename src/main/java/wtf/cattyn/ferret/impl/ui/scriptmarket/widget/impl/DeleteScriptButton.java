package wtf.cattyn.ferret.impl.ui.scriptmarket.widget.impl;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import wtf.cattyn.ferret.common.impl.util.ScriptUtil;
import wtf.cattyn.ferret.impl.ui.scriptmarket.widget.Component;

import java.awt.*;

public class DeleteScriptButton extends Component {
    private boolean shouldRender, hovered;
    private final ScriptComponent SCRIPT;

    public DeleteScriptButton(ScriptComponent script) {
        this.SCRIPT = script;
        this.shouldRender = ScriptUtil.isInstalled(script.getScriptName());
    }

    @Override public void render(MatrixStack matrix) {
        if (!shouldRender) return;
        DrawableHelper.fill(matrix, mc.getWindow().getScaledWidth() - 260, SCRIPT.getOffset() + 10, mc.getWindow().getScaledWidth() - 190, SCRIPT.getOffset() + 30, hovered ? GRAY.getRGB() : BLACK.getRGB());
        mc.textRenderer.drawWithShadow(matrix, "Uninstall", mc.getWindow().getScaledWidth() - 225 - mc.textRenderer.getWidth("Uninstall") / 2, SCRIPT.getOffset() + 15, Color.RED.getRGB());
    }

    @Override public void updateComponent(double mouseX, double mouseY) {
        hovered = hovered(mouseX, mouseY);
        shouldRender = ScriptUtil.isInstalled(SCRIPT.getScriptName());
    }

    @Override public void mouseClicked(double mouseX, double mouseY, int button) {
        if (hovered(mouseX, mouseY) && button == 0) {
            ScriptUtil.deleteScript(SCRIPT.getScriptName());
        }
    }

    private boolean hovered(double mouseX, double mouseY) {
        return mouseX >= mc.getWindow().getScaledWidth() - 260 && mouseX <= mc.getWindow().getScaledWidth() - 190 && mouseY >= SCRIPT.getOffset() + 10 && mouseY <= SCRIPT.getOffset() + 30 && shouldRender;
    }
}
