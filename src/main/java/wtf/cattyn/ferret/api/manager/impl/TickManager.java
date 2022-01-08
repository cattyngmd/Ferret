package wtf.cattyn.ferret.api.manager.impl;

import wtf.cattyn.ferret.api.manager.Manager;

public class TickManager implements Manager<TickManager> {

    private float multiplier;

    @Override public TickManager load() {
        multiplier = 1f;
        return this;
    }

    @Override public TickManager unload() {
        multiplier = 1f;
        return this;
    }

    public float getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }

}
