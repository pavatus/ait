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
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

public class WorldUtil {

    public static DirectedGlobalPos.Cached locateSafe(DirectedGlobalPos.Cached cached, TravelHandlerBase.GroundSearch vSearch, boolean hSearch) {
        if (vSearch == TravelHandlerBase.GroundSearch.NONE && !hSearch)
            return cached;

        BlockPos pos = cached.getPos();

        int y = switch (vSearch) {
            case CEILING -> cached.getWorld().getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());
            case FLOOR -> findSafeBottomY(cached.getWorld(), pos);
            case MEDIAN -> 0; // TODO
            case NONE -> pos.getY();
        };

        return cached.offset(0, y, 0);
    }

    private static int findSafeBottomY(ServerWorld world, BlockPos pos) {
        BlockPos.Mutable mutPos = pos.mutableCopy().setY(world.getBottomY());

        BlockState current = world.getBlockState(mutPos.down());
        BlockState above = world.getBlockState(mutPos);

        while (true) {
            System.out.println("At y: " + mutPos.getY());

            if (!current.blocksMovement() && !above.blocksMovement())
                return mutPos.getY();

            current = above;
            above = world.getBlockState(mutPos.move(Direction.UP));
        }
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
