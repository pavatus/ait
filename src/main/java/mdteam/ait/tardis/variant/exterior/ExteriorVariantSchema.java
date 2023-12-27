package mdteam.ait.tardis.variant.exterior;

import com.google.gson.*;
import mdteam.ait.AITMod;
import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.core.AITExteriorVariants;
import mdteam.ait.core.AITExteriors;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.tardis.exterior.ExteriorSchema;
import mdteam.ait.tardis.variant.console.ConsoleVariantSchema;
import mdteam.ait.tardis.variant.door.DoorSchema;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import java.lang.reflect.Type;

/**
 * A variant for a {@link ExteriorSchema} which provides a model, texture, emission, {@link ExteriorAnimation} and {@link DoorSchema}
 * <br><br>
 * This should be registered in {@link AITExteriorVariants}
 * <br><br>
 * This should <b>ONLY</b> be created once in registry, you should grab the class via {@link AITExteriorVariants#get(Identifier)}, the identifier being this variants id variable.
 * <br><br>
 * It is recommended for implementations of this class to have a static "REFERENCE" {@link Identifier} variable which other things can use to get this from the {@link AITExteriorVariants}
 * @see AITExteriorVariants
 * @author duzo
 */
public abstract class ExteriorVariantSchema {
    private final Identifier parent;
    private final Identifier id;

    protected ExteriorVariantSchema(Identifier parent, Identifier id) {
        this.parent = parent;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() == null) return false;

        ExteriorVariantSchema that = (ExteriorVariantSchema) o;

        return id.equals(that.id);
    }


    public ExteriorSchema parent() { return AITExteriors.get(this.parent); }
    public Identifier id() { return id; }

    /**
     * The bounding box for this exterior, will be used in {@link mdteam.ait.core.blocks.ExteriorBlock#getNormalShape(BlockState, BlockView, BlockPos)}
     * @return
     */
    public VoxelShape bounding(Direction dir) { return null; }

    public abstract Identifier texture();
    public abstract Identifier emission();
    public abstract ExteriorModel model();
    public abstract ExteriorAnimation animation(ExteriorBlockEntity exterior);
    public abstract DoorSchema door();

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

            return AITExteriorVariants.get(id);
        }

        @Override
        public JsonElement serialize(ExteriorVariantSchema src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.id().toString());
        }
    }
}
