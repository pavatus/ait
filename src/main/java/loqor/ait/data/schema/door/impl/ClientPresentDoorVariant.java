package loqor.ait.data.schema.door.impl;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.PresentDoorModel;
import loqor.ait.data.schema.door.ClientDoorSchema;

public class ClientPresentDoorVariant extends ClientDoorSchema {
    public ClientPresentDoorVariant() {
        super(PresentDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new PresentDoorModel(PresentDoorModel.getTexturedModelData().createModel());
    }
}
