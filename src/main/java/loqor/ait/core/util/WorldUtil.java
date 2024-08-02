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

@SuppressWarnings("deprecation")
public class WorldUtil {

    public static DirectedGlobalPos.Cached locateSafe(DirectedGlobalPos.Cached cached, TravelHandlerBase.GroundSearch vSearch, boolean hSearch) {
        ServerWorld world = cached.getWorld();
        BlockPos pos = cached.getPos();

        if (hSearch && vSearch == TravelHandlerBase.GroundSearch.NONE)
            vSearch = TravelHandlerBase.GroundSearch.MEDIAN;

        int x = pos.getX();
        int z = pos.getZ();

        int y = switch (vSearch) {
            case CEILING -> findSafeTopY(world, pos);
            case FLOOR -> findSafeBottomY(world, pos);
            case MEDIAN -> findSafeMedianY(world, pos);
            case NONE -> pos.getY();
        };

        return cached.pos(x, y, z);
    }

    private static int findSafeMedianY(ServerWorld world, BlockPos pos) {
        BlockPos upCursor = pos;
        BlockState curUp = world.getBlockState(upCursor);
        BlockState aboveUp = world.getBlockState(upCursor.up());

        BlockPos downCursor = pos;
        BlockState curDown = world.getBlockState(downCursor);
        BlockState aboveDown = world.getBlockState(downCursor.down());

        while (true) {
            System.out.println("At top y: " + upCursor.getY());
            System.out.println("At bottom y: " + downCursor.getY());

            if (!curUp.blocksMovement() && !aboveUp.blocksMovement())
                return upCursor.getY();

            if (!curDown.blocksMovement() && !aboveDown.blocksMovement())
                return downCursor.getY();

            upCursor = upCursor.up();
            downCursor = downCursor.down();

            curUp = aboveUp;
            aboveUp = world.getBlockState(upCursor);

            curDown = aboveDown;
            aboveDown = world.getBlockState(downCursor);
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

            cursor = cursor.up();

            current = above;
            above = world.getBlockState(cursor);
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
