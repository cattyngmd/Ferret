package wtf.cattyn.ferret.common.impl.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.common.Globals;

public class ChatUtil implements Globals {

    private ChatUtil() {
        throw new IllegalArgumentException();
    }

    public static void sendMessage(Text message) {
        if(mc.inGameHud == null) return;
        mc.inGameHud.getChatHud().addMessage(getDefaultPrefix().append(" ").append(message.shallowCopy()));
    }

    public static void sendMessage(String message) {
        if(mc.inGameHud == null) return;
        mc.inGameHud.getChatHud().addMessage(getDefaultPrefix().append(" ").append(new LiteralText(message).setStyle(Style.EMPTY)));
    }

    public static LiteralText getDefaultPrefix() {
        final LiteralText chatPrefix =
                new LiteralText(
                        String.format("%s[%sFerret%s]%s", Formatting.WHITE, Formatting.AQUA, Formatting.WHITE, Formatting.RESET)
                );
        chatPrefix.setStyle(
                Style.EMPTY.withHoverEvent(
                        new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT, new LiteralText(Formatting.AQUA + "Ferret")
                        )
                )
        );
        return chatPrefix;
    }

}
