package wtf.cattyn.ferret.impl.ui.scriptmarket.widget.impl;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;
import wtf.cattyn.ferret.common.impl.util.ScriptUtil;
import wtf.cattyn.ferret.common.impl.util.StopWatch;
import wtf.cattyn.ferret.impl.ui.scriptmarket.ScriptMarket;
import wtf.cattyn.ferret.impl.ui.scriptmarket.widget.Component;

import java.awt.*;
import java.util.concurrent.Future;

public class InstallScriptButton extends Component {
    private final ScriptComponent script;
    private Future<?> future;
    private Action statement;
    private boolean hovered;
    private final StopWatch updateWatcher = new StopWatch();

    public InstallScriptButton(ScriptComponent script, Action statement) {
        this.script = script;
        this.statement = statement;
    }

    @Override public void render(MatrixStack matrix) {
        DrawableHelper.fill(matrix, mc.getWindow().getScaledWidth() - 185, script.getOffset() + 10, mc.getWindow().getScaledWidth() - 35, script.getOffset() + 30, hovered ? GRAY.getRGB() : BLACK.getRGB());

        mc.textRenderer.drawWithShadow(matrix, future != null && !future.isDone() ? "Downloading..." : statement.name, mc.getWindow().getScaledWidth() - 110 - mc.textRenderer.getWidth(future != null && !future.isDone() ? "Downloading..." : statement.name) / 2f, script.getOffset() + 15, Color.WHITE.getRGB());
    }

    @Override public void updateComponent(double mouseX, double mouseY) {
        hovered = hovered(mouseX, mouseY);
        if (updateWatcher.passed(500)) {
            if (ScriptUtil.isInstalled(script.getScriptName())) {
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
                this.future = ScriptMarket.downloadService.submit(() -> {
                    ScriptUtil.downloadScript(script.getScriptName());
                    statement = Action.NONE;
                });
            }
        }
    }

    private boolean hovered(double mouseX, double mouseY) {
        return mouseX >= mc.getWindow().getScaledWidth() - 185 && mouseX <= mc.getWindow().getScaledWidth() - 35 && mouseY >= script.getOffset() + 10 && mouseY <= script.getOffset() + 30;
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
