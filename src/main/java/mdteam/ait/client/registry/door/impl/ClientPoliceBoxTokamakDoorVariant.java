package mdteam.ait.client.registry.door.impl;

import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.models.doors.PoliceBoxCoralDoorModel;
import mdteam.ait.client.registry.door.ClientDoorSchema;
import mdteam.ait.tardis.variant.door.PoliceBoxCoralDoorVariant;
import mdteam.ait.tardis.variant.door.PoliceBoxTokamakDoorVariant;

public class ClientPoliceBoxTokamakDoorVariant extends ClientDoorSchema {
    public ClientPoliceBoxTokamakDoorVariant() {
        super(PoliceBoxTokamakDoorVariant.REFERENCE);
    }
    @Override
    public DoorModel model() {
        return new PoliceBoxCoralDoorModel(PoliceBoxCoralDoorModel.getTexturedModelData().createModel());
    }
}
