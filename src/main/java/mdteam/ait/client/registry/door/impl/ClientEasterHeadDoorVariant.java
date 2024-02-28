package mdteam.ait.client.registry.door.impl;

import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.models.doors.EasterHeadDoorModel;
import mdteam.ait.client.registry.door.ClientDoorSchema;
import mdteam.ait.tardis.variant.door.EasterHeadDoorVariant;

public class ClientEasterHeadDoorVariant extends ClientDoorSchema {
	public ClientEasterHeadDoorVariant() {
		super(EasterHeadDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new EasterHeadDoorModel(EasterHeadDoorModel.getTexturedModelData().createModel());
	}
}
