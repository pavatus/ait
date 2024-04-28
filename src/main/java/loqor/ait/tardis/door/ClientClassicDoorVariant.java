package loqor.ait.tardis.door;

import loqor.ait.client.models.doors.ClassicDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.core.data.schema.door.ClientDoorSchema;
import loqor.ait.tardis.door.ClassicDoorVariant;

public class ClientClassicDoorVariant extends ClientDoorSchema {
	public ClientClassicDoorVariant() {
		super(ClassicDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new ClassicDoorModel(ClassicDoorModel.getTexturedModelData().createModel());
	}
}
