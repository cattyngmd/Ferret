package wtf.cattyn.ferret.mixins.ducks;

import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin( GameMessageS2CPacket.class )
public interface DuckGameMessageS2CPacket {

    @Mutable @Accessor("content")
    void setChatMessage(Text message);

}
