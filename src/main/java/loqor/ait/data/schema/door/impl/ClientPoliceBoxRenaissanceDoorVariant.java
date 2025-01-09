package loqor.ait.data.schema.door.impl;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.PoliceBoxCoralDoorModel;
import loqor.ait.data.schema.door.ClientDoorSchema;

public class ClientPoliceBoxRenaissanceDoorVariant extends ClientDoorSchema {
    public ClientPoliceBoxRenaissanceDoorVariant() {
        super(PoliceBoxRenaissanceDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new PoliceBoxCoralDoorModel(PoliceBoxCoralDoorModel.getTexturedModelData().createModel());
    }
}
