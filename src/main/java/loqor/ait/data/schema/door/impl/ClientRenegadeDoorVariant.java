package loqor.ait.data.schema.door.impl;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.RenegadeDoorModel;
import loqor.ait.data.schema.door.ClientDoorSchema;

public class ClientRenegadeDoorVariant extends ClientDoorSchema {
    public ClientRenegadeDoorVariant() {
        super(RenegadeDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new RenegadeDoorModel(RenegadeDoorModel.getTexturedModelData().createModel());
    }
}
