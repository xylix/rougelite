package rougelite.engine

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Pane
import rougelite.engine.systems.InputHandling
import rougelite.engine.systems.Physics

class Level private constructor() {
    private var won: Boolean
    private var lost: Boolean
    private val entities: List<Entity>
    private val physics: Physics
    private val inputHandling: InputHandling
    init {
        entities = ArrayList()
        won = false
        lost = false
        physics = Physics(entities)
        inputHandling = InputHandling(entities)
    }

    private fun update(press: KeyEvent) {
        inputHandling.update(press)
        physics.update()

        if (lost) {
            reset()
            return
        }
    }

    private fun reset() {
        physics.reset()
        lost = false
    }

    val scene: Scene
        get() {
            val pane = Pane()
            pane.children.addAll(physics.hitboxes)
            val scene = Scene(pane)
            scene.onKeyPressed = EventHandler { press: KeyEvent -> update(press) }
            return scene
        }
}
