package mdteam.ait.tardis.variant.exterior.box;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.animation.ExteriorAnimation;
import mdteam.ait.tardis.animation.PulsatingAnimation;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.registry.DoorRegistry;
import mdteam.ait.tardis.exterior.PoliceBoxCategory;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.variant.door.PoliceBoxDoorVariant;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class PoliceBoxVariant extends ExteriorVariantSchema {
    private final String name;
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/police_box/police_box_";

    protected PoliceBoxVariant(String name, String modId) { // idk why i added the modid bit i dont use it later lol
        super(PoliceBoxCategory.REFERENCE, new Identifier(modId, "exterior/police_box/" + name));

        this.name = name;
    }
    protected PoliceBoxVariant(String name) {
        this(name, AITMod.MOD_ID);
    }

    @Override
    public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
        return new PulsatingAnimation(exterior);
    }

    @Override
    public DoorSchema door() {
        return DoorRegistry.REGISTRY.get(PoliceBoxDoorVariant.REFERENCE);
    }

    @Override
    public boolean hasPortals() {
        return true;
    }

    @Override
    public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
        return switch (direction) {
            case DOWN, UP -> pos;
            case NORTH -> pos.add(0,0.207,-0.628);
            case SOUTH -> pos.add(0,0.207,0.628);
            case WEST -> pos.add(-0.628,0.207,0);
            case EAST -> pos.add(0.628,0.207,0);
        };
    }

    @Override
    public double portalHeight() {
        return 2.3d;
    }

    @Override
    public double portalWidth() {
        return 1.145d;
    }
}