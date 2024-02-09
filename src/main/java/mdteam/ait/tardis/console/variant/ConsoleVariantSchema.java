package mdteam.ait.tardis.console.variant;

import com.google.gson.*;
import mdteam.ait.AITMod;
import mdteam.ait.registry.ConsoleRegistry;
import mdteam.ait.registry.ConsoleVariantRegistry;
import mdteam.ait.registry.datapack.Identifiable;
import mdteam.ait.tardis.console.type.ConsoleTypeSchema;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.lang.reflect.Type;

/**
 * This class is for variants of a {@link ConsoleTypeSchema} and can be changed in game to the players desires
 * <br><br>
 * It's information should be final and set once on its creation during registration
 * <br><br>
 * It should be registered in {@link ConsoleVariantRegistry#REGISTRY} otherwise it wont show up in-game
 * <br><br>
 * It should only be gotten from {@link ConsoleVariantRegistry#REGISTRY#get(Identifier)} using its {@link #id} and only created once
 * <br><br>
 * @see ConsoleVariantRegistry#REGISTRY
 * @author duzo
 */
public abstract class ConsoleVariantSchema implements Identifiable {
    private final Identifier parent;
    private final Identifier id;

    protected ConsoleVariantSchema(Identifier parent, Identifier id) {
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

    protected Identifier parentId() { return this.parent; }
    public ConsoleTypeSchema parent() { return ConsoleRegistry.REGISTRY.get(this.parentId()); }
    public Identifier id() { return id; }

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

            return ConsoleVariantRegistry.getInstance().get(id);
        }

        @Override
        public JsonElement serialize(ConsoleVariantSchema src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.id().toString());
        }
    }
}