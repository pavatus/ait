package loqor.ait.client.registry.door.impl;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.EasterHeadDoorModel;
import loqor.ait.client.registry.door.ClientDoorSchema;
import loqor.ait.tardis.variant.door.EasterHeadDoorVariant;

public class ClientEasterHeadDoorVariant extends ClientDoorSchema {
	public ClientEasterHeadDoorVariant() {
		super(EasterHeadDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new EasterHeadDoorModel(EasterHeadDoorModel.getTexturedModelData().createModel());
	}
}
