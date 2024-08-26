package loqor.ait.data.schema.door.impl;

import loqor.ait.client.models.coral.CoralGrowthDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.data.schema.door.ClientDoorSchema;

public class ClientGrowthDoorVariant extends ClientDoorSchema {
    public ClientGrowthDoorVariant() {
        super(CoralGrowthDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new CoralGrowthDoorModel(CoralGrowthDoorModel.getTexturedModelData().createModel());
    }
}
