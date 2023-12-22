package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.control.impl.pos.PosType;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;

import java.util.ArrayList;
import java.util.List;

public class DimensionControl extends Control {
    public DimensionControl() {
        super("dimension");
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

        travel.setDestination(new AbsoluteBlockPos.Directed(PosType.Y.add(dest, 0), dims.get(next), dest.getDirection()), false); // postype.y.add means it clamps the y coord fixme doesnt work for nether as u can go above the bedrock but dont hardcode it like you did loqor :(

        messagePlayer(player, (ServerWorld) travel.getDestination().getWorld());

        return true;
    }

    private void messagePlayer(ServerPlayerEntity player, ServerWorld world) {
        player.sendMessage(Text.literal("Dimension: " + convertWorldToReadable(world)), true); // fixme translatable is preferred
    }

    public static String convertWorldToReadable(World world) {
        String path = world.getDimensionKey().getValue().getPath();

        // Split the string into words
        String[] words = path.split("_");

        // Capitalize the first letter of each word
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
        }

        // Join the words back together with spaces
        return String.join(" ", words);
    }

    public static String convertWorldValueToModified(String value) {

        // Split the string into words
        String[] words = value.split("_");

        // Capitalize the first letter of each word
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
        }

        // Join the words back together with spaces
        return String.join(" ", words);
    }

    public static String capitalizeAndReplaceEach(String input) {
        // Split the string into words
        String[] words = input.split("_");

        // Capitalize the first letter of each word
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
        }

        // Join the words back together with spaces
        return String.join(" ", words);
    }

    private List<ServerWorld> getDimensions() {
        List<ServerWorld> dims = new ArrayList<>();
        Iterable<ServerWorld> allDims = TardisUtil.getServer().getWorlds();

        // fixme this is easiest/stupidest way to do this without letting them get to the tardis dim :p - Loqor
        allDims.forEach(dim -> {
            if (dim.getRegistryKey() != TardisUtil.getTardisDimension().getRegistryKey())
                dims.add(dim);
        });

        return dims;
    }
}
