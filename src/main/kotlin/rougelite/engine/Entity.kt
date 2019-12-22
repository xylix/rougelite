package rougelite.engine

import com.google.gson.Gson
import com.google.gson.annotations.JsonAdapter
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import org.tinylog.kotlin.Logger
import rougelite.ui.Game
import rougelite.ui.Game.Companion.SCALE
import rougelite.utils.FileOperations
import kotlin.collections.HashMap

@JsonAdapter(EntitySerializer::class)
class Entity(
    private val type: String,
    x: Int,
    y: Int,
    width: Int?,
    height: Int?,
    properties: Properties
) {
    constructor(d: EntitySerializer.EntityData, p: Properties) : this(d.type, d.x, d.y, d.width, d.height, p)
    private var actionMap: HashMap<String, String>
    @Transient var hitbox: Rectangle
    @JvmField var movable = false
    @JvmField var passable = false

    init {
        movable = properties.movable
        passable = properties.passable
        actionMap = properties.actionMap
        hitbox = Rectangle(
                (x * SCALE).toDouble(),
                (y * SCALE).toDouble(),
                (width ?: properties.width) * SCALE.toDouble(),
                (height ?: properties.width) * SCALE.toDouble())
        hitbox.id = type
        val fillLocation: String = properties.graphics ?: "$type.png"
        hitbox.fill = FileOperations.loadSprite(fillLocation)
    }

    fun movementAction(direction: Direction) {
        if (type == "player") move(direction)
    }

    fun collide(collidee: Entity): Boolean {
        val collisionBox = Shape.intersect(hitbox, collidee.hitbox)
        return collisionBox.boundsInLocal.width != -1.0
    }

    fun collisionAction(collidee: Entity): String {
        return actionMap.getOrDefault(collidee.type, "")
    }

    fun move(direction : Direction) {
        Logger.trace("Moving in direction: $direction")
        when (direction) {
            Direction.DOWN -> hitbox.translateY = hitbox.translateY + Game.SCALE
            Direction.RIGHT -> hitbox.translateX = hitbox.translateX + Game.SCALE
            Direction.UP -> hitbox.translateY = hitbox.translateY - Game.SCALE
            Direction.LEFT -> hitbox.translateX = hitbox.translateX - Game.SCALE
        }
    }

    val json: String
        get() {
            val gson = Gson()
            return gson.toJson(this)
        }
}
