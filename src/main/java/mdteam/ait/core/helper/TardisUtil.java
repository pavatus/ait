package mdteam.ait.core.helper;

import mdteam.ait.api.tardis.IDesktop;
import mdteam.ait.api.tardis.ITardis;
import mdteam.ait.api.tardis.ITravel;
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
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@SuppressWarnings("unused")
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

    public static void teleport(ServerPlayerEntity player, ServerWorld world, BlockPos destination, float yaw, float pitch) {
        player.teleport(world, destination.getX(), destination.getY(), destination.getZ(), yaw, pitch);
    }

    public static void teleport(ServerPlayerEntity player, ServerWorld world, Vec3d destination, float yaw, float pitch) {
        player.teleport(world, destination.getX(), destination.getY(), destination.getZ(), yaw, pitch);
    }

    public static void teleport(ServerPlayerEntity player, AbsoluteBlockPos destination, float yaw, float pitch) {
        TardisUtil.teleport(player, (ServerWorld) destination.getWorld(), destination, yaw, pitch);
    }

    public static void teleport(ServerPlayerEntity player, AbsoluteBlockPos.Directed destination, float pitch) {
        TardisUtil.teleport(player, (ServerWorld) destination.getWorld(), destination, destination.getDirection().asRotation(), pitch);
    }

    public static BlockPos offsetInteriorDoorPosition(ITardis tardis) {
        return TardisUtil.offsetInteriorDoorPosition(tardis.getDesktop());
    }

    public static BlockPos offsetInteriorDoorPosition(IDesktop desktop) {
        return TardisUtil.offsetDoorPosition(desktop.getInteriorDoorPos());
    }

    public static BlockPos offsetExteriorDoorPosition(ITardis tardis) {
        return TardisUtil.offsetExteriorDoorPosition(tardis.getTravel());
    }

    public static BlockPos offsetExteriorDoorPosition(ITravel travel) {
        return TardisUtil.offsetDoorPosition(travel.getPosition());
    }

    public static BlockPos offsetDoorPosition(AbsoluteBlockPos.Directed pos) {
        return switch (pos.getDirection()) {
            case DOWN, UP -> throw new IllegalArgumentException("Cannot adjust door position with direction: " + pos.getDirection());
            case NORTH -> new BlockPos.Mutable(pos.getX() + 0.5, pos.getY(), pos.getZ() - 1);
            case SOUTH -> new BlockPos.Mutable(pos.getX() + 0.5, pos.getY(), pos.getZ() + 1);
            case EAST -> new BlockPos.Mutable(pos.getX() + 1, pos.getY(), pos.getZ() + 0.5);
            case WEST -> new BlockPos.Mutable(pos.getX() - 1, pos.getY(), pos.getZ() + 0.5);
        };
    }

    public static void teleportOutside(ITardis tardis, ServerPlayerEntity player) {
        AbsoluteBlockPos.Directed pos = tardis.getTravel().getPosition();

        TardisUtil.teleport(
                player, (ServerWorld) pos.getWorld(), TardisUtil.offsetDoorPosition(pos)
                        .toCenterPos(), pos.getDirection().asRotation(), player.getPitch()
        );
    }

    public static void teleportInside(ITardis tardis, ServerPlayerEntity player) {
        AbsoluteBlockPos.Directed pos = tardis.getDesktop().getInteriorDoorPos();

        TardisUtil.teleport(
                player, TardisUtil.getTardisDimension(), TardisUtil.offsetDoorPosition(pos)
                        .toCenterPos(), pos.getDirection().asRotation(), player.getPitch()
        );
    }
}
