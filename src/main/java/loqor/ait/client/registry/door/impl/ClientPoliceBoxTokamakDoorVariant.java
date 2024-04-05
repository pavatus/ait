package loqor.ait.client.registry.door.impl;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.PoliceBoxCoralDoorModel;
import loqor.ait.client.registry.door.ClientDoorSchema;
import loqor.ait.tardis.variant.door.PoliceBoxTokamakDoorVariant;

public class ClientPoliceBoxTokamakDoorVariant extends ClientDoorSchema {
	public ClientPoliceBoxTokamakDoorVariant() {
		super(PoliceBoxTokamakDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new PoliceBoxCoralDoorModel(PoliceBoxCoralDoorModel.getTexturedModelData().createModel());
	}
}
