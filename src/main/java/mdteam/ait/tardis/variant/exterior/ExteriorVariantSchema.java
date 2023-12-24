package mdteam.ait.tardis.variant.exterior;

import com.google.gson.*;
import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.AITDesktops;
import mdteam.ait.core.AITExteriorVariants;
import mdteam.ait.tardis.TardisDesktopSchema;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;

public abstract class ExteriorVariantSchema {
    private final ExteriorEnum parent;
    private final Identifier id;

    protected ExteriorVariantSchema(ExteriorEnum parent, Identifier id) {
        this.parent = parent;
        this.id = id;
    }

    public ExteriorEnum parent() { return parent; }
    public Identifier id() { return id; }

    public abstract Identifier texture();
    public abstract Identifier emission();

    public static Object serializer() {
        return new Serializer();
    }

    private static class Serializer implements JsonSerializer<ExteriorVariantSchema>, JsonDeserializer<ExteriorVariantSchema> {

        @Override
        public ExteriorVariantSchema deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.getAsJsonPrimitive().getAsString().equalsIgnoreCase("minecraft:DEFAULT"))
                return AITExteriorVariants.iterator().stream().findFirst().get(); // this should fix the crash classic gets when opening the pre-rewrite set world

            return AITExteriorVariants.get(new Identifier(json.getAsJsonPrimitive().getAsString()));
        }

        @Override
        public JsonElement serialize(ExteriorVariantSchema src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.id().toString());
        }
    }
}
