package loqor.ait.client.registry.door.impl;

import loqor.ait.client.models.doors.ClassicDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.registry.door.ClientDoorSchema;
import loqor.ait.tardis.variant.door.ClassicDoorVariant;

public class ClientClassicDoorVariant extends ClientDoorSchema {
	public ClientClassicDoorVariant() {
		super(ClassicDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new ClassicDoorModel(ClassicDoorModel.getTexturedModelData().createModel());
	}
}
