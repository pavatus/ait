package mdteam.ait.tardis.exterior;

import com.google.gson.*;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.sounds.MatSound;
import mdteam.ait.registry.ExteriorRegistry;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.lang.reflect.Type;

/**
 * An exterior which other classes reference ( eg {@link ExteriorVariantSchema} )
 * <br><br>
 * It has an {@link Identifier} which can be used to get it from the {@link ExteriorRegistry#REGISTRY} registry
 * <br><br>
 * Ensure your exterior is registered in {@link ExteriorRegistry#REGISTRY}, otherwise it won't be recognised nor obtainable.
 * <br><br>
 * Only create this class once during registry, and only get it from {@link ExteriorRegistry#REGISTRY#get(Identifier)} with identifier being {@link #id}
 * <br><br>
 * It is recommended for implementations of this class to have a static "REFERENCE" {@link Identifier} variable which other things can use to get this from the {@link ExteriorRegistry#REGISTRY}
 * <br><br>
 * This class has a {@link #name} in lowercase text seperated by underscores which can be formatted for usage.
 * <br><br>
 * To be visible in-game, this class requires one implementation of {@link ExteriorVariantSchema} to be registered with this as its parent.
 * @see ExteriorRegistry#REGISTRY
 * @author duzo
 */
public abstract class ExteriorSchema {
    private final Identifier id;
    private final String name;

    protected ExteriorSchema(Identifier id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() == null) return false;

        ExteriorSchema that = (ExteriorSchema) o;

        return id.equals(that.id);
    }

    public Identifier id() {
        return this.id;
    }
    public String name() { return this.name; }
    public boolean hasPortals() {return false;}

    @Override
    public String toString() {
        return this.name();
    }

    public static Object serializer() {
        return new Serializer();
    }

    private static class Serializer implements JsonSerializer<ExteriorSchema>, JsonDeserializer<ExteriorSchema> {

        @Override
        public ExteriorSchema deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Identifier id;

            try {
                id = new Identifier(json.getAsJsonPrimitive().getAsString());
            } catch (InvalidIdentifierException e) {
                id = CapsuleExterior.REFERENCE;
            }

            return ExteriorRegistry.REGISTRY.get(id);
        }

        @Override
        public JsonElement serialize(ExteriorSchema src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.id().toString());
        }
    }
}
