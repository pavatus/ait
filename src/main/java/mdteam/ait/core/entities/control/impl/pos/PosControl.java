package mdteam.ait.core.entities.control.impl.pos;

import mdteam.ait.core.entities.control.Control;
import mdteam.ait.data.AbsoluteBlockPos;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;

public abstract class PosControl extends Control {
    private final PosType type;
    public PosControl(PosType type, String id) {
        super(id);
        this.type = type;
    }
    public PosControl(PosType type) {
        this(type,type.asString());
    }

    @Override
    public boolean runClient(Tardis tardis, ClientPlayerEntity player, ClientWorld world) {
        return true;
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        TardisTravel travel = tardis.getTravel();
        AbsoluteBlockPos.Directed destination = travel.getDestination();
        PosManager manager = travel.getPosManager();

        BlockPos pos = this.type.add(destination, (player.isSneaking()) ? -manager.increment : manager.increment);
        travel.setDestination(new AbsoluteBlockPos.Directed(pos,destination.getWorld(),destination.getDirection()), true);

        messagePlayerDestination(player, travel);

        return true;
    }

    private void messagePlayerDestination(ServerPlayerEntity player, TardisTravel travel) {
        AbsoluteBlockPos.Directed dest = travel.getDestination();

        player.sendMessage(Text.literal("Destination: " + dest.getX() + " | " + dest.getY() + " | " + dest.getZ()), true); // fixme translatable is preferred
    }
}
