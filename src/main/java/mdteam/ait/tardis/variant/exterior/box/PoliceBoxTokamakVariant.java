package mdteam.ait.tardis.variant.exterior.box;

import mdteam.ait.core.AITSounds;
import mdteam.ait.core.sounds.MatSound;
import mdteam.ait.registry.DoorRegistry;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.variant.door.PoliceBoxCoralDoorVariant;
import mdteam.ait.tardis.variant.door.PoliceBoxTokamakDoorVariant;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class PoliceBoxTokamakVariant extends PoliceBoxVariant {
    public PoliceBoxTokamakVariant() {
        super("tokamak");
    }

    @Override
    public DoorSchema door() {
        return DoorRegistry.REGISTRY.get(PoliceBoxTokamakDoorVariant.REFERENCE);
    }

    @Override
    public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
        return super.adjustPortalPos(pos, direction);
    }

    @Override
    public MatSound getSound(TardisTravel.State state) {
        if (state == TardisTravel.State.MAT) return AITSounds.GHOST_MAT_ANIM;
        else return super.getSound(state);
    }
}
