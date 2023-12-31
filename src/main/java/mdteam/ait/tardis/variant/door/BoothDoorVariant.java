package mdteam.ait.tardis.variant.door;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.doors.BoothDoorModel;
import mdteam.ait.client.models.doors.DoorModel;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class BoothDoorVariant extends DoorSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "door/booth");

    public BoothDoorVariant() {
        super(REFERENCE);
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public SoundEvent openSound() {
        return SoundEvents.BLOCK_IRON_DOOR_OPEN;
    }

    @Override
    public SoundEvent closeSound() {
        return SoundEvents.BLOCK_IRON_DOOR_CLOSE;
    }
}
