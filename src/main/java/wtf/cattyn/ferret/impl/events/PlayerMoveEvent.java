package wtf.cattyn.ferret.impl.events;

import net.minecraft.entity.MovementType;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import wtf.cattyn.ferret.api.event.Event;

public class PlayerMoveEvent extends Event {

    private MovementType type;
    private Vec3d velocity;

    public PlayerMoveEvent(Vec3d velocity, MovementType type) {
        this.velocity = velocity;
        this.type = type;
    }

    public MovementType getType() {
        return type;
    }

    public Vec3d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec3d velocity) {
        this.velocity = velocity;
    }

    public void setVelocityX(double x) {
        this.velocity = new Vec3d(x, velocity.y, velocity.z);
    }

    public void setVelocityY(double y) {
        this.velocity = new Vec3d(velocity.x, y, velocity.z);
    }

    public void setVelocityZ(double z) {
        this.velocity = new Vec3d(velocity.x, velocity.y, z);
    }

    public void setVelocityXZ(double x, double z) {
        this.velocity = new Vec3d(x, velocity.y, z);
    }

    @Override public String getName() {
        return "player_move";
    }

}
