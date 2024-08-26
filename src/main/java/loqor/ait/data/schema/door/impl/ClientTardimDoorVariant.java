package loqor.ait.data.schema.door.impl;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.TardimDoorModel;
import loqor.ait.data.schema.door.ClientDoorSchema;

public class ClientTardimDoorVariant extends ClientDoorSchema {
    public ClientTardimDoorVariant() {
        super(TardimDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new TardimDoorModel(TardimDoorModel.getTexturedModelData().createModel());
    }
}
