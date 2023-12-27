package mdteam.ait.tardis.variant.door;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.models.doors.TardimDoorModel;
import mdteam.ait.core.AITBlocks;
import net.minecraft.util.Identifier;

public class TardimDoorVariant extends DoorSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "door/tardim");

    public TardimDoorVariant() {
        super(REFERENCE);
    }

    @Override
    public boolean isDouble() {
        return true;
    }

    @Override
    public DoorModel model() {
        return new TardimDoorModel(TardimDoorModel.getTexturedModelData().createModel());
    }
}
