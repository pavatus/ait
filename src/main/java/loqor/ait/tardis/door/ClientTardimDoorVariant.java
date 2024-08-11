package loqor.ait.tardis.door;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.TardimDoorModel;
import loqor.ait.core.data.schema.door.ClientDoorSchema;

public class ClientTardimDoorVariant extends ClientDoorSchema {
    public ClientTardimDoorVariant() {
        super(TardimDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new TardimDoorModel(TardimDoorModel.getTexturedModelData().createModel());
    }
}
