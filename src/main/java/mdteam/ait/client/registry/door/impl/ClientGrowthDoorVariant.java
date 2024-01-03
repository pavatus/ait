package mdteam.ait.client.registry.door.impl;

import mdteam.ait.client.models.coral.CoralGrowthDoorModel;
import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.registry.door.ClientDoorSchema;
import mdteam.ait.client.registry.exterior.impl.growth.ClientGrowthVariant;
import mdteam.ait.tardis.variant.door.CoralGrowthDoorVariant;
import net.minecraft.util.Identifier;

public class ClientGrowthDoorVariant extends ClientDoorSchema {
    public ClientGrowthDoorVariant() {
        super(CoralGrowthDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new CoralGrowthDoorModel(CoralGrowthDoorModel.getTexturedModelData().createModel());
    }
}
