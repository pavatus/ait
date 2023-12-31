package mdteam.ait.client.registry.door.impl;

import mdteam.ait.client.models.doors.CapsuleDoorModel;
import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.models.doors.TardimDoorModel;
import mdteam.ait.client.registry.door.ClientDoorSchema;
import mdteam.ait.tardis.variant.door.CapsuleDoorVariant;
import mdteam.ait.tardis.variant.door.TardimDoorVariant;

public class ClientTardimDoorVariant extends ClientDoorSchema {
    public ClientTardimDoorVariant() {
        super(TardimDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new TardimDoorModel(TardimDoorModel.getTexturedModelData().createModel());
    }
}
