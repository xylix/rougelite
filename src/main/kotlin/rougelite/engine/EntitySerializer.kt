package rougelite.engine

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import rougelite.utils.FileOperations
import java.lang.reflect.Type

class EntitySerializer : JsonDeserializer<Entity> {
    override fun deserialize(je: JsonElement, type: Type, jdc: JsonDeserializationContext): Entity {
        val data = jdc.deserialize<EntityData>(je, EntityData::class.java)!!
        val props = FileOperations.loadProperties(data.type);
        return Entity(data, props)
    }
}

data class EntityData(
    var type: String,
    var x: Int,
    var y: Int,
    var width: Int?,
    var height: Int?
)
