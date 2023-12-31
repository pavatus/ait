package mdteam.ait.tardis.variant.exterior.classic;

import mdteam.ait.AITMod;
import mdteam.ait.client.animation.ClassicAnimation;
import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.client.animation.PulsatingAnimation;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.registry.DoorRegistry;
import mdteam.ait.tardis.exterior.ClassicExterior;
import mdteam.ait.tardis.variant.door.ClassicDoorVariant;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class ClassicBoxVariant extends ExteriorVariantSchema {
    private final String name;
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/classic/classic_";

    protected ClassicBoxVariant(String name, String modId) { // idk why i added the modid bit i dont use it later lol
        super(ClassicExterior.REFERENCE, new Identifier(modId, "exterior/classic/" + name));

        this.name = name;
    }
    protected ClassicBoxVariant(String name) {
        this(name, AITMod.MOD_ID);
    }

    @Override
    public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
        return new PulsatingAnimation(exterior);
    }

    @Override
    public DoorSchema door() {
        return DoorRegistry.REGISTRY.get(ClassicDoorVariant.REFERENCE);
    }

    @Override
    public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
        return switch (direction) {
            case DOWN, UP -> pos;
            case NORTH -> pos.add(0,0.2,0.48);
            case SOUTH -> pos.add(0,0.2,-0.48);
            case WEST -> pos.add(0.48,0.2,0);
            case EAST -> pos.add(-0.48,0.2,0);
        };
    }

    @Override
    public double portalHeight() {
        return 2.4d;
    }

    @Override
    public double portalWidth() {
        return 1.2d;
    }
}