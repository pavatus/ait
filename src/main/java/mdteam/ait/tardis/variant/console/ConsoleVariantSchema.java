package mdteam.ait.tardis.variant.console;

import com.google.gson.*;
import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.core.AITConsoleVariants;
import mdteam.ait.core.AITExteriors;
import mdteam.ait.tardis.exterior.CapsuleExterior;
import mdteam.ait.tardis.exterior.ExteriorSchema;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.lang.reflect.Type;

public abstract class ConsoleVariantSchema {
    private final ConsoleEnum parent;
    private final Identifier id;

    protected ConsoleVariantSchema(ConsoleEnum parent, Identifier id) {
        this.parent = parent;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() == null) return false;

        ConsoleVariantSchema that = (ConsoleVariantSchema) o;

        return id.equals(that.id);
    }

    public ConsoleEnum parent() { return parent; }
    public Identifier id() { return id; }

    public abstract Identifier texture();
    public abstract Identifier emission();

    public static Object serializer() {
        return new Serializer();
    }

    private static class Serializer implements JsonSerializer<ConsoleVariantSchema>, JsonDeserializer<ConsoleVariantSchema> {

        @Override
        public ConsoleVariantSchema deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Identifier id;

            try {
                id = new Identifier(json.getAsJsonPrimitive().getAsString());
            } catch (InvalidIdentifierException e) {
                id = new Identifier(AITMod.MOD_ID, "console/borealis");
            }

            return AITConsoleVariants.get(id);
        }

        @Override
        public JsonElement serialize(ConsoleVariantSchema src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.id().toString());
        }
    }
}
