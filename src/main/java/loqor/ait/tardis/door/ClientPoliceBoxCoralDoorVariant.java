package loqor.ait.tardis.door;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.PoliceBoxCoralDoorModel;
import loqor.ait.core.data.schema.door.ClientDoorSchema;
import loqor.ait.tardis.door.PoliceBoxCoralDoorVariant;

public class ClientPoliceBoxCoralDoorVariant extends ClientDoorSchema {
	public ClientPoliceBoxCoralDoorVariant() {
		super(PoliceBoxCoralDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new PoliceBoxCoralDoorModel(PoliceBoxCoralDoorModel.getTexturedModelData().createModel());
	}
}
