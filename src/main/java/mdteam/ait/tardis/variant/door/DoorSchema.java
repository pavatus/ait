package mdteam.ait.tardis.variant.door;

import com.google.gson.*;
import mdteam.ait.registry.DoorRegistry;
import mdteam.ait.tardis.exterior.variant.ExteriorVariantSchema;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.Type;

// todo add @Environment(value = EnvType.CLIENT) to client-only stuff if stuff starts crashing

/**
 * This class provides information about a door for an exterior
 * <br><br>
 * It's information should be final and set once during creation.
 * <br><br>
 * It should be registered in {@link DoorRegistry#REGISTRY} and only obtained from there.
 * <br><br>
 * This should be referenced by a {@link ExteriorVariantSchema} to be used
 * @see DoorRegistry#REGISTRY
 * @author duzo
 */
public abstract class DoorSchema {
    private final Identifier id;

    protected DoorSchema(Identifier id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() == null) return false;

        DoorSchema that = (DoorSchema) o;

        return id.equals(that.id);
    }

    public Identifier id() { return id; }
    public abstract boolean isDouble();

    // fixme should this be in a "DoorSounds" type thing, also i dont like these method names.
    public SoundEvent openSound() {
        return SoundEvents.BLOCK_WOODEN_DOOR_CLOSE;
    }

    public SoundEvent closeSound() {
        return SoundEvents.BLOCK_WOODEN_DOOR_OPEN;
    }

    public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
        return pos; // just cus some dont have portals
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

            return DoorRegistry.REGISTRY.get(id);
        }

        @Override
        public JsonElement serialize(DoorSchema src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.id().toString());
        }
    }
}
