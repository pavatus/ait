package mdteam.ait.tardis.exterior;

import com.google.gson.*;
import mdteam.ait.registry.CategoryRegistry;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.lang.reflect.Type;

/**
 * An exterior which other classes reference ( eg {@link ExteriorVariantSchema} )
 * <br><br>
 * It has an {@link Identifier} which can be used to get it from the {@link CategoryRegistry#REGISTRY} registry
 * <br><br>
 * Ensure your exterior is registered in {@link CategoryRegistry#REGISTRY}, otherwise it won't be recognised nor obtainable.
 * <br><br>
 * Only create this class once during registry, and only get it from {@link CategoryRegistry#REGISTRY#get(Identifier)} with identifier being {@link #id}
 * <br><br>
 * It is recommended for implementations of this class to have a static "REFERENCE" {@link Identifier} variable which other things can use to get this from the {@link CategoryRegistry#REGISTRY}
 * <br><br>
 * This class has a {@link #name} in lowercase text seperated by underscores which can be formatted for usage.
 * <br><br>
 * To be visible in-game, this class requires one implementation of {@link ExteriorVariantSchema} to be registered with this as its parent.
 * @see CategoryRegistry#REGISTRY
 * @author duzo
 */
public abstract class ExteriorCategory {
    private final Identifier id;
    private final String name;

    protected ExteriorCategory(Identifier id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() == null) return false;

        ExteriorCategory that = (ExteriorCategory) o;

        return id.equals(that.id);
    }

    public Identifier id() {
        return this.id;
    }
    public String name() { return this.name; }
    @Deprecated // Replace with the exteriors own hasPortals method, they need to override it
    public boolean hasPortals() {return false;}

    @Override
    public String toString() {
        return this.name();
    }

    public static Object serializer() {
        return new Serializer();
    }

    private static class Serializer implements JsonSerializer<ExteriorCategory>, JsonDeserializer<ExteriorCategory> {

        @Override
        public ExteriorCategory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Identifier id;

            try {
                id = new Identifier(json.getAsJsonPrimitive().getAsString());
            } catch (InvalidIdentifierException e) {
                id = CapsuleCategory.REFERENCE;
            }

            return CategoryRegistry.REGISTRY.get(id);
        }

        @Override
        public JsonElement serialize(ExteriorCategory src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.id().toString());
        }
    }
}
