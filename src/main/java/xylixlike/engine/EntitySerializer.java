package xylixlike.engine;

import static xylixlike.utils.FileOperations.loadProperties;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import java.lang.reflect.Type;

public class EntitySerializer implements JsonDeserializer<Entity> {
    @Override
    public Entity deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) {
        EntityData data = jdc.deserialize(je, EntityData.class);
        Entity entity;
        if (data.width == null || data.height == null) {
            entity = new Entity(data.type, data.x, data.y);
        } else {
            entity = new Entity(data.type, data.x, data.y, data.width, data.height);
        }

        entity.setProperties(loadProperties(data.type));
        return entity;
    }

    private class EntityData {
        String type;
        int x;
        int y;
        Integer width;
        Integer height;
    }
}

