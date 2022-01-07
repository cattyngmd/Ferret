package wtf.cattyn.ferret.common.impl.util.thread;

import wtf.cattyn.ferret.api.feature.script.Script;
import wtf.cattyn.ferret.api.manager.impl.ConfigManager;
import wtf.cattyn.ferret.common.impl.util.StopWatch;
import wtf.cattyn.ferret.core.Ferret;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.*;

public class FileWatcher extends Thread {

    StopWatch lastReload = new StopWatch();

    public FileWatcher() {
    }

    @Override public void run() {
        super.run();
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path dir = ConfigManager.SCRIPT_FOLDER.toPath();
            dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

            while (true) {
                WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException ex) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path path = ( Path ) event.context();

                    if (kind == ENTRY_MODIFY) {

                        String name = path.toFile().getName();

                        for (Script script : Ferret.getDefault().getScripts()) {

                            if(script.getName().equalsIgnoreCase(name) && lastReload.passed(0.1, TimeUnit.SECONDS)) {
                                System.out.println("reload");
                                script.reload();
                                lastReload.reset();
                            }

                        }

                    }

                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
