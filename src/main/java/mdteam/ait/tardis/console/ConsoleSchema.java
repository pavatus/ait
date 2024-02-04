package mdteam.ait.tardis.console;

import com.google.gson.*;
import mdteam.ait.registry.ConsoleRegistry;
import mdteam.ait.tardis.control.ControlTypes;
import mdteam.ait.tardis.exterior.CapsuleCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.lang.reflect.Type;

public abstract class ConsoleSchema {
    private final Identifier id;
    private final String name;

    protected ConsoleSchema(Identifier id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() == null) return false;

        ConsoleSchema that = (ConsoleSchema) o;

        return id.equals(that.id);
    }

    public Identifier id() {
        return this.id;
    }
    public String name() { return this.name; }

    @Override
    public String toString() {
        return this.name();
    }

    //@TODO protocol abstraction with numbered letters

    public abstract ControlTypes[] getControlTypes(); // fixme this kinda sucks idk

    public static Object serializer() {
        return new Serializer();
    }

    private static class Serializer implements JsonSerializer<ConsoleSchema>, JsonDeserializer<ConsoleSchema> {

        @Override
        public ConsoleSchema deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Identifier id;

            try {
                id = new Identifier(json.getAsJsonPrimitive().getAsString());
            } catch (InvalidIdentifierException e) {
                id = CapsuleCategory.REFERENCE;
            }

            return ConsoleRegistry.REGISTRY.get(id);
        }

        @Override
        public JsonElement serialize(ConsoleSchema src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.id().toString());
        }
    }
}
