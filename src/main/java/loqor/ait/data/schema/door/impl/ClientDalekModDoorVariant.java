package loqor.ait.data.schema.door.impl;

import loqor.ait.client.models.doors.DalekModDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.data.schema.door.ClientDoorSchema;

public class ClientDalekModDoorVariant extends ClientDoorSchema {
    public ClientDalekModDoorVariant() {
        super(DalekModDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new DalekModDoorModel(DalekModDoorModel.getTexturedModelData().createModel());
    }
}
