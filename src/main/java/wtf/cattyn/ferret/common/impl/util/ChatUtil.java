package wtf.cattyn.ferret.common.impl.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import wtf.cattyn.ferret.common.Globals;

public class ChatUtil implements Globals {

    private static final String chatPrefix = String.format("%s[%sFerret%s]%s", Formatting.WHITE, Formatting.AQUA, Formatting.WHITE, Formatting.RESET);

    private ChatUtil() {
        throw new IllegalArgumentException();
    }

    public static void sendMessage(String message) {
        mc.inGameHud.getChatHud().addMessage(Text.of(chatPrefix + " " + message));
    }

}
