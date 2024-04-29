package loqor.ait.tardis.door;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.RenegadeDoorModel;
import loqor.ait.core.data.schema.door.ClientDoorSchema;
import loqor.ait.tardis.door.RenegadeDoorVariant;

public class ClientRenegadeDoorVariant extends ClientDoorSchema {
	public ClientRenegadeDoorVariant() {
		super(RenegadeDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new RenegadeDoorModel(RenegadeDoorModel.getTexturedModelData().createModel());
	}
}
