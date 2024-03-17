package loqor.ait.client.registry.door.impl;

import loqor.ait.client.models.doors.CapsuleDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.registry.door.ClientDoorSchema;
import loqor.ait.tardis.variant.door.CapsuleDoorVariant;

public class ClientCapsuleDoorVariant extends ClientDoorSchema {
	public ClientCapsuleDoorVariant() {
		super(CapsuleDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new CapsuleDoorModel(CapsuleDoorModel.getTexturedModelData().createModel());
	}
}
