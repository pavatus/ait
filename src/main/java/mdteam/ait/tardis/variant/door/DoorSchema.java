package mdteam.ait.tardis.variant.door;

import com.google.gson.*;
import mdteam.ait.AITMod;
import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.core.AITDoors;
import mdteam.ait.core.AITExteriorVariants;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.lang.reflect.Type;

// todo add @Environment(value = EnvType.CLIENT) to client-only stuff if stuff starts crashing
public abstract class DoorSchema {
    private final Identifier id;

    protected DoorSchema(Identifier id) {
        this.id = id;
    }

    public Identifier id() { return id; }
    public abstract boolean isDouble();
    public abstract DoorModel model(); // fixme will have the same texture as the exterior, WILL cause problems if we want to use different textures on the door.

    // fixme should this be in a "DoorSounds" type thing, also i dont like these method names.
    public SoundEvent openSound() {
        return SoundEvents.BLOCK_WOODEN_DOOR_CLOSE;
    }

    public SoundEvent closeSound() {
        return SoundEvents.BLOCK_WOODEN_DOOR_OPEN;
    }

    public static Object serializer() {
        return new Serializer();
    }

    private static class Serializer implements JsonSerializer<DoorSchema>, JsonDeserializer<DoorSchema> {

        @Override
        public DoorSchema deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Identifier id;

            try {
                id = new Identifier(json.getAsJsonPrimitive().getAsString());
            } catch (InvalidIdentifierException e) {
                id = CapsuleDoorVariant.REFERENCE;
            }

            return AITDoors.get(id);
        }

        @Override
        public JsonElement serialize(DoorSchema src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.id().toString());
        }
    }
}
