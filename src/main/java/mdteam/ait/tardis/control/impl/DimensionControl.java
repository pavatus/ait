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
import java.util.Objects;

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

        // FIXME we should make it so that once the ender dragon is defeated, the end is unlocked; also make that a config option as well for the server. - Loqor

        /*if (dest.getWorld().getRegistryKey() != World.END) {*/
        travel.setDestination(new AbsoluteBlockPos.Directed(PosType.Y.add(dest, 0), dims.get(next), dest.getDirection()), false); // postype.y.add means it clamps the y coord fixme doesnt work for nether as u can go above the bedrock but dont hardcode it like you did loqor :(
        messagePlayer(player, (ServerWorld) travel.getDestination().getWorld());
        /*} else {
            if(dest.getWorld().getServer().getWorld(dest.getWorld().getRegistryKey()).getAliveEnderDragons().isEmpty()) {
                messagePlayer(player, (ServerWorld) travel.getDestination().getWorld());
                travel.setDestination(new AbsoluteBlockPos.Directed(PosType.Y.add(dest, 0), dims.get(next), dest.getDirection()), false);
            } else {
                player.sendMessage(Text.literal("The End is forbidden."), true); // fixme translatable is preferred
                travel.setDestination(new AbsoluteBlockPos.Directed(PosType.Y.add(dest, 0), world.getServer().getWorld(World.OVERWORLD), dest.getDirection()), false);
            }
        }*/

        tardis.markDirty();

        return true;
    }

    private void messagePlayer(ServerPlayerEntity player, ServerWorld world) {
        Text message = Text.translatable("message.ait.tardis.control.dimension.info").append(
                Text.literal(" " + convertWorldToReadable(world))
        );

        player.sendMessage(message, true); // fixme translatable is preferred
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

    // @TODO for some reason in the dev env, this method tends to not like doing anything sometimes. idk, it works or it doesnt, but in builds, it always works. funny what a lil spaghetti man can tell you at 3 am
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
