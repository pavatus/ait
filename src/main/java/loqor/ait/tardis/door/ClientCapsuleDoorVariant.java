package loqor.ait.tardis.door;

import loqor.ait.client.models.doors.CapsuleDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.core.data.schema.door.ClientDoorSchema;
import loqor.ait.tardis.door.CapsuleDoorVariant;

public class ClientCapsuleDoorVariant extends ClientDoorSchema {
	public ClientCapsuleDoorVariant() {
		super(CapsuleDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new CapsuleDoorModel(CapsuleDoorModel.getTexturedModelData().createModel());
	}
}
