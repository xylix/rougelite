package rougelite.engine.components

import javafx.scene.shape.Rectangle
import rougelite.engine.Direction
import rougelite.ui.Game
import rougelite.utils.FileOperations
import rougelite.engine.EntityPrototype
import rougelite.engine.EntityData

class Body(data: EntityData, prototype: EntityPrototype) {
    val type = data.type
    val x = data.x
    val y = data.y
    var lastMove: Direction? = null

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
}
