package mdteam.ait.core.entities.control;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;

public abstract class Control {
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

    public abstract boolean runClient(Tardis tardis, ClientPlayerEntity player, ClientWorld world);
    public abstract boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world);

    @Override
    public String toString() {
        return "Control{" +
                "id='" + id + '\'' +
                '}';
    }
}
