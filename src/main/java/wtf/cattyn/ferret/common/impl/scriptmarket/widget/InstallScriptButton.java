package wtf.cattyn.ferret.common.impl.scriptmarket.widget;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import wtf.cattyn.ferret.common.impl.scriptmarket.ScriptUtil;

import java.awt.*;

public class InstallScriptButton extends Component {
    private final ScriptComponent SCRIPT;
    private boolean downloaded, hovered;

    private final Color BLACK = new Color(0, 0, 0, 150);
    private final Color GRAY = new Color(30, 30, 30, 150);

    public InstallScriptButton(ScriptComponent script, boolean downloaded) {
        this.SCRIPT = script;
        this.downloaded = downloaded;
    }

    @Override public void render(MatrixStack matrix) {
        DrawableHelper.fill(matrix, mc.getWindow().getScaledWidth() - 185, SCRIPT.getOffset() + 10, mc.getWindow().getScaledWidth() - 35, SCRIPT.getOffset() + 30, hovered ? GRAY.getRGB() : BLACK.getRGB());
        String text = downloaded ? "Installed" : "Download";
        mc.textRenderer.drawWithShadow(matrix, text, mc.getWindow().getScaledWidth() - 110 - mc.textRenderer.getWidth(text) / 2, SCRIPT.getOffset() + 15, downloaded ? Color.GREEN.getRGB() : Color.WHITE.getRGB());
    }

    @Override public void updateComponent(double mouseX, double mouseY) {
        hovered = hovered(mouseX, mouseY);
        downloaded = ScriptUtil.isInstalled(SCRIPT.getScriptName());
    }

    @Override public void mouseClicked(double mouseX, double mouseY, int button) {
        if (hovered(mouseX, mouseY) && button == 0 && !ScriptUtil.isInstalled(SCRIPT.getScriptName())) {
            ScriptUtil.downloadScript(SCRIPT.getScriptName());
        }
    }

    private boolean hovered(double mouseX, double mouseY) {
        return mouseX >= mc.getWindow().getScaledWidth() - 185 && mouseX <= mc.getWindow().getScaledWidth() - 35 && mouseY >= SCRIPT.getOffset() + 10 && mouseY <= SCRIPT.getOffset() + 30;
    }
}
