package rougelite.engine.components

import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import org.tinylog.kotlin.Logger
import rougelite.engine.*
import rougelite.ui.Game
import rougelite.utils.FileOperations

class Physics(data: EntityData, prototype : Prototype) {
    private val type = data.type
    private val x = data.x
    private val y = data.y
    private var lastMove: Direction? = null

    var width = data.width
    var height = data.height

    var passable: Boolean = prototype.passable

    var hitbox: Rectangle = Rectangle(
            (x * Game.SCALE).toDouble(),
            (y * Game.SCALE).toDouble(),
            (width ?: prototype.width) * Game.SCALE.toDouble(),
            (height ?: prototype.width) * Game.SCALE.toDouble())
            .apply {
                this.id = type
                this.fill = FileOperations.loadSprite(prototype.graphics ?: "$type.png")
            }


    fun movementAction(direction: Direction) {
        if (type == "player") move(direction)
        if (type == "mob") move(Direction.random())
    }

    fun collide(collidee: Physical): Boolean {
        val collisionBox = Shape.intersect(hitbox, collidee.physics.hitbox)
        return collisionBox.boundsInLocal.width != -1.0
    }

    fun collisionAction(collidee: Entity): String {
        return actionMap.getOrDefault(collidee.type, "")
    }

    private fun move(direction: Direction) {
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
}
