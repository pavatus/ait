package loqor.ait.tardis.door;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.EasterHeadDoorModel;
import loqor.ait.core.data.schema.door.ClientDoorSchema;

public class ClientEasterHeadDoorVariant extends ClientDoorSchema {
    public ClientEasterHeadDoorVariant() {
        super(EasterHeadDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new EasterHeadDoorModel(EasterHeadDoorModel.getTexturedModelData().createModel());
    }
}
