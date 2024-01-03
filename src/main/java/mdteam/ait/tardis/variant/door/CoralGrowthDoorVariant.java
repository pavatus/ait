package mdteam.ait.tardis.variant.door;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.doors.ClassicDoorModel;
import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.models.doors.EasterHeadDoorModel;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class CoralGrowthDoorVariant extends DoorSchema {

    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "door/coral_growth");

    public CoralGrowthDoorVariant() {
        super(REFERENCE);
    }

    @Override
    public boolean isDouble() {
        return true;
    }

    @Override
    public SoundEvent openSound() {
        return SoundEvents.BLOCK_WOODEN_DOOR_OPEN;
    }

    @Override
    public SoundEvent closeSound() {
        return SoundEvents.BLOCK_WOODEN_DOOR_CLOSE;
    }
}
