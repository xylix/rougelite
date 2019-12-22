package xylixlike.engine

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import xylixlike.utils.FileOperations.Companion.loadProperties
import java.lang.reflect.Type

class EntitySerializer : JsonDeserializer<Entity> {
    override fun deserialize(je: JsonElement, type: Type, jdc: JsonDeserializationContext): Entity {
        val data = jdc.deserialize<EntityData>(je, EntityData::class.java)
        val entity: Entity
        entity = if (data.width == null || data.height == null) {
            Entity(data.type!!, data.x, data.y)
        } else {
            Entity(data.type!!, data.x, data.y, data.width!!, data.height!!)
        }
        entity.setProperties(loadProperties(data.type!!))
        return entity
    }

    private inner class EntityData {
        var type: String? = null
        var x = 0
        var y = 0
        var width: Int? = null
        var height: Int? = null
    }
}