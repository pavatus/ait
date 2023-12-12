package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.control.Control;
import mdteam.ait.data.AbsoluteBlockPos;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;

public class RandomiserControl extends Control {
    public RandomiserControl() {
        super("randomiser");
    }

    @Override
    public boolean runClient(Tardis tardis, ClientPlayerEntity player, ClientWorld world) {
        return true;
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        TardisTravel travel = tardis.getTravel();
        int increment = travel.getPosManager().increment;
        AbsoluteBlockPos.Directed current = travel.getPosition();
        AbsoluteBlockPos.Directed dest = travel.getDestination();

        int x = current.getX() + ((world.random.nextBoolean()) ? world.random.nextInt(increment) : -world.random.nextInt(increment));
        int z = current.getZ() + ((world.random.nextBoolean()) ? world.random.nextInt(increment) : -world.random.nextInt(increment));
        BlockPos pos = new BlockPos(x,current.getY(),z);

        travel.setDestination(new AbsoluteBlockPos.Directed(pos, dest.getWorld(), dest.getDirection()), true);

        messagePlayer(player, travel);

        return true;
    }

    private void messagePlayer(ServerPlayerEntity player, TardisTravel travel) {
        AbsoluteBlockPos.Directed dest = travel.getDestination();

        player.sendMessage(Text.literal("Destination: " + dest.getX() + " | " + dest.getY() + " | " + dest.getZ()), true); // fixme translatable is preferred
    }
}
