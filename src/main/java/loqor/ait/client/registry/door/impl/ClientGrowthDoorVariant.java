package loqor.ait.client.registry.door.impl;

import loqor.ait.client.models.coral.CoralGrowthDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.registry.door.ClientDoorSchema;
import loqor.ait.tardis.variant.door.CoralGrowthDoorVariant;

public class ClientGrowthDoorVariant extends ClientDoorSchema {
	public ClientGrowthDoorVariant() {
		super(CoralGrowthDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new CoralGrowthDoorModel(CoralGrowthDoorModel.getTexturedModelData().createModel());
	}
}
