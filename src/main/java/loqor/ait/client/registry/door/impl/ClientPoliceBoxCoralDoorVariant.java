package loqor.ait.client.registry.door.impl;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.PoliceBoxCoralDoorModel;
import loqor.ait.client.registry.door.ClientDoorSchema;
import loqor.ait.tardis.variant.door.PoliceBoxCoralDoorVariant;

public class ClientPoliceBoxCoralDoorVariant extends ClientDoorSchema {
	public ClientPoliceBoxCoralDoorVariant() {
		super(PoliceBoxCoralDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new PoliceBoxCoralDoorModel(PoliceBoxCoralDoorModel.getTexturedModelData().createModel());
	}
}
