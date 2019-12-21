package xylixlike.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import org.tinylog.Logger;
import xylixlike.engine.Level;
import xylixlike.engine.Properties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static xylixlike.ui.App.SCALE;

public class FileOperations {
    private FileOperations() {
        throw new IllegalStateException("Utility class");
    }

    public static Level loadLevel(String level) {
        JsonElement json = loadJson("levels/" + level + ".json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(json, Level.class);
    }

    public static Properties loadProperties(String filename) {
        JsonElement json = loadJson("entities/" + filename + ".json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(json, Properties.class);
    }

    public static JsonElement loadJson(String filename) {
        InputStream path = FileOperations.class.getClassLoader().getResourceAsStream(filename);
        if (path == null) {
            Logger.error("Entity: " + filename + " not found.");
        }
        try (InputStreamReader fr = new InputStreamReader(path)) {
            return JsonParser.parseReader(fr);
        } catch (IOException e) {
            Logger.error(e);
            return null;
        }
    }

    public static Paint loadSprite(String filename) {
        URL spriteUrl = FileOperations.class.getClassLoader().getResource("sprites/" + filename);
        if (spriteUrl != null) {
            Image sprite = new Image(spriteUrl.toString());
            return new ImagePattern(sprite, 0, 0, SCALE, SCALE, false);
        } else {
            Logger.error("No sprite named: '" + filename + "' found");
            return Color.GREEN;
        }
    }


    public static File getClassResource(String resource, Class clazz) {
        return new File(clazz.getClassLoader().getResource(resource).getFile());
    }
}
