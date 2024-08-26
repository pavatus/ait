package loqor.ait.data.schema.door;

import java.lang.reflect.Type;

import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.registry.impl.door.ClientDoorRegistry;
import loqor.ait.registry.impl.door.DoorRegistry;
import loqor.ait.tardis.door.CapsuleDoorVariant;

@Environment(EnvType.CLIENT)
public abstract class ClientDoorSchema {
    private final Identifier parent;
    private final Identifier id;

    protected ClientDoorSchema(Identifier parent, Identifier id) {
        this.parent = parent;
        this.id = id;
    }

    protected ClientDoorSchema(Identifier parent) {
        this.id = parent;
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() == null)
            return false;

        ClientDoorSchema that = (ClientDoorSchema) o;

        return id.equals(that.id);
    }

    public DoorSchema parent() {
        return DoorRegistry.REGISTRY.get(this.parent);
    }

    public Identifier id() {
        return id;
    }

    // public abstract Identifier texture();
    // public abstract Identifier emission();
    public abstract DoorModel model();

    public static Object serializer() {
        return new Serializer();
    }

    private static class Serializer implements JsonSerializer<ClientDoorSchema>, JsonDeserializer<ClientDoorSchema> {

        @Override
        public ClientDoorSchema deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            Identifier id;

            try {
                id = new Identifier(json.getAsJsonPrimitive().getAsString());
            } catch (InvalidIdentifierException e) {
                id = CapsuleDoorVariant.REFERENCE;
            }

            return ClientDoorRegistry.REGISTRY.get(id);
        }

        @Override
        public JsonElement serialize(ClientDoorSchema src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.id().toString());
        }
    }
}
