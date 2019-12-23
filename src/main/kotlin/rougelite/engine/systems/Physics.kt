package rougelite.engine.systems

import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import org.tinylog.kotlin.Logger
import rougelite.engine.Direction
import rougelite.engine.Entity
import rougelite.engine.Physical
import rougelite.engine.components.Body
import rougelite.ui.Game

class Physics (entities: List<Entity>){
    private val physicsObjects: List<Physical>  = entities.filterIsInstance<Physical>()

    fun update() {
        collisionHandle()
    }

    private fun collisionHandle() {
        physicsObjects.filter(Physical::movable).forEach { collider ->
            physicsObjects.filter { collided -> collides(collider, collided) }.forEach { collided ->
                if (collider !== collided) handleAction(collider, collided)
            }
        }
    }

    private fun handleAction(collider: Entity, collided: Entity) {
        val action = collisionAction(collider, collided)
        if (!action.isBlank()) {
            Logger.trace(action)
        }
        when (action) {
            "loss" -> lost = true
            "pickup" -> collider.inventory.pickUp(collided)
            "victory" -> {
                Logger.info("You're winner!")
                won = true
            } "try-open" -> {
                if (collider.inventory.hasItem("key")) {
                    collided.passable = true
                    collided.hitbox.fill = Color.TRANSPARENT
                    collider.inventory.useItem("key")
                    Logger.trace("Successful open")
                }
            }
        }
        if (!collided.passable) collider.undoMove()
    }

    fun collisionAction(collider: Entity, collided: Entity): String {
        collider.actionMap.get(collided.type)
    }

    fun collides(collider: Physical, collided: Physical): Boolean {
        val collisionBox = Shape.intersect(collider.body.hitbox, collided.body.hitbox)
        return collisionBox.boundsInLocal.width != -1.0
    }

    private fun move(body : Body, direction: Direction) {
        Logger.trace("Entity `${body.type}` moving in direction: `$direction`")
        body.lastMove = direction
        val hitbox = body.hitbox
        when (direction) {
            Direction.DOWN -> hitbox.translateY = hitbox.translateY + Game.SCALE
            Direction.RIGHT -> hitbox.translateX = hitbox.translateX + Game.SCALE
            Direction.UP -> hitbox.translateY = hitbox.translateY - Game.SCALE
            Direction.LEFT -> hitbox.translateX = hitbox.translateX - Game.SCALE
        }
    }

    fun undoMove(body : Body) {
        body.lastMove?.reverse()?.let { move(body, it) }
    }

    fun reset() {
        physicsObjects.forEach { e: Physical ->
            e.body.hitbox.translateX = 0.0
            e.body.hitbox.translateY = 0.0
        }
    }

    val hitboxes: List<Rectangle>
        get() = physicsObjects.map { it.body.hitbox }.toList()

}
