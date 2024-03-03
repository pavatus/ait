package mdteam.ait.client.registry.door.impl;

import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.models.doors.RenegadeDoorModel;
import mdteam.ait.client.registry.door.ClientDoorSchema;
import mdteam.ait.tardis.variant.door.RenegadeDoorVariant;

public class ClientRenegadeDoorVariant extends ClientDoorSchema {
	public ClientRenegadeDoorVariant() {
		super(RenegadeDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new RenegadeDoorModel(RenegadeDoorModel.getTexturedModelData().createModel());
	}
}
