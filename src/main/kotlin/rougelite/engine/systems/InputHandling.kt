package rougelite.engine.systems

import javafx.scene.input.KeyEvent
import org.tinylog.kotlin.Logger
import rougelite.engine.Direction
import rougelite.engine.Entity
import rougelite.engine.Player
import rougelite.engine.Physics
import rougelite.engine.components.Body

class InputHandling (entities: List<Entity>){
    private val player: Player = entities.filterIsInstance<Player>().first()

    fun update(press: KeyEvent) {
        if (Direction.validDirection(press.code.name)) {
            val movementDirection = Direction.valueOf(press.code.toString())
            movementAction(player.body, movementDirection)
        } else {
            Logger.trace("Unbound keyboard input `${press.code.name}`")
        }
    }

    fun movementAction(body: Body, direction: Direction) {
        if (body.type == "player") move(body, direction)
    }
}
