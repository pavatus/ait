package loqor.ait.client.registry.door.impl;

import loqor.ait.client.models.doors.BoothDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.registry.door.ClientDoorSchema;
import loqor.ait.tardis.variant.door.BoothDoorVariant;

public class ClientBoothDoorVariant extends ClientDoorSchema {
	public ClientBoothDoorVariant() {
		super(BoothDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new BoothDoorModel(BoothDoorModel.getTexturedModelData().createModel());
	}
}
