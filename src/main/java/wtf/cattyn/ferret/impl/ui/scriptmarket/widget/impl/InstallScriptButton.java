package wtf.cattyn.ferret.impl.ui.scriptmarket.widget.impl;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;
import wtf.cattyn.ferret.common.impl.util.ScriptUtil;
import wtf.cattyn.ferret.common.impl.util.StopWatch;
import wtf.cattyn.ferret.impl.ui.scriptmarket.widget.Component;

import java.awt.*;

public class InstallScriptButton extends Component {

    private final ScriptComponent SCRIPT;
    private Action statement;
    private boolean hovered;
    private final StopWatch updateWatcher = new StopWatch();

    public InstallScriptButton(ScriptComponent script, Action statement) {
        this.SCRIPT = script;
        this.statement = statement;
    }

    @Override public void render(MatrixStack matrix) {
        DrawableHelper.fill(matrix, mc.getWindow().getScaledWidth() - 185, SCRIPT.getOffset() + 10, mc.getWindow().getScaledWidth() - 35, SCRIPT.getOffset() + 30, hovered ? GRAY.getRGB() : BLACK.getRGB());

        mc.textRenderer.drawWithShadow(matrix, statement.name, mc.getWindow().getScaledWidth() - 110 - mc.textRenderer.getWidth(statement.name) / 2f, SCRIPT.getOffset() + 15, Color.WHITE.getRGB());
    }

    @Override public void updateComponent(double mouseX, double mouseY) {
        hovered = hovered(mouseX, mouseY);
        if (updateWatcher.passed(1000)) {
            if (ScriptUtil.isInstalled(SCRIPT.getScriptName())) {
                statement = Action.NONE;
            } else {
                statement = Action.INSTALL;
            }
            updateWatcher.reset();
        }
    }

    @Override public void mouseClicked(double mouseX, double mouseY, int button) {
        if (hovered(mouseX, mouseY) && button == 0) {
            if (statement == Action.INSTALL) {
                ScriptUtil.downloadScript(SCRIPT.getScriptName());
                statement = Action.NONE;
            }
        }
    }

    private boolean hovered(double mouseX, double mouseY) {
        return mouseX >= mc.getWindow().getScaledWidth() - 185 && mouseX <= mc.getWindow().getScaledWidth() - 35 && mouseY >= SCRIPT.getOffset() + 10 && mouseY <= SCRIPT.getOffset() + 30;
    }

    public enum Action {
        INSTALL("Install"),
        NONE(Formatting.GREEN + "Installed");

        final String name;

        Action(String name) {
            this.name = name;
        }

    }

}
