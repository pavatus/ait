package mdteam.ait.core.helper;

import mdteam.ait.api.tardis.ITardis;
import mdteam.ait.api.tardis.ITardisManager;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.data.AbsoluteBlockPos;
import mdteam.ait.data.Corners;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.util.*;

public class TardisUtil {

    private static final Random random = new Random();

    private static MinecraftServer SERVER;
    private static ServerWorld TARDIS_DIMENSION;

    //FIXME: here
    public static void init(MinecraftServer server) {
        SERVER = server;
        TARDIS_DIMENSION = server.getWorld(AITDimensions.TARDIS_DIM_WORLD);
    }

    public static MinecraftServer getServer() {
        return SERVER;
    }

    public static ServerWorld getTardisDimension() {
        return TARDIS_DIMENSION;
    }

    public static boolean inBox(Box box, BlockPos pos) {
        return pos.getX() <= box.maxX && pos.getX() >= box.minX &&
                pos.getZ() <= box.maxZ && pos.getZ() >= box.minZ;
    }

    public static boolean inBox(Corners corners, BlockPos pos) {
        return inBox(corners.getBox(), pos);
    }

    public static DoorBlockEntity getDoor(ITardis tardis) {
        if (!(TardisUtil.getTardisDimension().getBlockEntity(tardis.getDesktop().getInteriorDoorPos()) instanceof DoorBlockEntity door))
            return null;

        return door;
    }

    public static ExteriorBlockEntity getExterior(ITardis tardis) {
        if (!(tardis.getTravel().getPosition().getBlockEntity() instanceof ExteriorBlockEntity exterior))
            return null;

        return exterior;
    }

    public static Corners findInteriorSpot() {
        BlockPos first = findRandomPlace();

        return new Corners(
                first, first.add(256, 0, 256)
        );
    }

    public static BlockPos findRandomPlace() {
        return new BlockPos(random.nextInt(100000), 0, random.nextInt(100000));
    }

    public static BlockPos findBlockInTemplate(StructureTemplate template, BlockPos pos, Direction direction, Block targetBlock) {
        List<StructureTemplate.StructureBlockInfo> list = template.getInfosForBlock(
                pos, new StructurePlacementData().setRotation(
                        TardisUtil.directionToRotation(direction)
                ), targetBlock
        );

        if (list.isEmpty())
            return null;

        return list.get(0).pos();
    }

    public static BlockRotation directionToRotation(Direction direction) {
        return switch (direction) {
            case NORTH -> BlockRotation.CLOCKWISE_180;
            case EAST -> BlockRotation.COUNTERCLOCKWISE_90;
            case WEST -> BlockRotation.CLOCKWISE_90;
            default -> BlockRotation.NONE;
        };
    }

    public static Optional<StructureTemplate> findStructure(Identifier identifier) {
        return TardisUtil.getServer().getStructureTemplateManager().getTemplate(identifier);
    }

    public static void teleport(ServerPlayerEntity player, ServerWorld world, BlockPos destination, float yaw, float pitch) {
        player.teleport(world, destination.getX(), destination.getY(), destination.getZ(), yaw, pitch);
    }

    public static void teleport(ServerPlayerEntity player, AbsoluteBlockPos destination, float yaw, float pitch) {
        TardisUtil.teleport(player, (ServerWorld) destination.getWorld(), destination, yaw, pitch);
    }

    public static void teleport(ServerPlayerEntity player, AbsoluteBlockPos.Directed destination, float pitch) {
        TardisUtil.teleport(player, (ServerWorld) destination.getWorld(), destination, destination.getDirection().asRotation(), pitch);
    }
}
