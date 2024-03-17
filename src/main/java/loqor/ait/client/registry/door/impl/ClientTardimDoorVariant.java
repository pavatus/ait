package loqor.ait.client.registry.door.impl;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.TardimDoorModel;
import loqor.ait.client.registry.door.ClientDoorSchema;
import loqor.ait.tardis.variant.door.TardimDoorVariant;

public class ClientTardimDoorVariant extends ClientDoorSchema {
	public ClientTardimDoorVariant() {
		super(TardimDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new TardimDoorModel(TardimDoorModel.getTexturedModelData().createModel());
	}
}
