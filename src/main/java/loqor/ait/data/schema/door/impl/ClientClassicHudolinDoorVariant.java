package loqor.ait.data.schema.door.impl;

import loqor.ait.client.models.doors.ClassicHudolinDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.data.schema.door.ClientDoorSchema;

public class ClientClassicHudolinDoorVariant extends ClientDoorSchema {
    public ClientClassicHudolinDoorVariant() {
        super(ClassicHudolinDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new ClassicHudolinDoorModel(ClassicHudolinDoorModel.getTexturedModelData().createModel());
    }
}
