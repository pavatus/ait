package mdteam.ait.tardis.variant.exterior;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.sounds.MatSound;
import mdteam.ait.registry.DoorRegistry;
import mdteam.ait.registry.ExteriorVariantRegistry;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.animation.ExteriorAnimation;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.variant.exterior.box.PoliceBoxDefaultVariant;
import mdteam.ait.tardis.variant.exterior.box.PoliceBoxVariant;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;

public class DatapackVariant extends ExteriorVariantSchema {
    public static final Identifier DEFAULT_TEXTURE = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/desktop/missing_preview.png");

    protected final Identifier parent;
    protected final Identifier texture;
    protected final Identifier emission;
    protected boolean initiallyDatapack;

    public static final Codec<DatapackVariant> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(ExteriorVariantSchema::id),
                    Identifier.CODEC.fieldOf("category").forGetter(ExteriorVariantSchema::categoryId),
                    Identifier.CODEC.fieldOf("parent").forGetter(DatapackVariant::getParentId),
                    Identifier.CODEC.fieldOf("texture").forGetter(DatapackVariant::texture),
                    Identifier.CODEC.fieldOf("emission").forGetter(DatapackVariant::emission),
                    Codec.BOOL.optionalFieldOf("isDatapack", true).forGetter(DatapackVariant::wasDatapack)
            ).apply(instance, DatapackVariant::new));

//    public static final Codec<ExteriorVariantSchema> CODEC_DEFAULT = RecordCodecBuilder.create(
//            instance -> instance.group(
//                    Identifier.CODEC.fieldOf("id").forGetter(ExteriorVariantSchema::id),
//                    Identifier.CODEC.fieldOf("category").forGetter(ExteriorVariantSchema::categoryId)
//            ).apply(instance, DatapackVariant::new)
//    );

    public DatapackVariant(Identifier id, Identifier category, Identifier parent, Identifier texture, Identifier emission, boolean isDatapack) {
        super(category, id);
        this.parent = parent;
        this.texture = texture;
        this.emission = emission;
        this.initiallyDatapack = isDatapack;
    }
    public DatapackVariant(Identifier id, Identifier category, Identifier parent, Identifier texture, Identifier emission) {
        this(id, category, parent, texture, emission, true);
    }

    public static DatapackVariant fromInputStream(InputStream stream) {
        return fromJson(JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject());
    }
    public static DatapackVariant fromJson(JsonObject json) {
        AtomicReference<DatapackVariant> created = new AtomicReference<>();

        CODEC.decode(JsonOps.INSTANCE, json)
                .get()
                .ifLeft(desktop -> { created.set((DatapackVariant) desktop.getFirst()); })
                .ifRight(err -> { created.set(null);
                    AITMod.LOGGER.error("Error decoding datapack desktop: " + err);
                });

        return created.get();
    }

    public ExteriorVariantSchema getParent() {
        // ExteriorVariantRegistry.getInstance().toList().forEach(schema -> System.out.println(schema.id()));
        return ExteriorVariantRegistry.getInstance().get(this.getParentId());
    }
    public Identifier getParentId() {
        return this.parent;
    }

    @Override
    public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
        return this.getParent().animation(exterior);
    }

    @Override
    public DoorSchema door() {
        System.out.println(this.getParent().id());
        return DoorRegistry.BOX;

        //return this.getParent().door();
    }

    @Override
    public MatSound getSound(TardisTravel.State state) {
        return this.getParent().getSound(state);
    }

    @Override
    public VoxelShape bounding(Direction dir) {
        return this.getParent().bounding(dir);
    }

    @Override
    public boolean hasPortals() {
        return this.getParent().hasPortals();
    }

    @Override
    public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
        return this.getParent().adjustPortalPos(pos, direction);
    }

    @Override
    public double portalWidth() {
        return this.getParent().portalWidth();
    }

    @Override
    public double portalHeight() {
        return this.getParent().portalHeight();
    }

    public boolean wasDatapack() {
        return this.initiallyDatapack;
    }
    public Identifier texture() {
        return this.texture;
    }

    public Identifier emission() {
        return this.emission;
    }
}
