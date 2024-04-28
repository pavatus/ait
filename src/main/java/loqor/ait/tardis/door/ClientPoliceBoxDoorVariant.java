package loqor.ait.tardis.door;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.PoliceBoxDoorModel;
import loqor.ait.core.data.schema.door.ClientDoorSchema;
import loqor.ait.tardis.door.PoliceBoxDoorVariant;

public class ClientPoliceBoxDoorVariant extends ClientDoorSchema {
	public ClientPoliceBoxDoorVariant() {
		super(PoliceBoxDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new PoliceBoxDoorModel(PoliceBoxDoorModel.getTexturedModelData().createModel());
	}
}
