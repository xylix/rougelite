package rougelite.engine

import com.google.gson.annotations.JsonAdapter
import org.tinylog.kotlin.Logger
import rougelite.engine.components.Interaction
import rougelite.engine.components.Inventory
import rougelite.engine.components.Body

fun buildEntity(data: EntityData, prototype: EntityPrototype): Entity? {
    return when (data.type) {
        "player" -> Player(
                type = "player",
                body = Body(data, prototype),
                inventory = Inventory(),
                interaction = Interaction(prototype.actionMap)
        )
        "mob" -> Mob(type = "mob", body = Body(data, prototype), interaction = Interaction(prototype.actionMap))
        "wall" -> Tile(type = "tile", body = Body(data, prototype))
        else -> {
            Logger.error("Invalid type: $data.type")
            null
        }
    }
}

@JsonAdapter(EntitySerializer::class)
sealed class Entity(val type: String)

interface Physical {
    val body: Body
    val movable: Boolean
}

class Player(
    type: String,
    override val body: Body,
    override val movable: Boolean = true,
    val inventory: Inventory,
    val interaction: Interaction
) : Entity(type), Physical

class Mob(
    type: String,
    override val body: Body,
    override val movable: Boolean = true,
    interaction: Interaction
) : Entity(type), Physical

class Tile(
    type: String,
    override val body: Body,
    override val movable: Boolean = false
) : Entity(type), Physical
