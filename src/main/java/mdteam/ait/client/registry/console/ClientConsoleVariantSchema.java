package mdteam.ait.client.registry.console;

import com.google.gson.*;
import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.client.registry.ClientConsoleVariantRegistry;
import mdteam.ait.registry.ConsoleVariantRegistry;
import mdteam.ait.tardis.variant.console.ConsoleVariantSchema;
import mdteam.ait.tardis.variant.console.HartnellVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.lang.reflect.Type;

@Environment(EnvType.CLIENT)
public abstract class ClientConsoleVariantSchema {
    private final Identifier parent;
    private final Identifier id;

    protected ClientConsoleVariantSchema(Identifier parent, Identifier id) {
        this.parent = parent;
        this.id = id;
    }
    protected ClientConsoleVariantSchema(Identifier parent) {
        this.id = parent;
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() == null) return false;

        ClientConsoleVariantSchema that = (ClientConsoleVariantSchema) o;

        return id.equals(that.id);
    }

    public ConsoleVariantSchema parent() { return ConsoleVariantRegistry.REGISTRY.get(this.parent); }
    public Identifier id() { return id; }
    public abstract Identifier texture();
    public abstract Identifier emission();
    @Environment(EnvType.CLIENT)
    public abstract ConsoleModel model();

    public static Object serializer() {
        return new Serializer();
    }

    private static class Serializer implements JsonSerializer<ClientConsoleVariantSchema>, JsonDeserializer<ClientConsoleVariantSchema> {

        @Override
        public ClientConsoleVariantSchema deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Identifier id;

            try {
                id = new Identifier(json.getAsJsonPrimitive().getAsString());
            } catch (InvalidIdentifierException e) {
                id = HartnellVariant.REFERENCE;
            }

            return ClientConsoleVariantRegistry.REGISTRY.get(id);
        }

        @Override
        public JsonElement serialize(ClientConsoleVariantSchema src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.id().toString());
        }
    }
}
