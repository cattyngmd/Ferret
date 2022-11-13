package wtf.cattyn.ferret.common.impl.util;

import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import wtf.cattyn.ferret.common.Globals;

public class ChatUtil implements Globals {

    private ChatUtil() {
        throw new IllegalArgumentException();
    }

    public static void sendMessage(Text message) {
        if(mc.inGameHud == null) return;
        mc.inGameHud.getChatHud().addMessage(getDefaultPrefix().append(" ").append(message.copy()));
    }

    public static void sendMessage(String message) {
        if(mc.inGameHud == null) return;
        mc.inGameHud.getChatHud().addMessage(getDefaultPrefix().append(" ").append(Text.of(message)));
    }

    public static MutableText getDefaultPrefix() {
        final MutableText chatPrefix =
                Text.literal(
                        String.format("%s[%sFerret%s]%s", Formatting.WHITE, Formatting.AQUA, Formatting.WHITE, Formatting.RESET)
                );
        chatPrefix.setStyle(
                Style.EMPTY.withHoverEvent(
                        new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT, Text.of(Formatting.AQUA + "Ferret")
                        )
                )
        );
        return chatPrefix;
    }

}
