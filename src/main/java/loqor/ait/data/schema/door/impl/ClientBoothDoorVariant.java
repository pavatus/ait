package loqor.ait.data.schema.door.impl;

import loqor.ait.client.models.doors.BoothDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.data.schema.door.ClientDoorSchema;

public class ClientBoothDoorVariant extends ClientDoorSchema {
    public ClientBoothDoorVariant() {
        super(BoothDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new BoothDoorModel(BoothDoorModel.getTexturedModelData().createModel());
    }
}
