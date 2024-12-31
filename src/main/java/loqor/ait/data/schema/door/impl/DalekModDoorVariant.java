package loqor.ait.data.schema.door.impl;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.schema.door.DoorSchema;

public class DalekModDoorVariant extends DoorSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "door/dalek_mod");

    public DalekModDoorVariant() {
        super(REFERENCE);
    }

    @Override
    public boolean isDouble() {
        return true;
    }
}
