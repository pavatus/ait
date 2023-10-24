package mdteam.ait.core.tardis;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blocks.DoorBlock;
import mdteam.ait.core.helper.AbsoluteBlockPos;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.minecraft.datafixer.fix.BlockEntitySignTextStrictJsonFix.GSON;

public class TardisHandler {

    public static Map<UUID, Tardis> tardisses = new HashMap<>();

    @Nullable
    public static Tardis getTardis(UUID uuid) {
        return tardisses.get(uuid);
    }

    @Nullable
    public static Tardis getTardisByExteriorPos(BlockPos blockPos) {
        for (Tardis tardis : tardisses.values()) {
            if (tardis.getPosition().toBlockPos().equals(blockPos)) {
                return tardis;
            }
        }
        return null;
    }

    @Nullable
    public static Tardis getTardisByInteriorPos(BlockPos pos) {
        for (Tardis value : tardisses.values()) {
            BlockPos xz1 = value.getDesktop().getInteriorCornerPositions().get(0).toBlockPos();
            BlockPos xz2 = value.getDesktop().getInteriorCornerPositions().get(1).toBlockPos();
            if (pos.getX() <= Math.max(xz1.getX(), xz2.getX()) && pos.getX() >= Math.min(xz1.getX(), xz2.getX())
						&& pos.getZ() <= Math.max(xz1.getZ(), xz2.getZ()) && pos.getZ() >= Math.min(xz1.getZ(), xz2.getZ())) {
                return value;
            }
        }
        return null;
    }

    public static BlockPos searchForDoorBlock(List<AbsoluteBlockPos> interiorCornerPositions) {
        BlockPos bottomLeft = interiorCornerPositions.get(0).toBlockPos();
        BlockPos topRight = interiorCornerPositions.get(1).toBlockPos();
        BlockPos checkPos = bottomLeft;
        World world = interiorCornerPositions.get(0).getDimension();

        // We want to check each row of these coordinates until we find a door block (hopefully)
        for (int y = bottomLeft.getY(); y < 256; y++) {
            for (int z = bottomLeft.getZ(); z <= topRight.getZ();z++) {
                for (int x = bottomLeft.getX(); x <= topRight.getX();x++) {
                    checkPos = new BlockPos(x,y,z);

                    if (world.getBlockState(checkPos).getBlock() instanceof DoorBlock) {
                        return checkPos;
                    }
                }
            }
        }

        return null;
    }

    public static void loadTardis(UUID uuid) {
        if(tardisses.containsKey(uuid)) return;
        File file = new File(AITMod.mcServer.getSavePath(WorldSavePath.ROOT) + "ait/" + uuid + ".json");
        if(!file.exists()) return;
        try {
            String fileContent = new String(Files.readAllBytes(file.toPath()));
            tardisses.put(uuid, GSON.fromJson(fileContent, Tardis.class));
        } catch (Exception e) {
            AITMod.LOGGER.warn("Tardis " + uuid + " does not exist");
            e.printStackTrace();
        }
    }

    public static void saveTardis(Tardis tardis) {
        File file = new File(AITMod.mcServer.getSavePath(WorldSavePath.ROOT) + "ait/" + tardis.getUuid() + ".json");
        if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath()));
            writer.write(GSON.toJson(tardis));
            writer.close();
        } catch (Exception e) {
            AITMod.LOGGER.warn("Couldnt save Tardis " + tardis.getUuid());
            e.printStackTrace();
        }
    }

}
