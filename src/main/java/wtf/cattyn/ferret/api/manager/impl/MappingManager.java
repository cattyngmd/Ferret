package wtf.cattyn.ferret.api.manager.impl;

import fuck.you.yarnparser.V1Parser;
import fuck.you.yarnparser.entry.ClassEntry;
import fuck.you.yarnparser.entry.FieldEntry;
import fuck.you.yarnparser.entry.MethodEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BowItem;
import org.apache.commons.io.FileUtils;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.ast.Str;
import wtf.cattyn.ferret.api.manager.Manager;
import wtf.cattyn.ferret.common.impl.util.ChatUtil;
import wtf.cattyn.ferret.core.MixinPlugin;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.zip.GZIPInputStream;

public final class MappingManager {

    private static final String YARN = "https://maven.fabricmc.net/net/fabricmc/yarn/1.18.2+build.2/yarn-1.18.2+build.2-tiny.gz";
    private final File mappings = new File("ferret/mappings/mappings.tiny");
    private V1Parser parser;

    private final Map<String, ClassEntry> classEntryCache = new HashMap<>();
    private final Map<String, FieldEntry> fieldEntryCache = new HashMap<>();
    private final Map<String, MethodEntry> methodEntryCache = new HashMap<>();

    private final Map<String, Field> fieldCache = new HashMap<>();
    private final Map<String, LuaValue> methodCache = new HashMap<>();
    private final Map<String, Class<?>> classCache = new HashMap<>();

    private static MappingManager instance;

    public static MappingManager getInstance( )
    {
        if( instance == null )
            instance = new MappingManager( );

        return instance;
    }

    private synchronized void loadYarn() {
        if(!mappings.exists()) {
            try {
                File archive = new File("ferret/mappings/mappings.gz");
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

    public MappingManager load() {

        loadYarn();

        try {
            parser = new V1Parser(mappings);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    public MappingManager unload() {
        parser.reset();
        return this;
    }

    public ClassEntry remapClass(String name, String type, boolean debug) {
        if (classEntryCache.containsKey(name)) {
            return classEntryCache.get(name);
        }

        ClassEntry entry = null;

        try {
            entry = parser.findClass(name, V1Parser.ClassFindType.valueOf(type.toUpperCase()));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (entry != null) classEntryCache.put(name, entry);
        else if (debug) {
            MixinPlugin.LOGGER.warn(name + " class is not found.");
            ChatUtil.sendMessage(name + " class is not found.");
        }

        return entry;
    }

    public FieldEntry remapField(String className, String name, String type) {
        if (fieldEntryCache.containsKey(className + "." + name)) {
            return fieldEntryCache.get(className + "." + name);
        }

        FieldEntry entry = null;

        try {
            entry = parser.findField(className, name, V1Parser.NormalFindType.valueOf(type.toUpperCase()));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (entry != null) fieldEntryCache.put(className + "." + name, entry);
        else {
            MixinPlugin.LOGGER.warn(className + "." + name + " field is not found.");
            ChatUtil.sendMessage(className + "." + name + " field is not found.");
        }

        return entry;
    }

    public MethodEntry remapMethod(String className, String name, String type, String desc) {
        if (methodEntryCache.containsKey(className + "." + name)) {
            return methodEntryCache.get(className + "." + name);
        }

        MethodEntry entry = null;

        try {
            entry = parser.findMethod(className, name, V1Parser.NormalFindType.valueOf(type.toUpperCase()), -1, desc);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (entry != null) methodEntryCache.put(className + "." + name, entry);
        else {
            MixinPlugin.LOGGER.warn(className + "." + name + " method is not found.");
            ChatUtil.sendMessage(className + "." + name + " method is not found.");
        }

        return entry;
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

    public Map<String, Class<?>> getClassCache() {
        return classCache;
    }

}
