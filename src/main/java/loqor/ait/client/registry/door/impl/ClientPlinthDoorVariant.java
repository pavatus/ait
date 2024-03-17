package loqor.ait.client.registry.door.impl;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.PlinthDoorModel;
import loqor.ait.client.registry.door.ClientDoorSchema;
import loqor.ait.tardis.variant.door.PlinthDoorVariant;

public class ClientPlinthDoorVariant extends ClientDoorSchema {
	public ClientPlinthDoorVariant() {
		super(PlinthDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new PlinthDoorModel(PlinthDoorModel.getTexturedModelData().createModel());
	}
}
