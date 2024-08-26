package loqor.ait.data.schema.door.impl;

import loqor.ait.client.models.doors.CapsuleDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.data.schema.door.ClientDoorSchema;

public class ClientCapsuleDoorVariant extends ClientDoorSchema {
    public ClientCapsuleDoorVariant() {
        super(CapsuleDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new CapsuleDoorModel(CapsuleDoorModel.getTexturedModelData().createModel());
    }
}
