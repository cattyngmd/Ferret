package wtf.cattyn.ferret.api.manager;

public interface Manager<T> {

    T load();

    T unload();

}
