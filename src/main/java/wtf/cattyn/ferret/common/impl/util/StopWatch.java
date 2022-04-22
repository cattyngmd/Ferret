package wtf.cattyn.ferret.common.impl.util;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class StopWatch {

    long time = -1L;

    public boolean passed(double time, TimeUnit unit) {
        return unit.convert(Duration.ofMillis(System.currentTimeMillis() - this.time)) >= time;
    }

    public boolean passed(long time) {
        return System.currentTimeMillis() - this.time >= time;
    }

    public void reset() {
        time = System.currentTimeMillis();
    }

}
