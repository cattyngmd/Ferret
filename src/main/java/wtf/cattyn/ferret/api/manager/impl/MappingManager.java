package wtf.cattyn.ferret.api.manager.impl;

import fuck.you.yarnparser.V1Parser;
import fuck.you.yarnparser.entry.ClassEntry;
import fuck.you.yarnparser.entry.FieldEntry;
import fuck.you.yarnparser.entry.MethodEntry;
import org.apache.commons.io.FileUtils;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.ast.Str;
import wtf.cattyn.ferret.api.manager.Manager;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class MappingManager implements Manager<MappingManager> {

    private static final String YARN = "https://maven.fabricmc.net/net/fabricmc/yarn/1.18.1+build.18/yarn-1.18.1+build.18-tiny.gz";
    private final File mappings = new File(ConfigManager.MAIN_FOLDER, "mappings/mappings.tiny");
    private V1Parser parser;

    private Map<String, Field> fieldCache = new HashMap<>();
    private Map<String, LuaValue> methodCache = new HashMap<>();

    private synchronized void loadYarn() {
        if(!mappings.exists()) {
            try {
                File archive = new File(ConfigManager.MAIN_FOLDER, "mappings/mappings.gz");
                FileUtils.copyURLToFile(new URL(YARN), archive, 10000, 10000);
                extract(archive, mappings);
                archive.deleteOnExit();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    private void extract(File file, File outFile) {
        byte[] buffer = new byte[1024];

        try{
            GZIPInputStream in = new GZIPInputStream(new FileInputStream(file));
            FileOutputStream out = new FileOutputStream(outFile);
            int len;

            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            in.close();
            out.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @Override public MappingManager load() {

        loadYarn();

        try {
            parser = new V1Parser(mappings);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    @Override public MappingManager unload() {
        parser.reset();
        return this;
    }

    public V1Parser getParser() {
        return parser;
    }

    public Map<String, Field> getFieldCache() {
        return fieldCache;
    }

    public Map<String, LuaValue> getMethodCache() {
        return methodCache;
    }

}
