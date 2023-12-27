package mdteam.ait.tardis.variant.exterior;

import com.google.gson.*;
import mdteam.ait.AITMod;
import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.tardis.ExteriorEnum;
import mdteam.ait.core.AITExteriorVariants;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.tardis.exterior.ExteriorSchema;
import mdteam.ait.tardis.variant.door.DoorSchema;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.lang.reflect.Type;

public abstract class ExteriorVariantSchema {
    private final ExteriorSchema parent;
    private final Identifier id;

    protected ExteriorVariantSchema(ExteriorSchema parent, Identifier id) {
        this.parent = parent;
        this.id = id;
    }

    public ExteriorSchema parent() { return parent; }
    public Identifier id() { return id; }

    public abstract Identifier texture();
    public abstract Identifier emission();
    public abstract ExteriorModel model();
    public abstract ExteriorAnimation animation(ExteriorBlockEntity exterior);
    public abstract DoorSchema door();

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
