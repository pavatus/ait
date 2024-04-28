package loqor.ait.tardis.door;

import loqor.ait.client.models.coral.CoralGrowthDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.core.data.schema.door.ClientDoorSchema;
import loqor.ait.tardis.door.CoralGrowthDoorVariant;

public class ClientGrowthDoorVariant extends ClientDoorSchema {
	public ClientGrowthDoorVariant() {
		super(CoralGrowthDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new CoralGrowthDoorModel(CoralGrowthDoorModel.getTexturedModelData().createModel());
	}
}
