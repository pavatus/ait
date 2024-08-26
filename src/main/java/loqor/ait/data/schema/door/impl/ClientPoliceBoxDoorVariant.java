package loqor.ait.data.schema.door.impl;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.PoliceBoxDoorModel;
import loqor.ait.data.schema.door.ClientDoorSchema;

public class ClientPoliceBoxDoorVariant extends ClientDoorSchema {
    public ClientPoliceBoxDoorVariant() {
        super(PoliceBoxDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new PoliceBoxDoorModel(PoliceBoxDoorModel.getTexturedModelData().createModel());
    }
}
