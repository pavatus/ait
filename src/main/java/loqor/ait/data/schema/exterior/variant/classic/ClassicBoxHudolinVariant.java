package loqor.ait.data.schema.exterior.variant.classic;

import loqor.ait.data.schema.door.DoorSchema;
import loqor.ait.data.schema.door.impl.ClassicHudolinDoorVariant;
import loqor.ait.registry.impl.door.DoorRegistry;

public class ClassicBoxHudolinVariant extends ClassicBoxVariant {
    public ClassicBoxHudolinVariant() {
        super("hudolin");
    }

    @Override
    public DoorSchema door() {
        return DoorRegistry.REGISTRY.get(ClassicHudolinDoorVariant.REFERENCE);
    }

}
