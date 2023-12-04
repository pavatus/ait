package mdteam.ait.core.entities.control.impl;

import mdteam.ait.core.entities.control.Control;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.AbsoluteBlockPos;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import the.mdteam.ait.Tardis;
import the.mdteam.ait.TardisTravel;

import java.util.ArrayList;
import java.util.List;

public class DimensionControl extends Control {
    public DimensionControl() {
        super("dimension");
    }

    @Override
    public boolean runClient(Tardis tardis, ClientPlayerEntity player, ClientWorld world) {
        return true;
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        TardisTravel travel = tardis.getTravel();
        AbsoluteBlockPos.Directed dest = travel.getDestination();
        List<ServerWorld> dims = getDimensions();

        int current = dims.indexOf(dest.getWorld());
        int next = 0;

        if (!player.isSneaking()) {
            next = ((current + 1) > dims.size() - 1) ? 0 : current + 1;
        } else {
            next = ((current - 1) < 0) ? dims.size() - 1 : current - 1;
        }

        travel.setDestination(new AbsoluteBlockPos.Directed(dest, dims.get(next), dest.getDirection()),false);

        messagePlayer(player, (ServerWorld) travel.getDestination().getWorld());

        return true;
    }

    private void messagePlayer(ServerPlayerEntity player, ServerWorld world) {
        player.sendMessage(Text.literal("Dimension: " + convertWorldToReadable(world)), true); // fixme translatable is preferred
    }
    public static String convertWorldToReadable(World world) {
        return world.getDimensionKey().getValue().getPath().replace("_"," ").toUpperCase();
    }

    private List<ServerWorld> getDimensions() {
        List<ServerWorld> dims = new ArrayList<>();
        Iterable<ServerWorld> allDims = TardisUtil.getServer().getWorlds();

        // todo an actual filter
        allDims.forEach(dims::add);

        return dims;
    }
}
