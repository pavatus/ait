package mdteam.ait.tardis.variant.door;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.models.doors.PoliceBoxDoorModel;
import mdteam.ait.client.models.doors.TardimDoorModel;
import net.minecraft.util.Identifier;

public class PoliceBoxDoorVariant extends DoorSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "door_police_box");

    public PoliceBoxDoorVariant() {
        super(REFERENCE);
    }

    @Override
    public boolean isDouble() {
        return true;
    }

    @Override
    public DoorModel model() {
        return new PoliceBoxDoorModel(PoliceBoxDoorModel.getTexturedModelData().createModel());
    }
}
