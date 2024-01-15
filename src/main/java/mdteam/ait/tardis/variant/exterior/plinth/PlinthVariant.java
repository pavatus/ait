package mdteam.ait.tardis.variant.exterior.plinth;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.registry.DoorRegistry;
import mdteam.ait.tardis.animation.ExteriorAnimation;
import mdteam.ait.tardis.animation.PulsatingAnimation;
import mdteam.ait.tardis.exterior.CapsuleExterior;
import mdteam.ait.tardis.exterior.PlinthExterior;
import mdteam.ait.tardis.variant.door.CapsuleDoorVariant;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.variant.door.PlinthDoorVariant;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class PlinthVariant extends ExteriorVariantSchema {
    private final String name;
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/plinth/plinth_";

    protected PlinthVariant(String name, String modId) { // idk why i added the modid bit i dont use it later lol
        super(PlinthExterior.REFERENCE, new Identifier(modId, "exterior/plinth/" + name));

        this.name = name;
    }
    protected PlinthVariant(String name) {
        this(name, AITMod.MOD_ID);
    }

    @Override
    public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
        return new PulsatingAnimation(exterior);
    }

    @Override
    public DoorSchema door() {
        return DoorRegistry.REGISTRY.get(PlinthDoorVariant.REFERENCE);
    }

    @Override
    public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
        return switch (direction) {
            case DOWN, UP -> pos;
            case NORTH -> pos.add(0,0.4875,-0.4);
            case SOUTH -> pos.add(0,0.4875,0.4);
            case WEST -> pos.add(-0.4,0.4875,0);
            case EAST -> pos.add(0.4,0.4875,0);
        };
    }

    @Override
    public double portalHeight() {
        return 2.7d;
    }

    @Override
    public double portalWidth() {
        return 0.75d;
    }
}