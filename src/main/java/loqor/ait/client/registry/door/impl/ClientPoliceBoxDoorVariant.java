package loqor.ait.client.registry.door.impl;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.PoliceBoxDoorModel;
import loqor.ait.client.registry.door.ClientDoorSchema;
import loqor.ait.tardis.variant.door.PoliceBoxDoorVariant;

public class ClientPoliceBoxDoorVariant extends ClientDoorSchema {
	public ClientPoliceBoxDoorVariant() {
		super(PoliceBoxDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new PoliceBoxDoorModel(PoliceBoxDoorModel.getTexturedModelData().createModel());
	}
}
