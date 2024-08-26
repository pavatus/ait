package loqor.ait.data.schema.door.impl;

import loqor.ait.client.models.doors.CapsuleDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.data.schema.door.ClientDoorSchema;

public class ClientAdaptiveDoorVariant extends ClientDoorSchema {
    public ClientAdaptiveDoorVariant() {
        super(AdaptiveDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new CapsuleDoorModel(CapsuleDoorModel.getTexturedModelData().createModel());
    }
}
