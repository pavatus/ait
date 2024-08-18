package loqor.ait.tardis.door;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.StallionDoorModel;
import loqor.ait.core.data.schema.door.ClientDoorSchema;

public class ClientStallionDoorVariant extends ClientDoorSchema {
    public ClientStallionDoorVariant() {
        super(StallionDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new StallionDoorModel(StallionDoorModel.getTexturedModelData().createModel());
    }
}
