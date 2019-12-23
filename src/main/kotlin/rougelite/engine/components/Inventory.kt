package rougelite.engine.components;

import rougelite.engine.Entity

class Inventory {
    private val contents = mutableListOf<Entity>()

    fun pickUp(entity: Entity) {
        contents.add(entity)
    }

    fun hasItem(type: String): Boolean {
        return contents.firstOrNull { entity -> entity.type == type } is Entity
    }

    fun useItem(type: String) {
        contents.remove(contents.first { entity -> entity.type == type })
    }
}
