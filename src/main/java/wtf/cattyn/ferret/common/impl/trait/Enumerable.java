package wtf.cattyn.ferret.common.impl.trait;

import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public interface Enumerable<T> extends Iterable<T> {

    default void enumerate(LuaClosure closure) {
        for (T t : this) {
            closure.invoke(CoerceJavaToLua.coerce(t));
        }
    }

}
