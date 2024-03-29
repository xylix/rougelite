package rougelite.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.paint.Paint
import org.tinylog.kotlin.Logger
import rougelite.engine.Level
import rougelite.engine.EntityPrototype
import rougelite.ui.Game
import java.io.IOException
import java.io.InputStreamReader

object FileOperations {
    fun loadLevel(level: String): Level {
        val json = loadJson("levels/$level.json")
        val gson = GsonBuilder().setPrettyPrinting().create()
        return gson.fromJson(json, Level::class.java)
    }

    @JvmStatic
    fun loadProperties(filename: String): EntityPrototype {
        val json = loadJson("entities/$filename.json")
        val gson = GsonBuilder().setPrettyPrinting().create()
        return gson.fromJson(json, EntityPrototype::class.java)
    }

    private fun loadJson(filename: String): JsonElement? {
        val path = FileOperations::class.java.classLoader.getResourceAsStream(filename)
        if (path == null) {
            Logger.error("Entity: $filename not found.")
        }
        try {
            InputStreamReader(path).use { fr -> return JsonParser.parseReader(fr) }
        } catch (e: IOException) {
            Logger.error(e)
            return null
        }
    }

    fun loadSprite(filename: String): Paint {
        val spriteUrl = FileOperations::class.java.classLoader.getResource("sprites/$filename")
        return if (spriteUrl != null) {
            val sprite = Image(spriteUrl.toString())
            ImagePattern(sprite, 0.0, 0.0, Game.SCALE.toDouble(), Game.SCALE.toDouble(), false)
        } else {
            Logger.error("No sprite named: '$filename' found")
            Color.GREEN
        }
    }
}
