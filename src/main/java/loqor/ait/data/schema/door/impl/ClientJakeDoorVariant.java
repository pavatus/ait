package loqor.ait.data.schema.door.impl;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.GeometricDoorModel;
import loqor.ait.data.schema.door.ClientDoorSchema;

public class ClientJakeDoorVariant extends ClientDoorSchema {
    public ClientJakeDoorVariant() {
        super(JakeDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new GeometricDoorModel(GeometricDoorModel.getTexturedModelData().createModel());
    }
}
