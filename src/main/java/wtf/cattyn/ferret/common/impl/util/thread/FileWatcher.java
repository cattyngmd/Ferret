package wtf.cattyn.ferret.common.impl.util.thread;

import wtf.cattyn.ferret.api.feature.script.Script;
import wtf.cattyn.ferret.core.Ferret;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yoursleep, mrnv, Cattyn
 */

public class FileWatcher extends Thread {

    Map<Script, Long> map = new HashMap<>();

    public FileWatcher() {
    }

    @Override public void run() {
        super.run();
        while (!interrupted()) {
            for (Script script : Ferret.getDefault().getScripts()) {
                try {
                    long lastModified = script.getPath().toFile().lastModified();
                    if (!map.containsKey(script)) {
                        map.put(script, lastModified);
                    } else {
                        // this shouldn't happen, but just for safeness...
                        if (!map.containsKey(script)) {
                            map.put(script, lastModified);
                            continue;
                        }

                        if (!map.get(script).equals(lastModified)) {
                            script.reload();
                            map.replace(script, lastModified);
                        }
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
        }

    }

}
