package wtf.cattyn.ferret.impl.ui.scriptmarket.widget.impl;

import com.google.gson.JsonObject;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import wtf.cattyn.ferret.common.impl.util.ScriptUtil;
import wtf.cattyn.ferret.impl.ui.scriptmarket.widget.Component;

import java.awt.*;
import java.util.function.Consumer;

public class ScriptComponent extends Component {
    private final JsonObject scriptObject;
    private String luaContent;
    private final String scriptName;
    private final InstallScriptButton installScriptButton;
    private final DeleteScriptButton deleteScriptButton;
    private final Thread downloadThread;
    private boolean hovered, isModule;
    private int offset;

    public ScriptComponent(JsonObject object) {
        this.scriptObject = object;
        this.scriptName = ScriptUtil.getScriptName(scriptObject).split("/")[1].replace("\"", "");
        InstallScriptButton.Action statement;
        if (ScriptUtil.isInstalled(scriptName)) {
           statement = InstallScriptButton.Action.NONE;
        } else {
            statement = InstallScriptButton.Action.INSTALL;
        }
        this.luaContent = "Loading";
        this.downloadThread = new DownloadThread(this.scriptObject, (t) -> {
            this.luaContent = t.content;
            this.isModule = t.isModule;
        });
        this.downloadThread.start();
        this.installScriptButton = new InstallScriptButton(this, statement);
        this.deleteScriptButton = new DeleteScriptButton(this);
    }

    @Override
    public void render(MatrixStack matrix) {
        DrawableHelper.fill(matrix, 30, offset, mc.getWindow().getScaledWidth() - 30, offset + 40, hovered ? GRAY.getRGB() : BLACK.getRGB());
        mc.textRenderer.drawWithShadow(matrix, scriptName, 35, offset + 7, Color.WHITE.getRGB());
        mc.textRenderer.drawWithShadow(matrix, isModule ? "Module : " + ScriptUtil.getModuleInfo(luaContent) : "Script", 35, offset + 20, Color.GRAY.getRGB());
        installScriptButton.render(matrix);
        deleteScriptButton.render(matrix);
    }

    public void setOffset(int newOffset) {
        this.offset = newOffset;
    }

    @Override public void updateComponent(double mouseX, double mouseY) {
        hovered = hovered(mouseX, mouseY);
        installScriptButton.updateComponent(mouseX, mouseY);
        deleteScriptButton.updateComponent(mouseX, mouseY);
    }

    @Override public void mouseClicked(double mouseX, double mouseY, int key) {
        installScriptButton.mouseClicked(mouseX, mouseY, key);
        deleteScriptButton.mouseClicked(mouseX, mouseY, key);
    }

    private boolean hovered(double mouseX, double mouseY) {
        return mouseX >= 30 && mouseX <= mc.getWindow().getScaledWidth() - 30 && mouseY >= offset && mouseY <= offset + 40;
    }

    public JsonObject getScriptJson() {
        return scriptObject;
    }

    public int getOffset() {
        return offset;
    }

    public String getScriptName() {
        return scriptName;
    }

    private static class DownloadThread extends Thread {
        private final Consumer<DownloadThread> callback;
        private final JsonObject object;
        private String content;
        private boolean isModule;

        private DownloadThread(JsonObject object, Consumer<DownloadThread> callback) {
            this.callback = callback;
            this.object = object;
        }

        @Override public void run() {
            super.run();
            this.content = ScriptUtil.getUrlContent("https://raw.githubusercontent.com/cattyngmd/Ferret-Scripts/main/" + ScriptUtil.getScriptName(object).substring(1, ScriptUtil.getScriptName(object).length() - 1));
            this.isModule = ScriptUtil.isModule(content);
            callback.accept(this);
        }
    }

}
