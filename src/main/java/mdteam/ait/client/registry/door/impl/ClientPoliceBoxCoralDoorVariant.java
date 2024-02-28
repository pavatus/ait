package mdteam.ait.client.registry.door.impl;

import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.models.doors.PoliceBoxCoralDoorModel;
import mdteam.ait.client.registry.door.ClientDoorSchema;
import mdteam.ait.tardis.variant.door.PoliceBoxCoralDoorVariant;

public class ClientPoliceBoxCoralDoorVariant extends ClientDoorSchema {
	public ClientPoliceBoxCoralDoorVariant() {
		super(PoliceBoxCoralDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new PoliceBoxCoralDoorModel(PoliceBoxCoralDoorModel.getTexturedModelData().createModel());
	}
}
