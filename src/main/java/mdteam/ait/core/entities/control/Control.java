package mdteam.ait.core.entities.control;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import the.mdteam.ait.Tardis;

public abstract class Control {
    public final String id; // a name to represent the control

    public Control(String id) {
        this.id = id;
    }

    public abstract boolean runClient(Tardis tardis,  ClientPlayerEntity player, ClientWorld world);
    public abstract boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world);

    @Override
    public String toString() {
        return "Control{" +
                "id='" + id + '\'' +
                '}';
    }
}
