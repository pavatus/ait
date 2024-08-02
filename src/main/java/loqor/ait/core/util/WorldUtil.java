package loqor.ait.core.util;

import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

public class WorldUtil {

    public static DirectedGlobalPos.Cached locateSafe(DirectedGlobalPos.Cached cached, TravelHandlerBase.GroundSearch vSearch, boolean hSearch) {
        ServerWorld world = cached.getWorld();
        BlockPos pos = cached.getPos();

        int y = switch (vSearch) {
            case CEILING -> findSafeTopY(world, pos);
            case FLOOR -> findSafeBottomY(world, pos);
            case MEDIAN -> pos.getY(); // TODO
        };

        return cached.offset(0, y, 0);
    }

    private static int findSafeMedianY(ServerWorld world, BlockPos pos) {
        BlockPos upCursor = pos;
        BlockPos downCursor = pos;

        while (true) {

        }
    }

    private static int findSafeBottomY(ServerWorld world, BlockPos pos) {
        BlockPos cursor = pos.withY(world.getBottomY() + 1);

        BlockState current = world.getBlockState(cursor.down());
        BlockState above = world.getBlockState(cursor);

        while (true) {
            System.out.println("At y: " + cursor.getY());

            if (!current.blocksMovement() && !above.blocksMovement())
                return cursor.getY();

            current = above;
            above = world.getBlockState(cursor.up());
        }
    }

    private static int findSafeTopY(ServerWorld world, BlockPos pos) {
        int x = pos.getX();
        int z = pos.getZ();

        return world.getChunk(
                ChunkSectionPos.getSectionCoord(x),
                ChunkSectionPos.getSectionCoord(z)
        ).sampleHeightmap(
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                x & 15, z & 15
        );
    }

    @Environment(EnvType.CLIENT)
    public static String getName(MinecraftClient client) {
        if (client.isInSingleplayer()) {
            return client.getServer().getSavePath(WorldSavePath.ROOT).getParent().getFileName().toString();
        }

        return client.getCurrentServerEntry().address;
    }

    public static Text worldText(RegistryKey<World> key) {
        return Text.translatableWithFallback(key.getValue().toTranslationKey("dimension"), fakeTranslate(key));
    }

    public static Text worldText(Identifier value) {
        return Text.translatableWithFallback(value.toTranslationKey("dimension"), fakeTranslate(value));
    }

    private static String fakeTranslate(RegistryKey<World> id) {
        return fakeTranslate(id.getValue());
    }

    private static String fakeTranslate(Identifier id) {
        return fakeTranslate(id.getPath());
    }

    private static String fakeTranslate(String path) {
        // Split the string into words
        String[] words = path.split("_");

        // Capitalize the first letter of each word
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
        }

        // Join the words back together with spaces
        return String.join(" ", words);
    }
}
