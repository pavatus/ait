package mdteam.ait.client.registry.door.impl;

import mdteam.ait.client.models.doors.ClassicDoorModel;
import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.models.doors.PoliceBoxDoorModel;
import mdteam.ait.client.registry.door.ClientDoorSchema;
import mdteam.ait.tardis.variant.door.ClassicDoorVariant;
import mdteam.ait.tardis.variant.door.PoliceBoxDoorVariant;

public class ClientPoliceBoxDoorVariant extends ClientDoorSchema {
    public ClientPoliceBoxDoorVariant() {
        super(PoliceBoxDoorVariant.REFERENCE);
    }
    @Override
    public DoorModel model() {
        return new PoliceBoxDoorModel(PoliceBoxDoorModel.getTexturedModelData().createModel());
    }
}
