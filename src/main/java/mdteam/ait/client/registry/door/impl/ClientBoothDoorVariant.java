package mdteam.ait.client.registry.door.impl;

import mdteam.ait.client.models.doors.BoothDoorModel;
import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.registry.door.ClientDoorSchema;
import mdteam.ait.tardis.variant.door.BoothDoorVariant;

public class ClientBoothDoorVariant extends ClientDoorSchema {
	public ClientBoothDoorVariant() {
		super(BoothDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new BoothDoorModel(BoothDoorModel.getTexturedModelData().createModel());
	}
}
