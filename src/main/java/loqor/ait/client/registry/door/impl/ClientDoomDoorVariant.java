package loqor.ait.client.registry.door.impl;

import loqor.ait.client.models.doors.DoomDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.registry.door.ClientDoorSchema;
import loqor.ait.tardis.variant.door.DoomDoorVariant;

public class ClientDoomDoorVariant extends ClientDoorSchema {
	public ClientDoomDoorVariant() {
		super(DoomDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new DoomDoorModel(DoomDoorModel.getTexturedModelData().createModel());
	}
}
