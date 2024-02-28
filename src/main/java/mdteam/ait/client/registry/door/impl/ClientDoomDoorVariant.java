package mdteam.ait.client.registry.door.impl;

import mdteam.ait.client.models.doors.DoomDoorModel;
import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.registry.door.ClientDoorSchema;
import mdteam.ait.tardis.variant.door.DoomDoorVariant;

public class ClientDoomDoorVariant extends ClientDoorSchema {
	public ClientDoomDoorVariant() {
		super(DoomDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new DoomDoorModel(DoomDoorModel.getTexturedModelData().createModel());
	}
}
