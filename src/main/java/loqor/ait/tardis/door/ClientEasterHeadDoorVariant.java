package loqor.ait.tardis.door;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.EasterHeadDoorModel;
import loqor.ait.core.data.schema.door.ClientDoorSchema;
import loqor.ait.tardis.door.EasterHeadDoorVariant;

public class ClientEasterHeadDoorVariant extends ClientDoorSchema {
	public ClientEasterHeadDoorVariant() {
		super(EasterHeadDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new EasterHeadDoorModel(EasterHeadDoorModel.getTexturedModelData().createModel());
	}
}
