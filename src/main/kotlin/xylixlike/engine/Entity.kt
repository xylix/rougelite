package xylixlike.engine

import com.google.gson.Gson
import com.google.gson.annotations.JsonAdapter
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import org.tinylog.kotlin.Logger
import xylixlike.ui.App
import xylixlike.utils.FileOperations
import java.util.Objects
import kotlin.collections.HashMap
import kotlin.math.abs

@JsonAdapter(EntitySerializer::class)
class Entity {
    private val type: String
    private val x: Int
    private val y: Int
    private var width: Int? = null
    private var height: Int? = null
    private lateinit var actionMap: HashMap<String, String>
    @Transient
    lateinit var hitbox: Rectangle
    @JvmField
    var movable = false
    @JvmField
    var passable = false

    constructor(type: String, x: Int, y: Int) {
        this.type = type
        this.x = x
        this.y = y
        setProperties(FileOperations.loadProperties(type))
    }

    constructor(type: String, x: Int, y: Int, width: Int, height: Int) {
        this.type = type
        this.x = x
        this.y = y
        this.width = width
        this.height = height
        setProperties(FileOperations.loadProperties(type))
    }

    fun setProperties(source: Properties) {
        if (width == null || height == null) {
            width = source.width
            height = source.height
        }
        movable = source.movable
        passable = source.passable

        actionMap = if (source.actionMap == null) HashMap() else source.actionMap!!
        hitbox = Rectangle((x * App.SCALE).toDouble(), (y * App.SCALE).toDouble(), (width!! * App.SCALE).toDouble(), (height!! * App.SCALE).toDouble())
        hitbox.id = type
        val fill : String = if (source.graphics == null) "$type.png" else source.graphics!!
        hitbox.fill = FileOperations.loadSprite(fill)
    }

    fun movementAction(direction: Int) {
        if (type == "player") move(direction)
    }

    fun collide(collidee: Entity): Boolean {
        val collisionBox = Shape.intersect(hitbox, collidee.hitbox)
        return collisionBox.boundsInLocal.width != -1.0
    }

    fun collisionAction(collidee: Entity): String {
        return actionMap.getOrDefault(collidee.type, "")
    }

    fun move(i: Int) {
        var i = i
        Logger.trace("Moving in direction: $i")
        i = abs(i % 360)
        when (i) {
            0 -> hitbox.translateY = hitbox.translateY + App.SCALE
            90 -> hitbox.translateX = hitbox.translateX + App.SCALE
            180 -> hitbox.translateY = hitbox.translateY - App.SCALE
            270 -> hitbox.translateX = hitbox.translateX - App.SCALE
            else -> Logger.error("Illegal movement call")
        }
    }

    val json: String
        get() {
            val gson = Gson()
            return gson.toJson(this)
        }
}