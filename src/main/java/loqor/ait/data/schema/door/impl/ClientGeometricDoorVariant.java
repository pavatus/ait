package loqor.ait.data.schema.door.impl;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.GeometricDoorModel;
import loqor.ait.data.schema.door.ClientDoorSchema;

public class ClientGeometricDoorVariant extends ClientDoorSchema {
    public ClientGeometricDoorVariant() {
        super(GeometricDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new GeometricDoorModel(GeometricDoorModel.getTexturedModelData().createModel());
    }
}
