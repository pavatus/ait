package mdteam.ait.client.registry.door.impl;

import mdteam.ait.client.models.doors.ClassicDoorModel;
import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.registry.door.ClientDoorSchema;
import mdteam.ait.tardis.variant.door.ClassicDoorVariant;
import net.minecraft.util.Identifier;

public class ClientClassicDoorVariant extends ClientDoorSchema {
    public ClientClassicDoorVariant() {
        super(ClassicDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new ClassicDoorModel(ClassicDoorModel.getTexturedModelData().createModel());
    }
}
