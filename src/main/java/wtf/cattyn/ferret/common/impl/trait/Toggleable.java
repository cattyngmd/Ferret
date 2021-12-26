package wtf.cattyn.ferret.common.impl.trait;

public interface Toggleable {

    boolean isToggled();

    default void toggle() {
        if (isToggled()) {
            disable();
        } else {
            enable();
        }
    }

    default void setToggled(boolean toggled) {
        if(isToggled() != toggled) toggle();
    }

    void enable();

    void disable();

}
