package mdteam.ait.tardis.control;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class Control {
    public String id; // a name to represent the control

    public Control(String id) {
        this.setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        return false;
    }

    public SoundEvent getSound() {
        return SoundEvents.BLOCK_NOTE_BLOCK_BIT.value();
    }

    @Override
    public String toString() {
        return "Control{" +
                "id='" + id + '\'' +
                '}';
    }
}
