package loqor.ait.data.schema.door.impl;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.PipeDoorModel;
import loqor.ait.data.schema.door.ClientDoorSchema;

public class ClientPipeDoorVariant extends ClientDoorSchema {
    public ClientPipeDoorVariant() {
        super(PipeDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new PipeDoorModel(PipeDoorModel.getTexturedModelData().createModel());
    }
}
