package loqor.ait.core.item.sonic;

import com.google.gson.*;
import loqor.ait.registry.ExteriorVariantRegistry;
import loqor.ait.registry.SonicRegistry;
import loqor.ait.registry.datapack.Identifiable;
import loqor.ait.tardis.exterior.variant.ExteriorVariantSchema;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.lang.reflect.Type;

public abstract class SonicSchema implements Identifiable {

    private final Identifier id;
    private final String name;
    private final int model;

    protected SonicSchema(Identifier id, String name, int model) {
        this.id = id;
        this.name = name;
        this.model = model;
    }

    @Override
    public Identifier id() {
        return id;
    }

    public String name() {
        return name;
    }

    public int model() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null)
            return false;

        return o instanceof SonicSchema that && id.equals(that.id);
    }

    public static Object serializer() {
        return new Serializer();
    }

    private static class Serializer implements JsonSerializer<ExteriorVariantSchema>, JsonDeserializer<ExteriorVariantSchema> {

        @Override
        public ExteriorVariantSchema deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Identifier id;

            try {
                id = new Identifier(json.getAsJsonPrimitive().getAsString());
            } catch (InvalidIdentifierException e) {
                id = SonicRegistry.PRIME.id();
            }

            return ExteriorVariantRegistry.getInstance().get(id);
        }

        @Override
        public JsonElement serialize(ExteriorVariantSchema src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.id().toString());
        }
    }
}
