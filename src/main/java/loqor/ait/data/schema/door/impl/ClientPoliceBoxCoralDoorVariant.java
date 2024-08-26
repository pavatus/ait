package loqor.ait.data.schema.door.impl;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.PoliceBoxCoralDoorModel;
import loqor.ait.data.schema.door.ClientDoorSchema;

public class ClientPoliceBoxCoralDoorVariant extends ClientDoorSchema {
    public ClientPoliceBoxCoralDoorVariant() {
        super(PoliceBoxCoralDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new PoliceBoxCoralDoorModel(PoliceBoxCoralDoorModel.getTexturedModelData().createModel());
    }
}
