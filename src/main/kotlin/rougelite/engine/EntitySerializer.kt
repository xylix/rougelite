package rougelite.engine

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import rougelite.utils.FileOperations
import java.lang.reflect.Type

class EntitySerializer : JsonDeserializer<Entity> {
    override fun deserialize(je: JsonElement, type: Type, jdc: JsonDeserializationContext): Entity {
        val data = jdc.deserialize<EntityData>(je, EntityData::class.java)!!
        val prototype = FileOperations.loadProperties(data.type)
        return buildEntity(data, prototype)!!
    }
}
