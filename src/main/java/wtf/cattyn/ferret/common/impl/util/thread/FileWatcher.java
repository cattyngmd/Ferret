package wtf.cattyn.ferret.common.impl.util.thread;

import com.google.common.hash.Hashing;
import wtf.cattyn.ferret.api.feature.script.Script;
import wtf.cattyn.ferret.core.Ferret;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YourSleep, Cattyn
 */

public class FileWatcher extends Thread {

    Map<Script, String> map = new HashMap<>();

    public FileWatcher() {
    }

    @Override public void run() {
        super.run();
        while (!interrupted()) {
            for (Script script : Ferret.getDefault().getScripts()) {
                try {
                    String hash = Base64.getEncoder().encodeToString(Hashing.sha256().hashBytes(Files.readAllBytes(script.getPath())).asBytes());
                    if (!map.containsKey(script)) {
                        map.put(script, hash);
                    } else {
                        // this shouldn't happen, but just for safeness...
                        if (!map.containsKey(script)) {
                            map.put(script, hash);
                            continue;
                        }

                        if (!map.get(script).equals(hash)) {
                            script.reload();
                        }
                    }
                    Thread.sleep(2000);
                } catch (Exception ignored) {

                }
            }
        }

    }

}
