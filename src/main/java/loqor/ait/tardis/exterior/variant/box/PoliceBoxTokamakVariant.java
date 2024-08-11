package loqor.ait.tardis.exterior.variant.box;

import net.minecraft.util.math.Vec3d;

import loqor.ait.core.data.schema.door.DoorSchema;
import loqor.ait.registry.impl.door.DoorRegistry;
import loqor.ait.tardis.door.PoliceBoxTokamakDoorVariant;

public class PoliceBoxTokamakVariant extends PoliceBoxVariant {
    public PoliceBoxTokamakVariant() {
        super("tokamak");
    }

    @Override
    public DoorSchema door() {
        return DoorRegistry.REGISTRY.get(PoliceBoxTokamakDoorVariant.REFERENCE);
    }

    @Override
    public Vec3d adjustPortalPos(Vec3d pos, byte direction) {
        /*
         * return switch(direction) { case DOWN, UP -> pos; case NORTH ->
         * pos.add(0,0.207,-0.63f); case SOUTH -> pos.add(0,0.207,0.63f); case WEST ->
         * pos.add(-0.63f,0.207,0); case EAST -> pos.add(0.63f,0.207,0); };
         */
        return super.adjustPortalPos(pos, direction);
    }
}
