package rougelite.engine

import com.google.gson.annotations.JsonAdapter
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import org.tinylog.kotlin.Logger
import rougelite.ui.Game
import rougelite.ui.Game.Companion.SCALE
import rougelite.utils.FileOperations
import kotlin.collections.HashMap

@JsonAdapter(EntitySerializer::class)
class Entity(d: EntityData, prototype: Prototype) {
    private var lastMove: Direction? = null
    private val type: String = d.type
    private val x = d.x
    private val y = d.y
    var width = d.width
    var height = d.height
    private val actionMap: HashMap<String, String> = prototype.actionMap
    private val inventory = mutableListOf<Entity>()
    @Transient var hitbox: Rectangle = Rectangle(
        (x * SCALE).toDouble(),
        (y * SCALE).toDouble(),
        (width ?: prototype.width) * SCALE.toDouble(),
        (height ?: prototype.width) * SCALE.toDouble())
        .apply {
            this.id = type
            this.fill = FileOperations.loadSprite(prototype.graphics ?: "$type.png")
        }
    var movable: Boolean = prototype.movable
    var passable: Boolean = prototype.passable

    fun movementAction(direction: Direction) {
        if (type == "player") move(direction)
        if (type == "mob") move(Direction.random())
    }

    fun collide(collidee: Entity): Boolean {
        val collisionBox = Shape.intersect(hitbox, collidee.hitbox)
        return collisionBox.boundsInLocal.width != -1.0
    }

    fun collisionAction(collidee: Entity): String {
        return actionMap.getOrDefault(collidee.type, "")
    }

    fun move(direction: Direction) {
        lastMove = direction
        Logger.trace("Entity `$type` moving in direction: `$direction`")
        when (direction) {
            Direction.DOWN -> hitbox.translateY = hitbox.translateY + Game.SCALE
            Direction.RIGHT -> hitbox.translateX = hitbox.translateX + Game.SCALE
            Direction.UP -> hitbox.translateY = hitbox.translateY - Game.SCALE
            Direction.LEFT -> hitbox.translateX = hitbox.translateX - Game.SCALE
        }
    }

    fun undoMove() {
        lastMove?.reverse()?.let { move(it) }
    }

    fun pickUp(entity: Entity) {
        inventory.add(entity)
    }

    fun hasItem(type: String): Boolean {
        return inventory.firstOrNull { entity -> entity.type == type } is Entity
    }

    fun useItem(type: String) {
        inventory.remove(inventory.first { entity -> entity.type == type })
    }
}
