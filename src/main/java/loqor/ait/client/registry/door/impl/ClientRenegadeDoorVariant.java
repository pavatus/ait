package loqor.ait.client.registry.door.impl;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.RenegadeDoorModel;
import loqor.ait.client.registry.door.ClientDoorSchema;
import loqor.ait.tardis.variant.door.RenegadeDoorVariant;

public class ClientRenegadeDoorVariant extends ClientDoorSchema {
	public ClientRenegadeDoorVariant() {
		super(RenegadeDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new RenegadeDoorModel(RenegadeDoorModel.getTexturedModelData().createModel());
	}
}
