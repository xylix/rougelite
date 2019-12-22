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

    fun tick(press: KeyEvent) {
        if (validDirection(press.code.name)) {
            val movementDirection = Direction.valueOf(press.code.toString())
            getEntities().forEach(Consumer { actor: Entity -> actor.movementAction(movementDirection) })
            collisionHandle(movementDirection)
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

    private fun collisionHandle(direction: Direction) {
        val undoDirection = direction.reverse()
        entities.stream().filter { e: Entity -> e.movable }.forEach { collider: Entity ->
            entities.stream().filter { collidee: Entity? -> collider.collide(collidee!!) }.forEach { collidee: Entity ->
                val action = collider.collisionAction(collidee)
                handleAction(collider, collidee, action, undoDirection)
            }
        }
    }

    private fun handleAction(collider: Entity, collidee: Entity, action: String, undo: Direction) {
        if (!action.isBlank()) {
            Logger.trace(action)
        }
        if ("victory" == action) {
            println("You're winner!")
            won = true
        } else if ("loss" == action) {
            lost = true
        } else if ("open" == action) {
            collidee.passable = true
            collidee.hitbox.fill = Color.TRANSPARENT
            collider.movable = false
            collider.hitbox.fill = Color.TRANSPARENT
        } else if (!collidee.passable) {
            collider.move(undo)
        }
    }

    private val hitboxes: List<Rectangle>
        get() = entities.stream().map { e: Entity -> e.hitbox }.toList()

    fun getEntities(): List<Entity> {
        return entities
    }

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
