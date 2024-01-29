package mdteam.ait.client.registry.door.impl;

import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.models.doors.PlinthDoorModel;
import mdteam.ait.client.models.doors.TardimDoorModel;
import mdteam.ait.client.registry.door.ClientDoorSchema;
import mdteam.ait.tardis.variant.door.PlinthDoorVariant;
import mdteam.ait.tardis.variant.door.TardimDoorVariant;

public class ClientPlinthDoorVariant extends ClientDoorSchema {
    public ClientPlinthDoorVariant() {
        super(PlinthDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new PlinthDoorModel(PlinthDoorModel.getTexturedModelData().createModel());
    }
}
