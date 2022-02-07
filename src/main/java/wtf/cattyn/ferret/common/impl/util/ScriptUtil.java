package wtf.cattyn.ferret.common.impl.util;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import wtf.cattyn.ferret.api.feature.script.Script;
import wtf.cattyn.ferret.api.manager.impl.ConfigManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ScriptUtil {
    public static JsonObject getAllScripts() {
        String content = getUrlContent("https://api.github.com/repos/cattyngmd/Ferret-Scripts/git/trees/main?recursive=1");
        return (JsonObject) JsonParser.parseString(content);
    }

    public static String getScriptName(JsonObject element) {
        return element.get("path").toString();
    }

    public static boolean isModule(String content) {
        return content.contains("Module.new");
    }

    public static String getUrlContent(String url) {
        String content = "";

        try {
            content = HttpRequest.get(new URL(url)).body();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return content;
    }

    public static String getModuleInfo(String content) {
        int index = content.indexOf("Module.new(") + 11;
        StringBuilder module = new StringBuilder();
        char a = content.charAt(index);
        while (a != ')') {
            a = content.charAt(index++);
            if (a != '"') module.append(a);
        }
        return module.toString().replace(",", " -").replace("- this)", "");
    }

    public static boolean isInstalled(String script) {
        return Path.of(ConfigManager.SCRIPT_FOLDER.getAbsolutePath(), script).toFile().exists();
    }

    public static boolean isScriptUpdated(String script, JsonObject json) {
        if(!json.has("size")) return true;
        return Path.of(ConfigManager.SCRIPT_FOLDER.getAbsolutePath(), script).toFile().length() == json.get("size").getAsLong();
    }

    public static void downloadScript(String scriptName) {
        Path scriptPath = Path.of(ConfigManager.SCRIPT_FOLDER.getAbsolutePath(), scriptName);
        try {
            if (!scriptPath.toFile().exists()) Files.createFile(scriptPath);
            Files.write(scriptPath, getUrlContent("https://raw.githubusercontent.com/cattyngmd/Ferret-Scripts/main/scripts/" + scriptName).getBytes(), StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteScript(String scriptName) {
        Path scriptPath = Path.of(ConfigManager.SCRIPT_FOLDER.getAbsolutePath(), scriptName);
        try {
            Files.delete(scriptPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
