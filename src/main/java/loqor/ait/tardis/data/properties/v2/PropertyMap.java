package loqor.ait.tardis.data.properties.v2;

import com.google.gson.*;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;
import java.util.HashMap;

public class PropertyMap extends HashMap<Identifier, Property<?>> {

    public static Object serializer() {
        return new Serializer();
    }

    private static class Serializer implements JsonSerializer<PropertyMap>, JsonDeserializer<PropertyMap> {

        @Override
        public PropertyMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            PropertyMap map = new PropertyMap();

            json.getAsJsonArray().forEach(elem -> {
                Property<?> property = context.deserialize(elem, Property.class);
                map.put(property.getId(), property);
            });

            return map;
        }

        @Override
        public JsonElement serialize(PropertyMap src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.values());
        }
    }
}
