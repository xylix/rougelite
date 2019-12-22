package rougelite.engine

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import org.tinylog.kotlin.Logger
import rougelite.engine.Direction.Companion.validDirection
import java.util.function.Consumer
import kotlin.streams.toList

class Level private constructor() {
    private var won: Boolean
    private var lost: Boolean
    private val entities: List<Entity>

    fun ifOver(action: Runnable) {
        if (won) {
            action.run()
        }
    }

    private fun tick(press: KeyEvent) {
        if (validDirection(press.code.name)) {
            val movementDirection = Direction.valueOf(press.code.toString())
            entities.forEach { it.movementAction(movementDirection) }
            collisionHandle()
        } else {
            Logger.trace("Unbound keyboard input `${press.code.name}`")
        }
        if (lost) {
            reset()
            return
        }
    }

    private fun reset() {
        entities.forEach(Consumer { e: Entity ->
            e.hitbox.translateX = 0.0
            e.hitbox.translateY = 0.0
        })
        lost = false
    }

    private fun collisionHandle() {
        entities.stream().filter(Entity::movable).forEach { collider ->
            entities.stream().filter { collidee -> collider.collide(collidee) }.forEach { collidee ->
                if (collider !== collidee) handleAction(collider, collidee)
            }
        }
    }

    private fun handleAction(collider: Entity, collidee: Entity) {
        val action = collider.collisionAction(collidee)
        if (!action.isBlank()) {
            Logger.trace(action)
        }
        when (action) {
            "hug" -> Logger.trace("Entity $collider hugged $collidee")
            "loss" -> lost = true
            "pickup" -> collider.pickUp(collidee)
            "victory" -> {
                Logger.info("You're winner!")
                won = true
            } "try-open" -> {
                if (collider.hasItem("key")) {
                    collidee.passable = true
                    collidee.hitbox.fill = Color.TRANSPARENT
                    collider.useItem("key")
                    Logger.trace("Successful open")
                }
            }
        }
        if (!collidee.passable) collider.undoMove()
    }

    private val hitboxes: List<Rectangle>
        get() = entities.stream().map { e: Entity -> e.hitbox }.toList()

    val scene: Scene
        get() {
            val pane = Pane()
            pane.children.addAll(hitboxes)
            val scene = Scene(pane)
            scene.onKeyPressed = EventHandler { press: KeyEvent -> tick(press) }
            return scene
        }

    init {
        entities = ArrayList()
        won = false
        lost = false
    }
}
