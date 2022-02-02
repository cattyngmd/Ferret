package wtf.cattyn.ferret.impl.ui.scriptmarket.widget;

import net.minecraft.client.util.math.MatrixStack;
import wtf.cattyn.ferret.common.Globals;

public abstract class Component implements Globals {
    public int offsetNested = 5;

    public abstract void render(MatrixStack matrix);

    public void updateComponent(double mouseX, double mouseY) {}

    public void mouseClicked(double mouseX, double mouseY, int button) {}

    public void mouseReleased(double mouseX,  double mouseY,  int mouseButton) {}

    public void keyTyped(int key) {}

    public void setOffsetY(int offsetY) {}

    public int getHeight() { return 0; }
}