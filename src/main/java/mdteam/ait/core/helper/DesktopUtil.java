package mdteam.ait.core.helper;

import mdteam.ait.AITMod;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DesktopUtil {
    //public static ServerWorld getInteriorLevel(MinecraftServer server) {
        //return server.getWorld();
    //}
    public static List<AbsoluteBlockPos> getNextAvailableInteriorSpot() {
        List<AbsoluteBlockPos> list = new ArrayList<>();

        AbsoluteBlockPos bottomLeft = generateRandomPosInTARDISDim();
        AbsoluteBlockPos topRight = new AbsoluteBlockPos(DimensionUtil.getTardisDimension(), bottomLeft.add(256,0,256));

        list.add(bottomLeft);
        list.add(topRight);

        return list;
    }
    private static AbsoluteBlockPos generateRandomPosInTARDISDim() {
        Random random = new Random();

        int x = random.nextInt(100000);
        int z = random.nextInt(100000);
        AbsoluteBlockPos pos = new AbsoluteBlockPos(DimensionUtil.getTardisDimension(),new BlockPos(x,0,z));
        return pos;
    }
}

