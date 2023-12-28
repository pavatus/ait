package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
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
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        TardisTravel travel = tardis.getTravel();

        randomiseDestination(tardis, 10);
        tardis.removeFuel(25);

        messagePlayer(player, travel);

        return true;
    }

    // fixme this is LAGGYYY @TODO
    public static AbsoluteBlockPos.Directed randomiseDestination(Tardis tardis, int limit) {
        TardisTravel travel = tardis.getTravel();
        int increment = travel.getPosManager().increment;
        AbsoluteBlockPos.Directed current = travel.getPosition();
        AbsoluteBlockPos.Directed dest = travel.getDestination();
        ServerWorld world = (ServerWorld) current.getWorld();

        BlockPos pos;
        int x, z;

//        for (int i = 0; i <= limit; i++) {
        x = current.getX() + ((world.random.nextBoolean()) ? world.random.nextInt(increment) : -world.random.nextInt(increment));
        z = current.getZ() + ((world.random.nextBoolean()) ? world.random.nextInt(increment) : -world.random.nextInt(increment));
        pos = new BlockPos(x, current.getY(), z);

        travel.setDestination(new AbsoluteBlockPos.Directed(pos, dest.getWorld(), dest.getDirection()), false);

        if (travel.checkDestination()) return travel.getDestination();
//        }

        return travel.getDestination();
    }

    private void messagePlayer(ServerPlayerEntity player, TardisTravel travel) {
        AbsoluteBlockPos.Directed dest = travel.getDestination();

        player.sendMessage(Text.literal("Destination: " + dest.getX() + " | " + dest.getY() + " | " + dest.getZ()), true); // fixme translatable is preferred
    }
}
