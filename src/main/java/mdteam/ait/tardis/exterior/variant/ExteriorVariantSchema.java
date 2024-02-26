package mdteam.ait.tardis.exterior.variant;

import com.google.gson.*;
import mdteam.ait.AITMod;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.sounds.MatSound;
import mdteam.ait.registry.datapack.Identifiable;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.animation.ExteriorAnimation;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.registry.CategoryRegistry;
import mdteam.ait.registry.ExteriorVariantRegistry;
import mdteam.ait.tardis.exterior.category.ExteriorCategorySchema;
import mdteam.ait.tardis.variant.door.DoorSchema;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import java.lang.reflect.Type;

/**
 * A variant for a {@link ExteriorCategorySchema} which provides a model, texture, emission, {@link ExteriorAnimation} and {@link DoorSchema}
 * <br><br>
 * This should be registered in {@link ExteriorVariantRegistry}
 * <br><br>
 * This should <b>ONLY</b> be created once in registry, you should grab the class via {@link ExteriorVariantRegistry#get(Identifier)}, the identifier being this variants id variable.
 * <br><br>
 * It is recommended for implementations of this class to have a static "REFERENCE" {@link Identifier} variable which other things can use to get this from the {@link ExteriorVariantRegistry}
 * @see ExteriorVariantRegistry
 * @author duzo
 */
public abstract class ExteriorVariantSchema implements Identifiable {
    private final Identifier category;
    private final Identifier id;

    protected ExteriorVariantSchema(Identifier category, Identifier id) {
        this.category = category;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() == null) return false;

        ExteriorVariantSchema that = (ExteriorVariantSchema) o;

        return id.equals(that.id);
    }

    public MatSound getSound(TardisTravel.State state) {
        return switch (state) {
            case LANDED, CRASH -> AITSounds.LANDED_ANIM;
            case FLIGHT -> AITSounds.FLIGHT_ANIM;
            case DEMAT -> AITSounds.DEMAT_ANIM;
            case MAT -> AITSounds.MAT_ANIM;
        };
    }

    protected Identifier categoryId() { return this.category; }

    public ExteriorCategorySchema category() { return CategoryRegistry.getInstance().get(this.categoryId()); }
    public Identifier id() { return id; }

    /**
     * The bounding box for this exterior, will be used in {@link mdteam.ait.core.blocks.ExteriorBlock#getNormalShape(BlockState, BlockView, BlockPos)}
     * @return
     */
    public VoxelShape bounding(Direction dir) { return null; }

    public abstract ExteriorAnimation animation(ExteriorBlockEntity exterior);
    public abstract DoorSchema door();

    public boolean hasPortals() { return this.category().hasPortals(); }
    public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
        return pos; // just cus some dont have portals
    }
    public double portalWidth() {return 1d;}
    public double portalHeight() {return 2d;}

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

            return ExteriorVariantRegistry.getInstance().get(id);
        }

        @Override
        public JsonElement serialize(ExteriorVariantSchema src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.id().toString());
        }
    }
}