package mdteam.ait.client.registry.door.impl;

import mdteam.ait.client.models.coral.CoralGrowthDoorModel;
import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.registry.door.ClientDoorSchema;
import mdteam.ait.tardis.variant.door.CoralGrowthDoorVariant;

public class ClientGrowthDoorVariant extends ClientDoorSchema {
	public ClientGrowthDoorVariant() {
		super(CoralGrowthDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new CoralGrowthDoorModel(CoralGrowthDoorModel.getTexturedModelData().createModel());
	}
}
