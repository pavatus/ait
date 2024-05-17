package loqor.ait.tardis.door;

import loqor.ait.client.models.doors.CapsuleDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.models.doors.GeometricDoorModel;
import loqor.ait.core.data.schema.door.ClientDoorSchema;

public class ClientGeometricDoorVariant extends ClientDoorSchema {
	public ClientGeometricDoorVariant() {
		super(GeometricDoorVariant.REFERENCE);
	}

	@Override
	public DoorModel model() {
		return new GeometricDoorModel(GeometricDoorModel.getTexturedModelData().createModel());
	}
}
