package loqor.ait.tardis.door;

import loqor.ait.client.models.doors.DoomDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.core.data.schema.door.ClientDoorSchema;

public class ClientDoomDoorVariant extends ClientDoorSchema {
    public ClientDoomDoorVariant() {
        super(DoomDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new DoomDoorModel(DoomDoorModel.getTexturedModelData().createModel());
    }
}
