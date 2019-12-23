package rougelite.engine

import com.google.gson.annotations.JsonAdapter
import org.tinylog.kotlin.Logger
import rougelite.engine.components.Interaction
import rougelite.engine.components.Inventory
import rougelite.engine.components.Physics

fun buildEntity(data : EntityData, prototype: Prototype): Entity? {
    return when (data.type) {
        "player" -> Player("player", Physics(data, prototype), Inventory(), Interaction(prototype.actionMap))
        "mob" -> Mob("mob", Physics(data, prototype), Interaction(prototype.actionMap))
        "wall" -> Tile("tile", Physics(data, prototype))
        else -> {
            Logger.error("Invalid type: $data.type")
            null
        }
    }
}

@JsonAdapter(EntitySerializer::class)
sealed class Entity(val type: String)

interface Physical {
    val physics : Physics
}

class Player(
    type: String,
    override val physics: Physics,
    val inventory: Inventory,
    val interaction: Interaction
) : Entity(type), Physical

class Mob(
    type: String,
    override val physics: Physics,
    interaction: Interaction
) : Entity(type), Physical

class Tile(type: String, override val physics: Physics) : Entity(type), Physical
