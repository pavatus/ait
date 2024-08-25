package loqor.ait.tardis.door;

import loqor.ait.client.models.doors.CapsuleDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.core.data.schema.door.ClientDoorSchema;

public class ClientAdaptiveDoorVariant extends ClientDoorSchema {
    public ClientAdaptiveDoorVariant() {
        super(AdaptiveDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new CapsuleDoorModel(CapsuleDoorModel.getTexturedModelData().createModel());
    }
}
