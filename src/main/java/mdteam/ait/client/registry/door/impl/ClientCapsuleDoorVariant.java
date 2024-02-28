package mdteam.ait.client.registry.door.impl;

import mdteam.ait.client.models.doors.CapsuleDoorModel;
import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.registry.door.ClientDoorSchema;
import mdteam.ait.tardis.variant.door.CapsuleDoorVariant;

public class ClientCapsuleDoorVariant extends ClientDoorSchema {
	public ClientCapsuleDoorVariant() {
		super(CapsuleDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new CapsuleDoorModel(CapsuleDoorModel.getTexturedModelData().createModel());
	}
}
