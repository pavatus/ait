package loqor.ait.tardis.door;

import loqor.ait.client.models.doors.BookshelfDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.core.data.schema.door.ClientDoorSchema;

public class ClientBookshelfDoorVariant extends ClientDoorSchema {
    public ClientBookshelfDoorVariant() {
        super(BookshelfDoorVariant.REFERENCE);
    }

    @Override
    public DoorModel model() {
        return new BookshelfDoorModel(BookshelfDoorModel.getTexturedModelData().createModel());
    }
}
