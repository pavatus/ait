package mdteam.ait.tardis.variant.exterior;

import com.google.gson.*;
import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.AITDesktops;
import mdteam.ait.core.AITExteriorVariants;
import mdteam.ait.tardis.TardisDesktopSchema;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

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
            Identifier id;

            try {
                id = new Identifier(json.getAsJsonPrimitive().getAsString());
            } catch (InvalidIdentifierException e) {
                id = new Identifier(AITMod.MOD_ID, "capsule_default");
            }

            return AITExteriorVariants.get(id);
        }

        @Override
        public JsonElement serialize(ExteriorVariantSchema src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.id().toString());
        }
    }
}
