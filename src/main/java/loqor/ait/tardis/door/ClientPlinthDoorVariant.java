package loqor.ait.tardis.door;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.PlinthDoorModel;
import loqor.ait.core.data.schema.door.ClientDoorSchema;

public class ClientPlinthDoorVariant extends ClientDoorSchema {
    public ClientPlinthDoorVariant() {
        super(PlinthDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new PlinthDoorModel(PlinthDoorModel.getTexturedModelData().createModel());
    }
}
