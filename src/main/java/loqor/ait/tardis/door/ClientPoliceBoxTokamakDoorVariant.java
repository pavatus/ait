package loqor.ait.tardis.door;

import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.PoliceBoxCoralDoorModel;
import loqor.ait.core.data.schema.door.ClientDoorSchema;
import loqor.ait.tardis.door.PoliceBoxTokamakDoorVariant;

public class ClientPoliceBoxTokamakDoorVariant extends ClientDoorSchema {
	public ClientPoliceBoxTokamakDoorVariant() {
		super(PoliceBoxTokamakDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new PoliceBoxCoralDoorModel(PoliceBoxCoralDoorModel.getTexturedModelData().createModel());
	}
}
