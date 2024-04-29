package loqor.ait.tardis.door;

import loqor.ait.client.models.doors.BoothDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.core.data.schema.door.ClientDoorSchema;
import loqor.ait.tardis.door.BoothDoorVariant;

public class ClientBoothDoorVariant extends ClientDoorSchema {
	public ClientBoothDoorVariant() {
		super(BoothDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new BoothDoorModel(BoothDoorModel.getTexturedModelData().createModel());
	}
}
