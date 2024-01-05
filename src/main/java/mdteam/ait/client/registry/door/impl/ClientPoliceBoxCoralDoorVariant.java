package mdteam.ait.client.registry.door.impl;

import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.models.doors.PoliceBoxCoralDoorModel;
import mdteam.ait.client.models.doors.PoliceBoxDoorModel;
import mdteam.ait.client.registry.door.ClientDoorSchema;
import mdteam.ait.tardis.variant.door.PoliceBoxCoralDoorVariant;
import mdteam.ait.tardis.variant.door.PoliceBoxDoorVariant;

public class ClientPoliceBoxCoralDoorVariant extends ClientDoorSchema {
    public ClientPoliceBoxCoralDoorVariant() {
        super(PoliceBoxCoralDoorVariant.REFERENCE);
    }
    @Override
    public DoorModel model() {
        return new PoliceBoxCoralDoorModel(PoliceBoxCoralDoorModel.getTexturedModelData().createModel());
    }
}
