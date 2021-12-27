package wtf.cattyn.ferret.api.manager;

import wtf.cattyn.ferret.common.Globals;

public interface Manager<T> extends Globals {

    T load();

    T unload();

}
