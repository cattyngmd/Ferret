package wtf.cattyn.ferret.impl.events;

import wtf.cattyn.ferret.api.event.Event;

public abstract class PlayerMotionUpdateEvent extends Event {

    public static class Pre extends PlayerMotionUpdateEvent {
        @Override public String getName() {
            return "player_motion_update_pre";
        }
    }

    public static class Post extends PlayerMotionUpdateEvent {
        @Override public String getName() {
            return "player_motion_update_post";
        }
    }

}
