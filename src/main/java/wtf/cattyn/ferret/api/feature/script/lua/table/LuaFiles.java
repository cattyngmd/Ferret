package wtf.cattyn.ferret.api.feature.script.lua.table;

import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class LuaFiles {

    private static LuaFiles instance;

    LuaFiles() {
    }

    public File createFile(String path) {
        try { return Files.createFile(Path.of(path)).toFile(); }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Path createDir(String path) {
        try { return Files.createDirectory(Path.of(path)); }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public File createTempFile(String prefix, String suffix) {
        try { return Files.createTempFile(prefix, suffix).toFile(); }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Path createTempDir(String name) {
        try { return Files.createTempDirectory(name); }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LuaTable readFileAsTable(String path) {
        LuaTable table = new LuaTable();
        try {
            List<String> strings = Files.readAllLines(Path.of(path));
            for (int i = 0; i < strings.size() - 1; i++) {
                table.set(i + 1, strings.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }

    public String readFile(String path) {
        try {
            return String.join("\n", Files.readAllLines(Path.of(path)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeFile(String file, String content) {
        writeFile(file, content.getBytes(StandardCharsets.UTF_8));
    }

    public void writeFile(String file, byte[] content) {
        try { Files.write(Path.of(file), content); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public void walk(String path, LuaClosure closure) {
        try { Files.walk(Path.of(path)).forEach(p -> LuaUtils.safeCall(closure, CoerceJavaToLua.coerce(p))); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public void walkTree(String path, LuaClosure closure) {
        try { Files.walkFileTree(Path.of(path), new SimpleFileVisitor<>() { }).forEach(p -> LuaUtils.safeCall(closure, CoerceJavaToLua.coerce(p))); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public static LuaFiles getDefault() {
        if (instance == null) {
            instance = new LuaFiles();
        }
        return instance;
    }

}
