package loqor.ait.data.schema.exterior.variant.classic;

import loqor.ait.data.schema.door.DoorSchema;
import loqor.ait.data.schema.door.impl.ClassicHudolinDoorVariant;
import loqor.ait.registry.impl.door.DoorRegistry;

public class ClassicBoxShalkaVariant extends ClassicBoxVariant {
    public ClassicBoxShalkaVariant() {
        super("shalka");
    }

    @Override
    public DoorSchema door() {
        return DoorRegistry.REGISTRY.get(ClassicHudolinDoorVariant.REFERENCE);
    }
}